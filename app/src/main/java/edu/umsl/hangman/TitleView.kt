package edu.umsl.hangman

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_title_view.*

class TitleView : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title_view)

        playButton.setOnClickListener{
            val intent = Intent(this, GameView::class.java)
            this.startActivity(intent)
            //this.finish()
        }

    }
}
