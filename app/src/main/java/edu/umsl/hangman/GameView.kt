package edu.umsl.hangman

import android.app.Activity
import android.content.Context
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_game_view.*
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast


class GameView : Activity() {

    private lateinit var game: Game

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_view)
        game = Game()
        changeButton.setOnClickListener{
            animate()
        }



    }

    private fun animate(){
//        val v = hangmanView.background as Animatable
//        v.start()
        hangmanView.anotherMistake()

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
            // animate correct mark

            //update phrase on screen
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

        inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.RESULT_UNCHANGED_HIDDEN)
        phraseView.text = game?.getUpdatedPhrase()
    }


}
