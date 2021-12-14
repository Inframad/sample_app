package com.test.a2021_q4_tyukavkin.domain.usecase

import com.test.a2021_q4_tyukavkin.domain.entity.LoanConditions
import com.test.a2021_q4_tyukavkin.domain.repository.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito

class GetLoanConditionsUsecaseTest {

    private val testLoanConditions =
        LoanConditions(
            maxAmount = 10000F,
            percent = 7.5,
            period = 30
        )

    private val loanConditions =
        LoanConditions(
            maxAmount = 10000F,
            percent = 7.5,
            period = 30
        )

    @ExperimentalCoroutinesApi
    @Test
    fun `WHEN invoke() EXPECT testLoanConditions`() {
        runTest {
            val mockRepository = Mockito.mock(Repository::class.java)
            Mockito.`when`(mockRepository.getLoanConditions()).thenReturn(loanConditions)
            val getLoanConditionsUsecase = GetLoanConditionsUsecase(mockRepository)

            val actual = getLoanConditionsUsecase()

            val expected = testLoanConditions

            assertEquals(expected, actual)
        }
    }
}