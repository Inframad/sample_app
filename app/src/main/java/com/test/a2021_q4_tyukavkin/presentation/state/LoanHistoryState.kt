package com.test.a2021_q4_tyukavkin.presentation.state

import android.view.View

enum class LoanHistoryState {
    LOADING {
        override val rvVisibility = View.INVISIBLE
        override val progressVisibility = View.VISIBLE
    },
    LOADED {
        override val rvVisibility = View.VISIBLE
        override val progressVisibility = View.INVISIBLE

    };

    abstract val rvVisibility: Int
    abstract val progressVisibility: Int
}