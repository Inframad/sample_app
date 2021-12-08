package com.test.a2021_q4_tyukavkin.ui

import androidx.recyclerview.widget.RecyclerView
import com.test.a2021_q4_tyukavkin.databinding.LoanItemBinding
import com.test.a2021_q4_tyukavkin.domain.entity.Loan

class LoanViewHolder(
    private val binding: LoanItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(loan: Loan, onClickItem: (Long) -> Unit) {
        binding.tempTv.text =
            "${loan.firstName} ${loan.lastName} \n" +
                    "${loan.percent} ${loan.state}"

        binding.root.setOnClickListener { onClickItem(loan.id) }
    }
}