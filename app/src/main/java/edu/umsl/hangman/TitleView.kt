package edu.umsl.hangman

import android.app.Activity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_title_view.*

class TitleView : Activity() {
    private var game: Game? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title_view)

        game = Game()

        playButton.setOnClickListener{
            Log.e("Clicked", "Hi")
            phraseView.text = game?.getPhrase()
        }
    }
}
