package com.illidant.easykanzicapstone.ui.screen.learn.writing

import com.illidant.easykanzicapstone.domain.model.Vocabulary

interface WritingContract {

    interface View {
        fun onSuccess(vocabularyList: List<Vocabulary>)

        fun onError(throwable: Throwable)

        fun onCorrectAnswer()

        fun onWrongAnswer(correctAnswer: String, userAnswer: String)
    }

    interface Presenter {
        fun vocabByLessonRequest(id: Int)

        fun checkAnswer(correctAnswer: String, userAnswer: String)
    }
}
