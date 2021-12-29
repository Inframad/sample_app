package com.test.a2021_q4_tyukavkin.presentation.state

import android.view.View

enum class UserAuthorizationFragmentState {
    DEFAULT{
        override val buttonsIsEnabled: Boolean
            get() = true
        override val progressVisibility: Int
            get() = View.INVISIBLE

    },
    LOADING{
        override val buttonsIsEnabled: Boolean
            get() = false
        override val progressVisibility: Int
            get() = View.VISIBLE

    },
    LOADED {
        override val buttonsIsEnabled: Boolean
            get() = true
        override val progressVisibility: Int
            get() = View.INVISIBLE

    },
    INVALID_CREDENTIALS {
        override val buttonsIsEnabled: Boolean
            get() = true
        override val progressVisibility: Int
            get() = View.INVISIBLE

    },
    NO_INTERNET_CONNECTION {
        override val buttonsIsEnabled: Boolean
            get() = false
        override val progressVisibility: Int
            get() = View.INVISIBLE
    },
    TIMEOUT_EXCEPTION {
        override val buttonsIsEnabled: Boolean
            get() = true
        override val progressVisibility: Int
            get() = View.INVISIBLE
    },
    BUSY_LOGIN {
        override val buttonsIsEnabled: Boolean
            get() = true
        override val progressVisibility: Int
            get() = View.INVISIBLE
    },
    BAD_RESPONSE {
        override val buttonsIsEnabled: Boolean
            get() = true
        override val progressVisibility: Int
            get() = View.INVISIBLE

    },
    UNKNOWN_ERROR {
        override val buttonsIsEnabled: Boolean
            get() = true
        override val progressVisibility: Int
            get() = View.INVISIBLE
    };

    abstract val buttonsIsEnabled: Boolean
    abstract val progressVisibility: Int
}