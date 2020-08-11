package com.illidant.easykanzicapstone.ui.screen.learn.writing

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Vocabulary
import com.illidant.easykanzicapstone.extension.toast
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.VocabularyRepository
import com.illidant.easykanzicapstone.platform.source.remote.VocabularyRemoteDataSource
import com.illidant.easykanzicapstone.ui.screen.learn.flashcard.FlashcardActivity
import com.illidant.easykanzicapstone.ui.screen.learn.multiple_choice.MultipleChoiceActivity
import com.illidant.easykanzicapstone.util.WritingMode
import kotlinx.android.synthetic.main.activity_writing.*
import kotlinx.android.synthetic.main.dialog_complete_writing.*
import kotlinx.android.synthetic.main.dialog_setting_writing.*

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
        tvQuestionNo.text = (currentIndex + 1).toString()

        switchOption.setOnClickListener {
            showSettingDialog()
        }

        buttonExit.setOnClickListener {
            finish()
        }

        buttonSubmit.setOnClickListener {
            val answer = when (writingMode) {
                WritingMode.Hiragana -> vocabularyList[currentIndex].hiragana
                WritingMode.Vietnamese -> vocabularyList[currentIndex].vocab_meaning
            }
            presenter.checkAnswer(answer, edtAnswer.text.toString().trim())
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
        tvQuestionNo.text = (currentIndex + 1).toString()
        progressBarWriting.progress = currentIndex + 1
        showQuestion(vocabularyList[currentIndex])
        updateWritingMode(writingMode)
        textHelper.setTextColor(Color.parseColor("#000000"))
    }

    private fun showQuestion(vocabulary: Vocabulary) {
        progressBarWriting.progress = currentIndex + 1
        tvKanjiQuestion.text = vocabulary.kanji_vocab
        edtAnswer.text?.clear()
        titleCorrectAnswer.visibility = View.INVISIBLE
        titleWrongAnswer.visibility = View.INVISIBLE
        tvAnswer.text = ""
        tvWrongAnswer.text = ""
    }

    private fun showCompleteDialog() {
        val dialog = Dialog(this).apply {
            setCancelable(false)
            getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            setContentView(R.layout.dialog_complete_writing)
            val lessonId = intent.getIntExtra("LESSON_ID", 0)

            btnLearnAgain.setOnClickListener {
                finish()
                startActivity(intent)
                dismiss()
            }

            btnMultipleChoice.setOnClickListener {
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
            getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
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
        if(titleWrongAnswer.visibility == View.VISIBLE){
            textHelper.text = getString(R.string.text_helper_rewrite_correct_answer)
            textHelper.setTextColor(Color.parseColor("#c0392d"))
        }else {
            textHelper.text = when (writingMode) {
                WritingMode.Hiragana -> "Write meaning by Hiragana"
                WritingMode.Vietnamese -> "Write meaning by Vietnamese"
            }
            textHelper.setTextColor(Color.parseColor("#000000"))
        }

        if (titleCorrectAnswer.visibility == View.VISIBLE) {
            tvAnswer.text = when (writingMode) {
                WritingMode.Hiragana -> vocabularyList[currentIndex].hiragana
                WritingMode.Vietnamese -> vocabularyList[currentIndex].vocab_meaning
            }
        }
    }

    override fun onSuccess(vocabularyList: List<Vocabulary>) {
        this.vocabularyList.addAll(vocabularyList)
        tvTotalQuestion.text = vocabularyList.size.toString()
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
        titleCorrectAnswer.visibility = View.VISIBLE
        titleWrongAnswer.visibility = View.VISIBLE
        edtAnswer.text?.clear()
        textHelper.text = getString(R.string.text_helper_rewrite_correct_answer)
        textHelper.setTextColor(Color.parseColor("#c0392d"))
        tvAnswer.text = correctAnswer
        tvWrongAnswer.text = userAnswer
    }
}