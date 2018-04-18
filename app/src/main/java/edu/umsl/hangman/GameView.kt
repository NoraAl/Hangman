package edu.umsl.hangman

import android.animation.AnimatorInflater
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.graphics.Rect
import android.os.Bundle
import android.text.InputType
import android.text.InputType.TYPE_CLASS_PHONE
import android.util.Log
import kotlinx.android.synthetic.main.activity_game_view.*
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import android.view.WindowManager
import android.opengl.ETC1.getHeight
import android.util.TypedValue
import android.R.anim
import android.view.animation.AnimationUtils
import android.view.animation.Animation
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
const val GAME = "game"

class GameView : Activity() {

    private lateinit var game: Game
    private lateinit var message :String
    private lateinit var imm: InputMethodManager
    private  var shown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_view)

        if (savedInstanceState!= null){
            game = savedInstanceState.getSerializable(GAME) as Game
        }else {
            game = Game()
        }

        message = getString(R.string.tap)
        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val barsHeight = getBars()
        shown = false
        phraseView.text = game?.getUpdatedPhrase()


        rootView.setOnClickListener {
            toggle()
        }

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        // game is only 5 strings and one boolean
        outState?.putSerializable(GAME,game.getGame())
    }

    private fun toggle(){
        phraseView.requestFocus()
        if (instructionText.text == message){ //keyboard is hidden by default in manifest file(stateHidden)
            instructionText.text = ""
            imm.toggleSoftInput( InputMethodManager.SHOW_FORCED,0)
        }
        else {
            instructionText.text = message
            imm.toggleSoftInput(0, 0)
        }
    }



    private fun getBars (): Int{
        val size = Point()
        windowManager.defaultDisplay.getSize(size)

        val density = resources.displayMetrics.density
        var statusBarHeight = (density * 24).toInt()
        var resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if ( resourceId != 0 ) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }

        var navigationBar = 0
        resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            navigationBar = resources.getDimensionPixelSize(resourceId)
        }
        return navigationBar+statusBarHeight

    }
    private fun animate(){
        hangmanView.anotherMistake()
    }

    private fun toast(text: String){
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, text, duration)
        toast.show()
    }


    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            toggle()
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
            val animator = ObjectAnimator.ofFloat(phraseView,
                    "translationX", 0f, 25f, -25f, 25f, -25f,15f, -15f, 6f, -6f, 0f)
            animator.duration = 1000
            animator.start()
        }
        return super.onKeyUp(keyCode, event)
    }


}
