package com.test.a2021_q4_tyukavkin.presentation.converter

import android.content.Context
import com.test.a2021_q4_tyukavkin.R
import com.test.a2021_q4_tyukavkin.domain.entity.Loan
import com.test.a2021_q4_tyukavkin.presentation.model.LoanPresentaion
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class Converter @Inject constructor(
    private val context: Context
) {

    fun convertToLoanPresentation(loan: Loan): LoanPresentaion {
        return LoanPresentaion(
            amount = loan.amount,
            date = DateTimeFormatter.ofPattern(context.getString(R.string.datePattern))
                .format(loan.offsetDateTime.atZoneSameInstant(ZoneId.systemDefault())),
            time = DateTimeFormatter.ofPattern(context.getString(R.string.timePattern))
                .format(loan.offsetDateTime.atZoneSameInstant(ZoneId.systemDefault())),
            firstName = loan.firstName,
            id = loan.id,
            lastName = loan.lastName,
            percent = context.getString(R.string.percent_tv, loan.percent.toString()),
            period = loan.period,
            phoneNumber = loan.phoneNumber,
            state = loan.state
        )
    }

}