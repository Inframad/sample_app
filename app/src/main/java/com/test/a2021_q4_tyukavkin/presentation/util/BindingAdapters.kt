package com.test.a2021_q4_tyukavkin.presentation.util

import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.test.a2021_q4_tyukavkin.R
import com.test.a2021_q4_tyukavkin.domain.entity.LoanState

@BindingAdapter("app:loanStatusColor") //TODO Snake case
fun loanStatusColor(view: TextView, status: LoanState?) {
    val color = getLoanStatusColor(status, view.context)

    view.setTextColor(color)
}

@BindingAdapter("app:loanStatusText") //TODO Snake case
fun loanStatusText(view: TextView, status: LoanState?) {
    view.text = when (status) {
        LoanState.APPROVED -> view.context.resources.getString(R.string.approved)
        LoanState.REGISTERED -> view.context.resources.getString(R.string.registered)
        LoanState.REJECTED -> view.context.resources.getString(R.string.rejected)
        else -> ""
    }
}

private fun getLoanStatusColor(status: LoanState?, context: Context): Int {
    val neutralColor = ContextCompat.getColor(context, R.color.neutral)
    return when (status) {
        LoanState.APPROVED -> ContextCompat.getColor(context, R.color.positive)
        LoanState.REGISTERED -> neutralColor
        LoanState.REJECTED -> ContextCompat.getColor(context, R.color.negative)
        else -> neutralColor
    }
}