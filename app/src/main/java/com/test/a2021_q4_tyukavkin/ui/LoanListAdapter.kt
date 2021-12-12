package com.test.a2021_q4_tyukavkin.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.test.a2021_q4_tyukavkin.databinding.LoanItemBinding
import com.test.a2021_q4_tyukavkin.presentation.model.LoanPresentaion

class LoanListAdapter (private val onClickItem: (Long) -> Unit):
    ListAdapter<LoanPresentaion, LoanViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoanViewHolder {
        val binding = LoanItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LoanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoanViewHolder, position: Int) {
        holder.bind(getItem(position), onClickItem)
    }

}

class DiffCallback : DiffUtil.ItemCallback<LoanPresentaion>() {
    override fun areItemsTheSame(oldItem: LoanPresentaion, newItem: LoanPresentaion): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: LoanPresentaion, newItem: LoanPresentaion): Boolean {
        return oldItem == newItem
    }
}