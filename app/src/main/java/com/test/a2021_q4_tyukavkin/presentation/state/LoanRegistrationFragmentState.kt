package com.test.a2021_q4_tyukavkin.presentation.state

import android.view.View

enum class LoanRegistrationFragmentState {

    LOADING {
        override val uiVisibility = View.INVISIBLE
        override val progressVisibility = View.VISIBLE
    },
    LOADED {
        override val uiVisibility = View.VISIBLE
        override val progressVisibility = View.INVISIBLE

    },
    INCORRECT_INPUT_DATA,
    UNKNOWN_HOST,
    TIMEOUT,
    UNKNOWN_ERROR;

    open val uiVisibility: Int = View.INVISIBLE
    open val progressVisibility: Int = View.VISIBLE

}