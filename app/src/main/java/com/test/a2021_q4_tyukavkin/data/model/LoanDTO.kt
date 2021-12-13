package com.test.a2021_q4_tyukavkin.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LoanDTO(
    val amount: Float,
    val date: String,
    val firstName: String,
    @PrimaryKey val id: Long,
    val lastName: String,
    val percent: Double,
    val period: Int,
    val phoneNumber: String,
    val state: String
)

