package edu.umsl.hangman

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_game_view.*
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Handler
import android.R.anim
import android.animation.AnimatorInflater
import android.animation.AnimatorSet




class GameView : Activity() {

    private lateinit var game: Game
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_view)


        textView = findViewById(R.id.phraseView)
        game = Game()
        showKKeyboard()

    }

    private fun toast(text: String){
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, text, duration)
        toast.show()
    }


    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        val key = event?.getUnicodeChar()?.toChar()?.toString()
        val isCorrect = game?.updateCurrentPhrase(key!!)
        if (isCorrect){
            phraseView.text = game?.getUpdatedPhrase()
            if (game?.isSolved()){
                toast("You Won!")}
            else
                toast("Correct! Try another one!")
        } else {
            //todo: hangman
            toast("Hard luck!")
        }


        return super.onKeyDown(keyCode, event)
    }



    private fun showKKeyboard(){
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS)
        phraseView.text = game?.getUpdatedPhrase()
    }


}
