package com.test.a2021_q4_tyukavkin.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.a2021_q4_tyukavkin.databinding.LoanItemBinding
import com.test.a2021_q4_tyukavkin.domain.entity.Loan

class LoanAdapter : RecyclerView.Adapter<LoanViewHolder>() {

    var loans: List<Loan> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoanViewHolder {
        val binding = LoanItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LoanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoanViewHolder, position: Int) =
        holder.bind(loans[position])

    override fun getItemCount(): Int =
        loans.size

}