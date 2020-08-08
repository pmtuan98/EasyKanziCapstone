package com.illidant.easykanzicapstone.ui.screen.learn.writing

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Vocabulary
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.VocabularyRepository
import com.illidant.easykanzicapstone.platform.source.remote.VocabularyRemoteDataSource
import com.illidant.easykanzicapstone.ui.screen.learn.LearnContract
import com.illidant.easykanzicapstone.ui.screen.learn.LearnPresenter
import com.illidant.easykanzicapstone.ui.screen.learn.flashcard.FlashcardActivity
import com.illidant.easykanzicapstone.ui.screen.learn.multiple_choice.MultipleChoiceActivity
import kotlinx.android.synthetic.main.activity_writing.*


class WritingActivity : AppCompatActivity(), LearnContract.View {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_writing)
        initialize()
    }

    private fun initialize() {
        val lesson_id = intent.getIntExtra("LESSON_ID", 0)
        presenter.vocabByLessonRequest(lesson_id)


        btnOption.setOnClickListener{
            showSettingDialog()
        }
        btnExit.setOnClickListener{
            finish()
        }

        val prefs: SharedPreferences = getSharedPreferences("com.illidant.kanji.prefs", Context.MODE_PRIVATE)
        val hiraMode = prefs.getBoolean("hiraState", true)
        val vietnamMode = prefs.getBoolean("vnState", true)
        if(hiraMode == true){ //check if hira mode on
            textAnswerField.helperText = "Write meaning by Hiragana"
        }else if (vietnamMode == true){
            textAnswerField.helperText = "Write meaning by Vietnamese"
        }
    }

    private val presenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val remote = VocabularyRemoteDataSource(retrofit)
        val repository = VocabularyRepository(remote)
        LearnPresenter(this, repository)
    }

    private fun showSettingDialog() {
        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_setting_writing)
        val buttonSave= dialog.findViewById(R.id.buttonSaveWritingMode) as TextView
        val radioHira = dialog.findViewById(R.id.radioHira) as RadioButton
        val radioVN = dialog.findViewById(R.id.radioVN) as RadioButton
        val prefs: SharedPreferences = getSharedPreferences("com.illidant.kanji.prefs", Context.MODE_PRIVATE)
        radioHira.isChecked = prefs.getBoolean("hiraState",radioHira.isChecked)
        radioVN.isChecked = prefs.getBoolean("vnState", radioVN.isChecked)
        dialog.show()
        buttonSave.setOnClickListener{
            if (radioHira.isChecked) {
                val prefs: SharedPreferences = getSharedPreferences("com.illidant.kanji.prefs", Context.MODE_PRIVATE)
                prefs.edit().putBoolean("hiraState", true).apply()
                prefs.edit().putBoolean("vnState", false).apply()
                val intent = intent
                finish()
                startActivity(intent)
                dialog.dismiss()
            }else if(radioVN.isChecked) {
                val prefs: SharedPreferences = getSharedPreferences("com.illidant.kanji.prefs", Context.MODE_PRIVATE)
                prefs.edit().putBoolean("vnState", true).apply()
                prefs.edit().putBoolean("hiraState", false).apply()
                val intent = intent
                finish()
                startActivity(intent)
                dialog.dismiss()
            }
        }
    }

    private fun showCompleteDialog () {
        val dialog = Dialog(this)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_complete_writing)
        val lesson_id = intent.getIntExtra("LESSON_ID", 0)
        val buttonAgain= dialog.findViewById(R.id.btnLearnAgain) as Button
        val buttonLearnMultiple = dialog.findViewById(R.id.btnMultipleChoice) as Button
        val buttonLearnFlashcard = dialog.findViewById(R.id.buttonLearnFlashcard) as Button
        dialog.show()
        buttonAgain.setOnClickListener {
            val intent = intent
            finish()
            startActivity(intent)
            dialog.dismiss()
        }
        buttonLearnMultiple.setOnClickListener{
            val intent = Intent(it.context, MultipleChoiceActivity::class.java)
            intent.putExtra("LESSON_ID", lesson_id)
            startActivity(intent)
            finish()
            dialog.dismiss()
        }
        buttonLearnFlashcard.setOnClickListener{
            val intent = Intent(it.context, FlashcardActivity::class.java)
            intent.putExtra("LESSON_ID", lesson_id)
            startActivity(intent)
            finish()
            dialog.dismiss()
        }
    }

    override fun getVocabByKanjiID(listVocab: List<Vocabulary>) {
       //Not use
    }

    override fun getVocabByLessonID(listVocab: List<Vocabulary>) {
        val prefs: SharedPreferences = getSharedPreferences("com.illidant.kanji.prefs", Context.MODE_PRIVATE)
        val hiraMode = prefs.getBoolean("hiraState", true)
        val vietnamMode = prefs.getBoolean("vnState", true)
        var counter = 0
        var handler = Handler(Looper.getMainLooper() /*UI thread*/)
        var workRunnable: Runnable? = null
        val kanjiList: MutableList<String> = mutableListOf()
        val vnList: MutableList<String> = mutableListOf()
        val hiraList: MutableList<String> = mutableListOf()
        // Add to list
        for (vocab in listVocab) {
            kanjiList.add(vocab.kanji_vocab)
            vnList.add(vocab.vocab_meaning)
            hiraList.add(vocab.hiragana)
        }

        tvTotalQuestion.text = kanjiList.size.toString()  // Display total question
        tvQuestionNo.text = (counter + 1).toString()     // Display current question

        tvKanjiQuestion.text = kanjiList[counter]  // Display first question
        var vnAnswer = vnList[counter]
        var hiraAnswer = hiraList[counter]

        //Set progress bar
        progressBarWriting.max = kanjiList.size
        progressBarWriting.progress = counter+1

        fun nextQuestion() {
            counter++
            if (counter == kanjiList.size) {
                counter = kanjiList.size - 1
                showCompleteDialog()
            }
            tvKanjiQuestion.text = kanjiList[counter]
            tvQuestionNo.text = (counter + 1).toString()
            edtAnswer.text?.clear()
            titleCorrectAnswer.visibility = View.INVISIBLE
            titleWrongAnswer.visibility = View.INVISIBLE
            tvWrongAnswer.text = ""
            tvAnswer.text = ""
            textAnswerField.isEndIconVisible = true
            progressBarWriting.progress = counter+1

            if(hiraMode == true){ //check if hira mode on
                hiraAnswer = hiraList[counter]
                textAnswerField.helperText = "Write meaning by Hiragana"
            }else if (vietnamMode == true) {
                vnAnswer = vnList[counter]
                textAnswerField.helperText = "Write meaning by Vietnamese"
            }
        }
        fun displayDialogCorrect(){
            val dialog = SweetAlertDialog(this@WritingActivity, SweetAlertDialog.SUCCESS_TYPE)
            dialog.hideConfirmButton()
            dialog.titleText = "Correct"
            dialog.show()
            handler.removeCallbacks(workRunnable)
            workRunnable = Runnable {
                dialog.dismiss()
                nextQuestion()
            }
            handler.postDelayed(workRunnable, 1500 /*delay*/)

        }

        fun forceInputCorrectAnswer() {
                edtAnswer.addTextChangedListener(object : TextWatcher {
                    override fun onTextChanged(text: CharSequence?, start: Int, before: Int, after: Int) {
                        if(!textAnswerField.isEndIconVisible &&
                            hiraMode == true && edtAnswer.text.toString().equals(hiraAnswer)){
                            displayDialogCorrect()
                        }
                        else if (!textAnswerField.isEndIconVisible &&
                            vietnamMode == true && edtAnswer.text.toString().equals(vnAnswer,ignoreCase = true)) {
                            displayDialogCorrect()
                        }
                    }
                    override fun afterTextChanged(p0: Editable?) {
                        //Not use
                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        //Not use
                    }
                })

        }

        btnHint.setOnClickListener {
            if(hiraMode == true) {
                titleCorrectAnswer.visibility = View.VISIBLE
                tvAnswer.text = hiraAnswer
            } else if(vietnamMode == true) {
                titleCorrectAnswer.visibility = View.VISIBLE
                tvAnswer.text = vnAnswer
            }
            textAnswerField.helperText = "Rewrite the correct answer"
            textAnswerField.isEndIconVisible = false
            forceInputCorrectAnswer()
        }

        fun checkWrongInputAnswer() {
            titleCorrectAnswer.visibility = View.VISIBLE
            titleWrongAnswer.visibility = View.VISIBLE
            edtAnswer.text?.clear()
            textAnswerField.helperText = "Rewrite the correct answer"
            textAnswerField.isEndIconVisible = false
            forceInputCorrectAnswer()
        }
        textAnswerField.setEndIconOnClickListener {
            val userAnswer = edtAnswer.text.toString()
            if (userAnswer.isEmpty()) { // Input answer blank
                if(hiraMode == true) {
                    hiraAnswer = hiraList[counter]
                    titleCorrectAnswer.visibility = View.VISIBLE
                    tvAnswer.text = hiraAnswer
                }else if (vietnamMode == true) {
                    vnAnswer = vnList[counter]
                    titleCorrectAnswer.visibility = View.VISIBLE
                    tvAnswer.text = vnAnswer
                }
                textAnswerField.helperText = "Rewrite the correct answer"
                textAnswerField.isEndIconVisible = false
                forceInputCorrectAnswer()
            } else { //Input answer not blank

                if(hiraMode == true && userAnswer.equals(hiraAnswer)) { //Check input answer correct
                    displayDialogCorrect()
                }else if(hiraMode == true && !userAnswer.equals(hiraAnswer)) {  //Check input answer not correct
                    tvWrongAnswer.text = userAnswer
                    checkWrongInputAnswer()
                    tvAnswer.text = hiraAnswer
                } else if(vietnamMode == true && userAnswer.equals(vnAnswer,ignoreCase = true)) { //Check input answer correct
                    displayDialogCorrect()
                } else if(vietnamMode == true && !userAnswer.equals(vnAnswer,ignoreCase = true)) {  //Check input answer not correct
                    tvAnswer.text = vnAnswer
                    tvWrongAnswer.text = userAnswer
                    checkWrongInputAnswer()
                }

              }

            }

        }
}