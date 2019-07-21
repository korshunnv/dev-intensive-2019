package ru.skillbranch.devintensive

import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.extensions.hideKeyboard
import ru.skillbranch.devintensive.models.Bender

class MainActivity : AppCompatActivity(), View.OnClickListener,  TextView.OnEditorActionListener {

    lateinit var benderImage : ImageView
    lateinit var textTxt : TextView
    lateinit var messageEd : EditText
    lateinit var sendBtn : ImageView

    lateinit var benderObj : Bender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //benderImage = findViewById(R.id.iv_bender)
        benderImage = iv_bender
        textTxt =tv_text
        messageEd = et_message
        sendBtn = iv_send

        val status = savedInstanceState?.getString("STATUS")?: Bender.Status.NORMAL.name
        val question = savedInstanceState?.getString("QUESTION")?: Bender.Question.NAME.name
        val answer = savedInstanceState?.getString("ANSWER")?: Bender.Question.NAME.answer[0]
        benderObj = Bender(Bender.Status.valueOf(status), Bender.Question.valueOf(question))
        messageEd.setText(answer)

        Log.d("M_MainActivity","onCreate $status $question")
        val (r,g,b) = benderObj.status.color
        benderImage.setColorFilter(Color.rgb(r,g,b), PorterDuff.Mode.MULTIPLY)

        textTxt.text = benderObj.askQueston()
        sendBtn.setOnClickListener(this)
        messageEd.setOnEditorActionListener(this)
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("M_MainActivity","onRestart")
    }

    override fun onStart() {
        super.onStart()
        Log.d("M_MainActivity","onStart ")
        /*val rect = Rect()
        this.window.decorView.getWindowVisibleDisplayFrame(rect) // this = activity
        Log.d("M_MainActivity","onStart Open ${isKeyboardOpen()} ${this.window.decorView.height} ${rect.height()}")*/
    }

    override fun onPause() {
        super.onPause()
        Log.d("M_MainActivity","onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("M_MainActivity","onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("M_MainActivity","onDestroy")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString("STATUS", benderObj.status.name)
        outState?.putString("QUESTION", benderObj.question.name)
        outState?.putString("ANSWER",messageEd.text.toString())
        Log.d("M_MainActivity","onSaveInstanceState ${benderObj.status.name} ${benderObj.question.name}")
    }

    override fun onClick(v: View?) {

        /*val rect = Rect()
        this.window.decorView.getWindowVisibleDisplayFrame(rect) // this = activity
        Log.d("M_MainActivity","onClick Open ${isKeyboardOpen()} ${this.window.decorView.height} ${rect.height()}")*/

        if (v?.id == R.id.iv_send){
            val (phrase, color) = benderObj.listenAnswer(messageEd.text.toString())
            messageEd.setText("")
            val (r,g,b) = color
            benderImage.setColorFilter(Color.rgb(r,g,b), PorterDuff.Mode.MULTIPLY)
            textTxt.text = phrase
            //Log.d("M_MainActivity","onClick Open ${isKeyboardOpen()}")
            //hideKeyboard()
        }
    }

    /*
    *actionDone
    Реализуй кнопку DONE в Software Keyboard (imeOptions="actionDone"), при нажатии на которую
    будет происходить отправка сообщения в экземпляр класса Bender и скрытие клавиатуры.
    Для этого реализуй OnEditorActionListener для EditText (et_message)
     */
    override fun onEditorAction(tv: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId==EditorInfo.IME_ACTION_DONE) {
            onClick(sendBtn)
            hideKeyboard()
            return true
        }
        return false
    }


}
