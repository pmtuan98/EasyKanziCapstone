package com.illidant.easykanzicapstone.ui.screen.ranking


import com.illidant.easykanzicapstone.domain.model.TestRanking

interface RankingContract {
    interface View {
        fun onRankingData(listRank: List<TestRanking>)
    }

    interface Presenter {
        fun getRankingByLevelIDRequest(id: Int)
    }
}