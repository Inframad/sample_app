package com.test.a2021_q4_tyukavkin.presentation

import android.view.View

enum class LoanDetailsState {
    LOADING {
        override val tvVisibility = View.INVISIBLE
        override val progressVisibility = View.VISIBLE
    },
    LOADED {
        override val tvVisibility = View.VISIBLE
        override val progressVisibility = View.INVISIBLE
    };

    abstract val tvVisibility: Int
    abstract val progressVisibility: Int
}