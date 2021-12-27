package com.test.a2021_q4_tyukavkin.ui

import androidx.recyclerview.widget.RecyclerView
import com.test.a2021_q4_tyukavkin.databinding.LoanItemBinding
import com.test.a2021_q4_tyukavkin.presentation.model.LoanPresentaion

class LoanViewHolder(
    private val binding: LoanItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(loan: LoanPresentaion, onClickItem: (Long) -> Unit) {
        binding.loanPresentation = loan

        binding.root.setOnClickListener { onClickItem(loan.id) }
    }
}