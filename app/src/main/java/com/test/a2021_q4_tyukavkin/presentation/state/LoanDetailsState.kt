package com.test.a2021_q4_tyukavkin.presentation.state

import android.view.View

enum class LoanDetailsState {
    LOADING {
        override val loanCardDetailVisibility = View.INVISIBLE
        override val progressVisibility = View.VISIBLE
    },
    LOADED {
        override val loanCardDetailVisibility = View.VISIBLE
        override val progressVisibility = View.INVISIBLE
    };

    abstract val loanCardDetailVisibility: Int
    abstract val progressVisibility: Int
}