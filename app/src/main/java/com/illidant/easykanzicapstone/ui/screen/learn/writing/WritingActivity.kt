package com.illidant.easykanzicapstone.ui.screen.learn.writing

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.core.widget.addTextChangedListener
import cn.pedant.SweetAlert.SweetAlertDialog
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Vocabulary
import com.illidant.easykanzicapstone.extension.toast
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.VocabularyRepository
import com.illidant.easykanzicapstone.platform.source.remote.VocabularyRemoteDataSource
import com.illidant.easykanzicapstone.ui.screen.learn.LearnContract
import com.illidant.easykanzicapstone.ui.screen.learn.LearnPresenter
import com.illidant.easykanzicapstone.ui.screen.learn.flashcard.FlashcardActivity
import com.illidant.easykanzicapstone.ui.screen.learn.multiple_choice.MultipleChoiceActivity
import com.illidant.easykanzicapstone.util.WritingMode
import kotlinx.android.synthetic.main.activity_writing.*
import kotlinx.android.synthetic.main.dialog_complete_writing.*
import kotlinx.android.synthetic.main.dialog_setting_writing.*
import kotlinx.android.synthetic.main.entry_test_layout.*

private const val DELAY = 1500L

class WritingActivity : AppCompatActivity(), WritingContract.View {

    private val presenter: WritingContract.Presenter by lazy {
        val retrofit = RetrofitService.getInstance(this).getService()
        val remote = VocabularyRemoteDataSource(retrofit)
        val repository = VocabularyRepository(remote)
        WritingPresenter(this, repository)
    }

    private val vocabularyList = mutableListOf<Vocabulary>()

    private var writingMode = WritingMode.Hiragana
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_writing)
        configViews()
        initData()
    }

    private fun configViews() {
        txt_questionNo.text = (currentIndex + 1).toString()

        switchOption.setOnClickListener {
            showSettingDialog()
        }

        buttonExit.setOnClickListener {
            finish()
        }

        btnHint.setOnClickListener {
            textViewCorrectAnswer.visibility = View.VISIBLE
            updateWritingMode(writingMode)
        }

        buttonSubmit.setOnClickListener {
            val answer = when (writingMode) {
                WritingMode.Hiragana -> vocabularyList[currentIndex].hiragana
                WritingMode.Vietnamese -> vocabularyList[currentIndex].vocab_meaning
            }
            presenter.checkAnswer(answer, editext_answer.text.toString().trim())
        }
    }

    private fun initData() {
        val prefs = getSharedPreferences("com.illidant.kanji.prefs", Context.MODE_PRIVATE)
        writingMode = WritingMode.fromInt(prefs.getInt("writingMode", 0))

        textHelper.text = when (writingMode) {
            WritingMode.Hiragana -> "Write meaning by Hiragana"
            WritingMode.Vietnamese -> "Write meaning by Vietnamese"
        }

        val lessonId = intent.getIntExtra("LESSON_ID", 0)
        presenter.vocabByLessonRequest(lessonId)
    }

    private fun nextQuestion() {
        currentIndex++
        if (currentIndex == vocabularyList.size) {
            currentIndex = vocabularyList.size - 1
            showCompleteDialog()
            return
        }

        txt_questionNo.text = (currentIndex + 1).toString()
        progressBarWriting.progress = currentIndex + 1
        showQuestion(vocabularyList[currentIndex])
    }

    private fun showQuestion(vocabulary: Vocabulary) {
        txt_kanji_question.text = vocabulary.kanji_vocab
        editext_answer.text?.clear()
        textViewCorrectAnswer.visibility = View.INVISIBLE
        textViewWrongAnswer.visibility = View.INVISIBLE
        txt_answer.text = ""
        txt_wrong_answer.text = ""
    }

    private fun showCompleteDialog() {
        val dialog = Dialog(this).apply {
            setCancelable(false)
            setContentView(R.layout.dialog_complete_writing)
            val lessonId = intent.getIntExtra("LESSON_ID", 0)

            buttonLearnAgain.setOnClickListener {
                finish()
                startActivity(intent)
                dismiss()
            }

            buttonMultipleChoice.setOnClickListener {
                val intent = Intent(this@WritingActivity, MultipleChoiceActivity::class.java)
                intent.putExtra("LESSON_ID", lessonId)
                startActivity(intent)
                finish()
                dismiss()
            }

            buttonLearnFlashcard.setOnClickListener {
                val intent = Intent(this@WritingActivity, FlashcardActivity::class.java)
                intent.putExtra("LESSON_ID", lessonId)
                startActivity(intent)
                finish()
                dismiss()
            }
        }
        dialog.show()
    }


    private fun showSettingDialog() {
        val dialog = Dialog(this).apply {
            setCancelable(true)
            setContentView(R.layout.dialog_setting_writing)
            when (writingMode) {
                WritingMode.Hiragana -> radioHira.isChecked = true
                WritingMode.Vietnamese -> radioVN.isChecked = true
            }
            buttonSaveWritingMode.setOnClickListener {
                writingMode = when (radioGroup.checkedRadioButtonId) {
                    radioVN.id -> WritingMode.Vietnamese
                    else -> WritingMode.Hiragana
                }
                val prefs = getSharedPreferences("com.illidant.kanji.prefs", Context.MODE_PRIVATE)
                prefs.edit().putInt("writingMode", writingMode.value).apply()
                updateWritingMode(writingMode)
                dismiss()
            }
        }
        dialog.show()
    }

    private fun updateWritingMode(writingMode: WritingMode) {
        textHelper.text = when (writingMode) {
            WritingMode.Hiragana -> "Write meaning by Hiragana"
            WritingMode.Vietnamese -> "Write meaning by Vietnamese"
        }

        if (textViewCorrectAnswer.visibility == View.VISIBLE) {
            txt_answer.text = when (writingMode) {
                WritingMode.Hiragana -> vocabularyList[currentIndex].hiragana
                WritingMode.Vietnamese -> vocabularyList[currentIndex].vocab_meaning
            }
        }
    }

    override fun onSuccess(vocabularyList: List<Vocabulary>) {
        this.vocabularyList.addAll(vocabularyList)
        txt_totalQuestion.text = vocabularyList.size.toString()
        showQuestion(vocabularyList.first())
    }

    override fun onError(throwable: Throwable) {
        throwable.localizedMessage?.let { toast(it) }
    }

    override fun onCorrectAnswer() {
        val dialog = SweetAlertDialog(this@WritingActivity, SweetAlertDialog.SUCCESS_TYPE)
        dialog.hideConfirmButton()
        dialog.titleText = "Correct"
        dialog.show()
        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismissWithAnimation()
            nextQuestion()
        }, DELAY)
    }

    override fun onWrongAnswer(correctAnswer: String, userAnswer: String) {
        textViewCorrectAnswer.visibility = View.VISIBLE
        textViewWrongAnswer.visibility = View.VISIBLE
        editext_answer.text?.clear()
        textHelper.text = getString(R.string.text_helper_rewrite_correct_answer)
        txt_answer.text = correctAnswer
        txt_wrong_answer.text = userAnswer
    }
}