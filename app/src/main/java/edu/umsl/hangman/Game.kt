package edu.umsl.hangman

import android.util.Log
import java.util.Random

/**
 * Created by nor on 4/12/18.
 */



class Game{
    private var phrase: String = ""
    private var hiddenPhrase: String = ""

    init{
        val phrases = Phrases()
        phrase = phrases.getRandomPhrase()
        hiddenPhrase = phrase

        val regexp = "[a-zA-z]".toRegex()// use this regular expression to remove the ocurrances of letters
        hiddenPhrase = regexp.replace(hiddenPhrase, "-")

    }

    fun getPhrase(): String{
        println("$hiddenPhrase")
        return hiddenPhrase
    }

    private fun showLetters(char: String): Boolean{
        //var hidden = phrase.toCharArray()
        var answerFlag = false

        val regex = char.toString().toRegex()
        val newHidden = regex.replace(hiddenPhrase, char)
        val isCorrect = hiddenPhrase != newHidden

        return isCorrect
    }

}


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
        val phrase = phrases?.get(random.nextInt(size))!!
        return phrase
    }

}