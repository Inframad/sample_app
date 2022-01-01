package com.test.a2021_q4_tyukavkin.screen

import android.view.View
import com.test.a2021_q4_tyukavkin.R
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.screen.Screen
import io.github.kakaocup.kakao.swiperefresh.KSwipeRefreshLayout
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

object LoansHistoryScreen : Screen<LoansHistoryScreen>() {

    val loansRv = KRecyclerView(
        builder = { withId(R.id.list) },
        itemTypeBuilder = { itemType(::LoanItem) }
    )

    val fab = KButton { withId(R.id.show_conditions_fab)}

    val swipeRefreshLayout = KSwipeRefreshLayout { withId(R.id.swiperefresh)}

    class LoanItem(parent: Matcher<View>) : KRecyclerItem<LoanItem>(parent) {

        val amountTv = KTextView(parent) { withId(R.id.amount_tv) }
        val percentTv = KTextView(parent) { withId(R.id.percent_tv) }
        val statusTv = KTextView(parent) { withId(R.id.status_tv) }
        val dateTv = KTextView(parent) { withId(R.id.date_tv) }
        val timeTv = KTextView(parent) { withId(R.id.time_tv) }

    }
}