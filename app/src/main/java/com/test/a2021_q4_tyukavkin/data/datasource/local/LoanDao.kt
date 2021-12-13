package com.test.a2021_q4_tyukavkin.data.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.test.a2021_q4_tyukavkin.data.model.LoanDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface LoanDao {

    @Query("SELECT * FROM loandto")
    fun getAll(): Flow<List<LoanDTO>>

    @Insert
    suspend fun insertAll(loans: List<LoanDTO>)

    @Query("DELETE FROM loandto")
    fun deleteAll()
}