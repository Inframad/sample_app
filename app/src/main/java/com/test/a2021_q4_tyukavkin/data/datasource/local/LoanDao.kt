package com.test.a2021_q4_tyukavkin.data.datasource.local

import androidx.room.*
import com.test.a2021_q4_tyukavkin.data.model.LoanDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface LoanDao {

    @Query("SELECT * FROM loandto")
    fun getAll(): Flow<List<LoanDTO>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(loans: List<LoanDTO>)

    @Query("DELETE FROM loandto")
    fun deleteAll()

    @Update
    fun updateLoans(loans: List<LoanDTO>)
}