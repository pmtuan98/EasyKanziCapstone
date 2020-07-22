package com.illidant.easykanzicapstone.ui.screen.learn.writing

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Vocabulary
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.VocabularyRepository
import com.illidant.easykanzicapstone.platform.source.remote.VocabularyRemoteDataSource
import com.illidant.easykanzicapstone.ui.screen.learn.LearnContract
import com.illidant.easykanzicapstone.ui.screen.learn.LearnPresenter
import kotlinx.android.synthetic.main.activity_writing_123.*


class WritingActivity : AppCompatActivity(), LearnContract.View {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_writing_123)
        initialize()


    }

    private fun initialize() {
        val lesson_id = intent.getIntExtra("LESSON_ID", 0)
        val lesson_name = intent.getStringExtra("LESSON_NAME")
        val level_name = intent.getStringExtra("LEVEL_NAME")
        presenter.vocabByLessonRequest(lesson_id)

        switchOption.setOnClickListener{
            showSettingDialog()
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
        dialog.setContentView(R.layout.setting_learn_writing_layout)
        val buttonSave= dialog.findViewById(R.id.buttonSaveWritingMode) as Button
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
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_flashcard)
        val buttonAgain= dialog.findViewById(R.id.buttonAgain) as Button
        dialog.show()
        buttonAgain.setOnClickListener {
            val intent = intent
            finish()
            startActivity(intent)
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
        val kanjiList: MutableList<String> = mutableListOf()
        val vnList: MutableList<String> = mutableListOf()
        val hiraList: MutableList<String> = mutableListOf()
        // Add to list
        for (vocab in listVocab) {
            kanjiList.add(vocab.kanji_vocab)
            vnList.add(vocab.vocab_meaning)
            hiraList.add(vocab.hiragana)
        }

        txt_totalQuestion.text = kanjiList.size.toString()  // Display total question
        txt_questionNo.text = (counter + 1).toString()     // Display current question

        txt_kanji_question.text = kanjiList[counter]  // Display first question
        var vnAnswer = vnList[counter]
        var hiraAnswer = hiraList[counter]

        //Set progress bar
        progressBarWriting.max = kanjiList.size
        progressBarWriting.progress = counter

        fun nextQuestion() {
            counter++
            if (counter == kanjiList.size) {
                counter = kanjiList.size - 1
                showCompleteDialog()
            }
            txt_kanji_question.text = kanjiList[counter]
            txt_questionNo.text = (counter + 1).toString()
            editext_answer.text?.clear()
            textViewCorrectAnswer.visibility = View.INVISIBLE
            textViewWrongAnswer.visibility = View.INVISIBLE
            txt_wrong_answer.text = ""
            txt_answer.text = ""
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
        fun forceInputCorrectAnswer() {
                editext_answer.addTextChangedListener(object : TextWatcher {
                    override fun onTextChanged(text: CharSequence?, start: Int, before: Int, after: Int) {
                        //Not use
                    }

                    override fun afterTextChanged(p0: Editable?) {
                        if(!textAnswerField.isEndIconVisible &&
                            hiraMode == true && editext_answer.text.toString().equals(hiraAnswer)){
                            nextQuestion()
                        }
                        else if (!textAnswerField.isEndIconVisible &&
                            vietnamMode == true && editext_answer.text.toString().equals(vnAnswer,ignoreCase = true)) {
                            nextQuestion()
                        }
                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        //Not use
                    }
                })

        }

        btnHint.setOnClickListener {
            if(hiraMode == true) {
                textViewCorrectAnswer.visibility = View.VISIBLE
                txt_answer.text = hiraAnswer
            } else if(vietnamMode == true) {
                textViewCorrectAnswer.visibility = View.VISIBLE
                txt_answer.text = vnAnswer
            }
            textAnswerField.helperText = "Rewrite the correct answer"
            textAnswerField.isEndIconVisible = false
            forceInputCorrectAnswer()
        }

        fun checkWrongInputAnswer() {
            textViewCorrectAnswer.visibility = View.VISIBLE
            textViewWrongAnswer.visibility = View.VISIBLE
            editext_answer.text?.clear()
            textAnswerField.helperText = "Rewrite the correct answer"
            textAnswerField.isEndIconVisible = false
            forceInputCorrectAnswer()
        }
        textAnswerField.setEndIconOnClickListener {
            val userAnswer = editext_answer.text.toString()
            if (userAnswer.isEmpty()) { // Input answer blank
                if(hiraMode == true) {
                    hiraAnswer = hiraList[counter]
                    textViewCorrectAnswer.visibility = View.VISIBLE
                    txt_answer.text = hiraAnswer
                }else if (vietnamMode == true) {
                    vnAnswer = vnList[counter]
                    textViewCorrectAnswer.visibility = View.VISIBLE
                    txt_answer.text = vnAnswer
                }
                textAnswerField.helperText = "Rewrite the correct answer"
                textAnswerField.isEndIconVisible = false
                forceInputCorrectAnswer()
            } else { //Input answer not blank

                if(hiraMode == true && userAnswer.equals(hiraAnswer)) { //Check input answer correct
                    nextQuestion()
                }else if(hiraMode == true && !userAnswer.equals(hiraAnswer)) {  //Check input answer not correct
                    txt_wrong_answer.text = userAnswer
                    checkWrongInputAnswer()
                    txt_answer.text = hiraAnswer
                } else if(vietnamMode == true && userAnswer.equals(vnAnswer,ignoreCase = true)) { //Check input answer correct
                    nextQuestion()
                } else if(vietnamMode == true && !userAnswer.equals(vnAnswer,ignoreCase = true)) {  //Check input answer not correct
                    txt_answer.text = vnAnswer
                    txt_wrong_answer.text = userAnswer
                    checkWrongInputAnswer()

                }

              }

            }

        }

    }