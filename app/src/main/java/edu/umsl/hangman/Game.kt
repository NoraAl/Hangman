package edu.umsl.hangman

import android.util.Log
import java.util.Random

/**
 * Created by nor on 4/12/18.
 */

 class Phrases{
    private val size = 2
    private var phrases: ArrayList<String>? = null
    init{
        phrases = ArrayList(size)
        phrases?.add("Red Apples")
        phrases?.add("green Apples")
        //phrases[2] = "Green Apples"
    }

    fun getRandomPhrase(): String{
        val random = Random()
        val phrase = phrases?.get(random.nextInt(size))
        Log.e("Hi", phrase)
        return phrase!!
    }




}

class Game{
    private var phrases: Phrases = Phrases()

    fun getPhrase(): String{

        return phrases?.getRandomPhrase()!!
    }

}