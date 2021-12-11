package com.test.a2021_q4_tyukavkin.presentation.state

import android.view.View

enum class LoanConditionsFragmentState {
    LOADING {
        override val loanConditionsCardVisibility: Int
            get() = View.INVISIBLE
        override val progressBarVisibility: Int
            get() = View.VISIBLE

    },
    LOADED {
        override val loanConditionsCardVisibility: Int
            get() = View.VISIBLE
        override val progressBarVisibility: Int
            get() = View.INVISIBLE

    };

    abstract val loanConditionsCardVisibility: Int
    abstract val progressBarVisibility: Int
}