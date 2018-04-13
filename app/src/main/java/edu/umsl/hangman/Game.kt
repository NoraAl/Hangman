package edu.umsl.hangman

import java.util.Random



class Game{
    private var answer: String = ""
    private var currentHidden: String = ""
    private var oldHidden: String =""
    private var pattern: String = ""
    private lateinit var regEx: Regex
    private var isSolved: Boolean = false

    init{
        val phrases = Phrases()
        answer = phrases.getRandomPhrase()
        scratchChar(" ")
        oldHidden = currentHidden
    }

    private fun scratchChar(char: String){
        // add current char to pattern
        // actually, it is all but that char, because eventually, we'll raplace them with '-'
        pattern += char
        regEx = Regex("(?i)[^"+pattern+"]")

        // show all occurrences of the current char
        currentHidden = regEx.replace(answer,"–")
        println (currentHidden)
    }

     fun updateCurrentPhrase(char: String): Boolean{
         scratchChar(char)
         if (currentHidden == answer)
             isSolved = true
         if (currentHidden != oldHidden){
             oldHidden = currentHidden
             return true
         }
         return false
    }

    fun getUpdatedPhrase(): String{
        println("$currentHidden")
        return currentHidden
    }

    fun isSolved(): Boolean{
        return isSolved
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