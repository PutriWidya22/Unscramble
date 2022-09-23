/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package com.putri.unscramble.ui.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.putri.unscramble.R
import com.putri.unscramble.databinding.GameFragmentBinding

// membuat class GameFragment
class GameFragment : Fragment() {

    // membuat viewModel
    // Jika fragmen dibuat ulang, maka akan menerima instance GameViewModel yang sama
    // yang dibuat oleh fragmen pertama.
    private val viewModel: GameViewModel by viewModels()

    // binding yang nantinya akan mengatur pada tampilan game_fragment.xml
    private lateinit var binding: GameFragmentBinding

    // membuat viewModel dengan onCreateView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Mengembangkan file XML dan mengembalikan instance objek yang ada.
        binding = GameFragmentBinding.inflate(inflater, container, false)
        Log.d("GameFragment", "GameFragment created/re-created!")
        Log.d("GameFragment", "Word: ${viewModel.currentScrambledWord} " +
                "Score: ${viewModel.score} WordCount: ${viewModel.currentWordCount}")
        return binding.root
    }

    // untuk menyimpan instance ke fragmen
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // membuat tombol submit dan skip menggunakan setOnClickListener
        binding.submit.setOnClickListener { onSubmitWord() }
        binding.skip.setOnClickListener { onSkipWord() }

        updateNextWordOnScreen()
        binding.score.text = getString(R.string.score, 0)
        binding.wordCount.text = getString(
            R.string.word_count, 0, MAX_NO_OF_WORDS)
    }

    // membuat fungsi submitWord yang nantinya akan dipanggil saat mengklik tombol submit.
    // Memeriksa kata dan memperbarui skors.
    // Menampilkan kata acak berikutnya.
    // menampilkan skors akhir.
    private fun onSubmitWord() {
        val playerWord = binding.textInputEditText.text.toString()

        if (viewModel.isUserWordCorrect(playerWord)) {
            setErrorTextField(false)
            if (viewModel.nextWord()) {
                updateNextWordOnScreen()
            } else {
                showFinalScoreDialog()
            }
        } else {
            setErrorTextField(true)
        }
    }

    // membuat fungsi onSkipWord
    // currentWordCount untuk menambah kata
    // menampilkan skors akhir.
    private fun onSkipWord() {
        if (viewModel.nextWord()) {
            setErrorTextField(false)
            updateNextWordOnScreen()
        } else {
            showFinalScoreDialog()
        }
    }

    // deklarasi variabel getNextScrambledWord untuk menampilkan kata acak
    private fun getNextScrambledWord(): String {
        val tempWord = allWordsList.random().toCharArray()
        tempWord.shuffle()
        return String(tempWord)
    }

    // Membuat dan menampilkan AlertDialog dengan skor akhir.
    private fun showFinalScoreDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.congratulations))
            .setMessage(getString(R.string.you_scored, viewModel.score))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.exit)) { _, _ ->
                exitGame()
            }
            .setPositiveButton(getString(R.string.play_again)) { _, _ ->
                restartGame()
            }
            .show()
    }

    // Inisialisasi ulang data di ViewModel dan memperbarui tampilan dengan data baru,
    // untuk mulai ulang permainan.
    private fun restartGame() {
        viewModel.reinitializeData()
        setErrorTextField(false)
        updateNextWordOnScreen()
    }

    // keluar dari permainan
    private fun exitGame() {
        activity?.finish()
    }
    override fun onDetach() {
        super.onDetach()
        Log.d("GameFragment", "GameFragment destroyed!")
    }

    // Mengatur ulang kesalahan teks.
    private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.textField.isErrorEnabled = true
            binding.textField.error = getString(R.string.try_again)
        } else {
            binding.textField.isErrorEnabled = false
            binding.textInputEditText.text = null
        }
    }

    // memperbarui kata permainan
    private fun updateNextWordOnScreen() {
        binding.textViewUnscrambledWord.text = viewModel.currentScrambledWord
    }
}