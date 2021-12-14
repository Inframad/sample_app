package com.test.a2021_q4_tyukavkin.presentation.state

import android.view.View

enum class UserAuthorizationFragmentState {
    DEFAULT{
        override val buttonsIsEnabled: Boolean
            get() = true
        override val progressVisibility: Int
            get() = View.INVISIBLE
        override val warningMsg: String
            get() = ""

    },
    LOADING{
        override val buttonsIsEnabled: Boolean
            get() = false
        override val progressVisibility: Int
            get() = View.VISIBLE
        override val warningMsg: String
            get() = ""

    },
    LOADED {
        override val buttonsIsEnabled: Boolean
            get() = true
        override val progressVisibility: Int
            get() = View.INVISIBLE
        override val warningMsg: String
            get() = ""

    },
    INVALID_CREDENTIALS {
        override val buttonsIsEnabled: Boolean
            get() = true
        override val progressVisibility: Int
            get() = View.INVISIBLE
        override val warningMsg: String
            get() = "Неверный логин или пароль" //TODO Hardcore

    },
    NO_INTERNET_CONNECTION {
        override val buttonsIsEnabled: Boolean
            get() = false
        override val progressVisibility: Int
            get() = View.INVISIBLE
        override val warningMsg: String
            get() = "Проверьте интернет соединение" //TODO Hardcore
    },
    TIMEOUT_EXCEPTION {
        override val buttonsIsEnabled: Boolean
            get() = true
        override val progressVisibility: Int
            get() = View.INVISIBLE
        override val warningMsg: String
            get() = "Время ожидания ответа сервера истекло" //TODO Hardcore
    },
    BUSY_LOGIN {
        override val buttonsIsEnabled: Boolean
            get() = true
        override val progressVisibility: Int
            get() = View.INVISIBLE
        override val warningMsg: String
            get() = "Логин занят"

    };

    abstract val buttonsIsEnabled: Boolean
    abstract val progressVisibility: Int
    abstract val warningMsg: String
}