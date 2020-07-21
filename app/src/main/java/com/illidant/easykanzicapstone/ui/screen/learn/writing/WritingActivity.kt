package com.illidant.easykanzicapstone.ui.screen.learn.writing

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Vocabulary
import com.illidant.easykanzicapstone.extension.isNotEmptyAndBlank
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.VocabularyRepository
import com.illidant.easykanzicapstone.platform.source.remote.VocabularyRemoteDataSource
import com.illidant.easykanzicapstone.ui.screen.learn.LearnActivity
import com.illidant.easykanzicapstone.ui.screen.learn.LearnContract
import com.illidant.easykanzicapstone.ui.screen.learn.LearnPresenter
import kotlinx.android.synthetic.main.activity_flashcard.*
import kotlinx.android.synthetic.main.activity_level.*
import kotlinx.android.synthetic.main.activity_writing_123.*
import kotlinx.android.synthetic.main.setting_learn_writing_layout.*


class WritingActivity : AppCompatActivity(), LearnContract.View {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_writing_123)
        initialize()

        switchOption.setOnClickListener{
            navigateToSetting()
        }
    }

    private fun initialize() {
        val lesson_id = intent.getIntExtra("LESSON_ID", 0)
        val lesson_name = intent.getStringExtra("LESSON_NAME")
        val level_name = intent.getStringExtra("LEVEL_NAME")
        presenter.vocabByLessonRequest(1)

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

    private fun navigateToSetting() {
        val intent = Intent(this, SettingWritingActivity::class.java)
        startActivity(intent)
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
                            vietnamMode == true && editext_answer.text.toString().equals(vnAnswer)) {
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
                } else if(vietnamMode == true && userAnswer.equals(vnAnswer)) { //Check input answer correct
                    nextQuestion()
                } else if(vietnamMode == true && !userAnswer.equals(vnAnswer)) {  //Check input answer not correct
                    txt_answer.text = vnAnswer
                    txt_wrong_answer.text = userAnswer
                    checkWrongInputAnswer()

                }

              }

            }

        }

    }