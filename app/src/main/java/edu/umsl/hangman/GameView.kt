package edu.umsl.hangman

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


class GameView : Activity() {

    private lateinit var game: Game
    var t  = true
    private  var shown = false
    private lateinit var imm: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_view)

        game = Game()
        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val barsHeight = set()
        shown = false
        phraseView.text = game?.getUpdatedPhrase()

        phraseView.setOnClickListener{
            //animate()
            if (shown){
                //Log.e("Button","it is shown")
                //toggleEdit(false)

                toggle(false)


            } else {
                //Log.e("Button","it is hidden")
                //toggleEdit(true)
                toggle(true)

            }
        }

        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)
            val currentViewHeight = rootView.rootView.height - (rect.bottom - rect.top)

            shown = currentViewHeight > barsHeight
            if (shown) {
                rootView.rootView.top =  rootView.rootView.top - 100
                Log.e("root", "shown, $currentViewHeight $barsHeight")
            }
            else
                Log.e("root", "hidden, $currentViewHeight $barsHeight")
        }



    }

    fun set (): Int{
        val size = Point()
        windowManager.defaultDisplay.getSize(size)

        val screenHeight = size.y

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

    override fun onResume() {
        super.onResume()
        //Log.e("onResume","$shown")
        //toggleEdit(true)
    }

    override fun onPause() {
        super.onPause()
        //Log.e("onPause","Accepting")
        //toggle(false)
    }

    override fun onDestroy() {
        super.onDestroy()
//        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        if (imm.isAcceptingText) {
//
//            toggle(false)
//            Log.e("onDestroy","Accepting")
//        }
    }
//    private fun ss(){
//
//        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY)
//    }



    private fun toggle(show:Boolean){
        if (show && shown){//already
            Log.e("toggleEdit","it is already shown")
            return
        }
        if (show){//not but show
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            Log.e("toggleEdit","show")
            return
        }
        if (!shown){//not and hide
            Log.e("toggleEdit","it is already hidden")
            return
        }
        shown = false
        imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY)
        Log.e("toggleEdit","hide")
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


}
