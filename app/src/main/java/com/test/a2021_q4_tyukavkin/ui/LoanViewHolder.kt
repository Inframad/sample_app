package com.test.a2021_q4_tyukavkin.ui

import androidx.recyclerview.widget.RecyclerView
import com.test.a2021_q4_tyukavkin.databinding.LoanItemBinding
import com.test.a2021_q4_tyukavkin.presentation.model.LoanPresentaion

class LoanViewHolder(
    private val binding: LoanItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(loan: LoanPresentaion, onClickItem: (Long) -> Unit) {

        binding.apply {
            amountTv.text = loan.amount.toString()
            percentTv.text = loan.percent
            statusTv.text = loan.state
            dateTv.text = loan.date
            timeTv.text = loan.time
        }

        binding.root.setOnClickListener { onClickItem(loan.id) }
    }
}