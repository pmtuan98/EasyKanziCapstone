package com.illidant.easykanzicapstone.ui.screen.quiz

import com.illidant.easykanzicapstone.domain.model.Quiz

interface QuizContract {
    interface View{

        fun getQuizByLessonID(listQuiz: List<Quiz>)
    }

    interface Presenter{
        fun quizByLessonRequest(id: Int)
    }
}