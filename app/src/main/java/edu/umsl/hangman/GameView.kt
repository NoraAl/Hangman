package edu.umsl.hangman

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_game_view.*
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast


class GameView : Activity() {

    private lateinit var game: Game
    var t  = true
    var shown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_view)
        game = Game()
        showKeyboard()
        changeButton.setOnClickListener{
            //animate()
            if (shown){

                toggle(false)


            } else {

                toggle(true)

            }
        }


    }

    override fun onResume() {
        super.onResume()
        toggle(true)
    }

    override fun onPause() {
        super.onPause()
        Log.e("onPause","Accepting")
        //toggle(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isAcceptingText) {

            toggle(false)
            Log.e("onDestroy","Accepting")
        }
    }

    private fun toggle(show:Boolean){
        if (shown && show)
            return
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (show){
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            shown = true
            return
        }
        shown = false

        imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY)
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

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            currentFocus?.let {
                val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputManager?.hideSoftInputFromInputMethod(currentFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
            return super.onKeyUp(keyCode, event)
        }


        val key = event?.unicodeChar?.toChar()?.toString()
        val isCorrect = game?.updateCurrentPhrase(key!!)

        if (isCorrect){
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
        return super.onKeyUp(keyCode, event)
    }

    private fun showKeyboard(){
        toggle(true)
        phraseView.text = game?.getUpdatedPhrase()
    }


}
