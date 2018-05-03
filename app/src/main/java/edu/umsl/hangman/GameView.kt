package edu.umsl.hangman

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_game_view.*



class GameView : Activity() {
    companion object {
        const val GAME = "game"
        const val MISTAKES = "mistakes"
    }

    private lateinit var game: Game
    private lateinit var message: String
    private lateinit var imm: InputMethodManager
    private var shown = false
    private var mistakeCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_view)
        message = getString(R.string.tap)
        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        shown = false


        if (savedInstanceState == null) {
            game = Game()
        } else {
            game = savedInstanceState.getSerializable(GAME) as Game
            instructionText.text = message
            mistakeCount = savedInstanceState.getInt(MISTAKES)
            hangmanView.setMistakes(mistakeCount)
        }

        phraseView.text = game?.getUpdatedPhrase()
        rootView.setOnClickListener {
            toggle()
        }

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putSerializable(GAME, game.getGame())
        outState?.putInt(MISTAKES, mistakeCount)
    }

    private fun toggle() {
        phraseView.requestFocus()
        if (instructionText.text == message) { //keyboard is hidden by default in manifest file(stateHidden)
            instructionText.text = ""
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        } else {
            instructionText.text = message
            imm.toggleSoftInput(0, 0)
        }
    }


    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK || HangmanCanvas.animating) {
            toggle()
            return super.onKeyUp(keyCode, event)
        }


        val key = event?.unicodeChar?.toChar()?.toString()
        val isCorrect = game?.updateCurrentPhrase(key!!)

        if (isCorrect) {
            //update phrase on screen
            phraseView.text = game?.getUpdatedPhrase()
            if (game?.isSolved()) {
                hangmanView.animateCorrect()
                val handler = Handler()
                handler.postDelayed({ finish() }, 1600)
            }
        } else {
            val gameover = hangmanView.anotherMistake()
            mistakeCount++
            val animator = ObjectAnimator.ofFloat(phraseView,
                    "translationX", 0f, 25f, -25f, 25f, -25f, 15f, -15f, 6f, -6f, 0f)
            animator.duration = 1000
            animator.start()


            if (gameover) {
                val handler = Handler()
                handler.postDelayed({ finish() }, 1600)

            }

        }
        return super.onKeyUp(keyCode, event)
    }


}
