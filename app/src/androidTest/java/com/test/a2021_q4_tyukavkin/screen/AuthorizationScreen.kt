package com.test.a2021_q4_tyukavkin.screen

import com.test.a2021_q4_tyukavkin.R
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.screen.Screen
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView

object AuthorizationScreen : Screen<AuthorizationScreen>() {

    val loginEt = KEditText { withId(R.id.login_et) }
    val passwordEt = KEditText { withId(R.id.password_et) }
    val loginBtn = KButton { withId(R.id.login_btn) }
    val registerBtn = KButton { withId(R.id.register_btn) }
    val warningTv = KTextView { withId(R.id.warning_message_tv) }
}