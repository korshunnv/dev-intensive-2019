package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.ImageView.ScaleType.CENTER_CROP
import android.widget.ImageView.ScaleType.CENTER_INSIDE
import androidx.core.content.ContextCompat
import ru.skillbranch.devintensive.App
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.utils.Utils
import androidx.annotation.ColorRes as ColorRes

/*
Реализуй CustomView с названием класса CircleImageView и кастомными xml атрибутами
cv_borderColor (цвет границы (format="color") по умолчанию white) и
cv_borderWidth (ширина границы (format="dimension") по умолчанию 2dp).

CircleImageView должна превращать установленное изображение в круглое изображение с цветной рамкой,
у CircleImageView должны быть реализованы методы
getBorderWidth():Int,
setBorderWidth(dp:Int),
getCv_borderColor():Color,
setCv_borderColor(hex:String),
setCv_borderColor(@ColorRes colorId: Int).

Используй CircleImageView как ImageView для аватара пользователя (@id/iv_avatar)
 */

class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0)
    : ImageView(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_CV_BORDER_WIDTH = 2
        private const val DEFAULT_CV_BORDER_COLOR:Int = Color.WHITE
    }

    // Properties
    private val paint: Paint = Paint().apply { isAntiAlias = true }
    private val paintBorder: Paint = Paint().apply { isAntiAlias = true }
    private val paintBackground: Paint = Paint().apply { isAntiAlias = true }
    private var circleCenter = 0
    private var heightCircle: Int = 0

    private var borderColor = DEFAULT_CV_BORDER_COLOR
    private var borderWidth = DEFAULT_CV_BORDER_WIDTH

    fun getBorderWidth():Int = borderWidth
    fun setBorderWidth(dp:Int){
        borderWidth = dp
        this.invalidate()
    }

    fun getBorderColor(): Int = borderColor

    fun setBorderColor(hex:String){
        borderColor = Color.parseColor(hex)
        this.invalidate()
    }

    fun setBorderColor(@ColorRes colorId: Int){
        borderColor = colorId//ContextCompat.getColor(App.applicationContext(), colorId)
        this.invalidate()
    }

    // Color Filter
    private var civColorFilter: ColorFilter? = null
        set(value) {
            if (field != value) {
                field = value
                if (field != null) {
                    civDrawable = null // To force re-update shader
                    invalidate()
                }
            }
        }

    // Object used to draw
    private var civImage: Bitmap? = null
    private var civDrawable: Drawable? = null

    init {
        init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        // Load the styled attributes and set their properties
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyleAttr, 0)

        // Init Border
        val defaultBorderSize = DEFAULT_CV_BORDER_WIDTH * getContext().resources.displayMetrics.density
        borderWidth = attributes.getDimension(R.styleable.CircleImageView_cv_borderWidth, defaultBorderSize).toInt()
        borderColor = attributes.getColor(R.styleable.CircleImageView_cv_borderColor, DEFAULT_CV_BORDER_COLOR)

        attributes.recycle()
    }
    //endregion

    //region Set Attr Method
    override fun setColorFilter(colorFilter: ColorFilter) {
        civColorFilter = colorFilter
    }

    override fun getScaleType(): ScaleType =
        super.getScaleType().let { if (it == null || it != CENTER_INSIDE) CENTER_CROP else it }

    override fun setScaleType(scaleType: ScaleType) {
        if (scaleType != CENTER_CROP && scaleType != CENTER_INSIDE) {
            throw IllegalArgumentException(String.format("ScaleType %s not supported. " + "Just ScaleType.CENTER_CROP & ScaleType.CENTER_INSIDE are available for this library.", scaleType))
        } else {
            super.setScaleType(scaleType)
        }
    }
    //endregion

    //region Draw Method
    override fun onDraw(canvas: Canvas) {
        // Load the bitmap
        loadBitmap()

        // Check if civImage isn't null
        if (civImage == null) return

        val circleCenterWithBorder = circleCenter + borderWidth.toFloat()
        paintBorder.color = borderColor
        paint.color = borderColor
        paintBackground.color = context.getColor(R.color.color_accent)
        // Draw Border
        canvas.drawCircle(circleCenterWithBorder, circleCenterWithBorder, circleCenterWithBorder, paintBorder)
        // Draw Circle background
        canvas.drawCircle(circleCenterWithBorder, circleCenterWithBorder, circleCenter.toFloat(), paintBackground)
        // Draw CircularImageView
        canvas.drawCircle(circleCenterWithBorder, circleCenterWithBorder, circleCenter.toFloat(), paint)
    }

    private fun update() {
        if (civImage != null)
            updateShader()

        val usableWidth = width - (paddingLeft + paddingRight)
        val usableHeight = height - (paddingTop + paddingBottom)

        heightCircle = Math.min(usableWidth, usableHeight)

        circleCenter = (heightCircle - borderWidth * 2).toInt() / 2

        invalidate()
    }

    private fun loadBitmap() {
        if (civDrawable == drawable) return

        civDrawable = drawable
        civImage = drawableToBitmap(civDrawable)
        updateShader()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        update()
    }

    private fun updateShader() {
        civImage?.also {
            // Create Shader
            val shader = BitmapShader(it, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

            // Center Image in Shader
            val scale: Float
            val dx: Float
            val dy: Float

            when (scaleType) {
                CENTER_CROP -> if (it.width * height > width * it.height) {
                    scale = height / it.height.toFloat()
                    dx = (width - it.width * scale) * 0.5f
                    dy = 0f
                } else {
                    scale = width / it.width.toFloat()
                    dx = 0f
                    dy = (height - it.height * scale) * 0.5f
                }
                CENTER_INSIDE -> if (it.width * height < width * it.height) {
                    scale = height / it.height.toFloat()
                    dx = (width - it.width * scale) * 0.5f
                    dy = 0f
                } else {
                    scale = width / it.width.toFloat()
                    dx = 0f
                    dy = (height - it.height * scale) * 0.5f
                }
                else -> {
                    scale = 0f
                    dx = 0f
                    dy = 0f
                }
            }

            shader.setLocalMatrix(Matrix().apply {
                setScale(scale, scale)
                postTranslate(dx, dy)
            })

            // Set Shader in Paint
            paint.shader = shader

            // Apply colorFilter
            paint.colorFilter = civColorFilter
        }
    }

    private fun drawableToBitmap(drawable: Drawable?): Bitmap? =
        when (drawable) {
            null -> null
            is BitmapDrawable -> drawable.bitmap
            else -> try {
                // Create Bitmap object out of the drawable
                val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
                bitmap
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    //endregion

    //region Measure Method
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = measure(widthMeasureSpec)
        val height = measure(heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    private fun measure(measureSpec: Int): Int {
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        return when (specMode) {
            MeasureSpec.EXACTLY -> specSize // The parent has determined an exact size for the child.
            MeasureSpec.AT_MOST -> specSize // The child can be as large as it wants up to the specified size.
            else -> heightCircle // The parent has not imposed any constraint on the child.
        }
    }
    //endregion
}