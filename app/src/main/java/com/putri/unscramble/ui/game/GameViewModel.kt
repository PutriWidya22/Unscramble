package com.putri.unscramble.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel

// membuat class GameViewModel
// mendeklarasikan variabel _score
class GameViewModel : ViewModel(){
    private var _score = 0
    val score: Int
        get() = _score

    // deklarasi variabel _currentWordCount dengan nilai awal 0
    // tipe data int
    private var _currentWordCount = 0
    val currentWordCount: Int
        get() = _currentWordCount

    // deklarasi variabel _currentScrambledWord dengan tipe data String
    private lateinit var _currentScrambledWord: String
    val currentScrambledWord: String
        get() = _currentScrambledWord

    // deklarasi variabel wordsList yang menyertakan list permainan pada file ListofWords.kt
    private var wordsList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String

    init {
        Log.d("GameFragment", "GameViewModel created!")
        getNextWord()
    }

    // membuat fungsi untuk menyelesaikan atau menghapus
    override fun onCleared() {
        super.onCleared()
        Log.d("GameFragment", "GameViewModel destroyed!")
    }

    // Memperbarui kata dan mengacak kata saat ini dengan kata berikutnya.
    private fun getNextWord() {
        currentWord = allWordsList.random()
        val tempWord = currentWord.toCharArray()
        tempWord.shuffle()

        while (String(tempWord).equals(currentWord, false)) {
            tempWord.shuffle()
        }
        if (wordsList.contains(currentWord)) {
            getNextWord()
        } else {
            _currentScrambledWord = String(tempWord)
            ++_currentWordCount
            wordsList.add(currentWord)
        }
    }

    // Inisialisasi ulang data permainan untuk memulai ulang.
    fun reinitializeData() {
        _score = 0
        _currentWordCount = 0
        wordsList.clear()
        getNextWord()
    }

    // Membuat skor meningkat jika jawaban permainan benar.
    private fun increaseScore() {
        _score += SCORE_INCREASE
    }

    // Mengembalikan nilai benar jika permainan benar serta meningkatkan skor
    fun isUserWordCorrect(playerWord: String): Boolean {
        if (playerWord.equals(currentWord, true)) {
            increaseScore()
            return true
        }
        return false
    }

    // mengembalikan nilai benar jika nilai kurang dari MAX_NO_OF_WORDS
    fun nextWord(): Boolean {
        return if (_currentWordCount < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else false
    }
}