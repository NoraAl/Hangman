package edu.umsl.hangman

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_game_view.*

class GameView : Activity() {

    private var game: Game? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_view)

        game = Game()

        ssButton.setOnClickListener{
            phraseView.text = game?.getPhrase()
        }
    }
}
