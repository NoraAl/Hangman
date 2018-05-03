package edu.umsl.hangman

import java.io.Serializable
import java.util.Random



class Game:Serializable{
    private var answer: String = ""
    private var currentHidden: String = ""
    private var oldHidden: String =""
    private var pattern: String = ""
    private var isSolved: Boolean = false



    init{
            val phrases = Phrases()
            answer = phrases.getRandomPhrase()
            scratchChar(" ")
            oldHidden = currentHidden
    }

    fun getGame(): Serializable{
        return this
    }

    private fun scratchChar(char: String){
        // add current char to pattern
        // actually, it is all but that char, because eventually, we'll raplace them with '-'
        pattern += char
        val regEx = Regex("(?i)[^"+pattern+"]")

        // show all occurrences of the current char
        currentHidden = regEx.replace(answer,"–")
        println (currentHidden)
    }

    // returns true if correct
     fun updateCurrentPhrase(char: String): Boolean{
         scratchChar(char)
         if (currentHidden == answer) isSolved = true
         if (currentHidden != oldHidden) oldHidden = currentHidden


         return answer.toUpperCase().contains(char.toUpperCase() )
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
        phrases?.add("Blue color")
        phrases?.add("Green color")
        phrases?.add("Red color")
        phrases?.add("Gray color")
        phrases?.add("Brown color")
        phrases?.add("Purple color")
        phrases?.add("navy color")
        phrases?.add("Pink color")
        phrases?.add("Black color")
        phrases?.add("White color")
        phrases?.add("Orange color")
        phrases?.add("Yellow color")
    }

    fun getRandomPhrase(): String{
        val random = Random()
        val phrase = phrases?.get(random.nextInt(phrases?.size!!))!!
        return phrase
    }

}