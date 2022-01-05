package com.test.a2021_q4_tyukavkin.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.test.a2021_q4_tyukavkin.domain.entity.Loan
import com.test.a2021_q4_tyukavkin.domain.entity.LoanState
import com.test.a2021_q4_tyukavkin.domain.usecase.GetLoanDataUsecase
import com.test.a2021_q4_tyukavkin.presentation.converter.Converter
import com.test.a2021_q4_tyukavkin.presentation.model.LoanPresentaion
import com.test.a2021_q4_tyukavkin.presentation.state.FragmentState
import com.test.a2021_q4_tyukavkin.util.CoroutinesTestRule
import com.test.a2021_q4_tyukavkin.util.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockkClass
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.time.OffsetDateTime

@ExperimentalCoroutinesApi
class LoanDetailsFragmentViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private lateinit var getLoanDataUsecase: GetLoanDataUsecase
    private lateinit var converter: Converter

    @Before
    fun setup() {
        getLoanDataUsecase = mockkClass(GetLoanDataUsecase::class)
        converter = mockkClass(Converter::class)
    }

    private val testLoan =
        Loan(
            amount = 10000F,
            offsetDateTime = OffsetDateTime.MAX,
            firstName = "John",
            lastName = "Johnson",
            id = 1L,
            percent = 10.5,
            period = 30,
            phoneNumber = "(123) 456-7890",
            state = LoanState.REGISTERED
        )

    private val testLoanPresentaion =
        LoanPresentaion(
            amount = 10000F,
            date = "",
            time = "",
            firstName = "John",
            lastName = "Johnson",
            id = 1L,
            percent = "10.5%",
            period = 30,
            phoneNumber = "(123) 456-7890",
            state = LoanState.REGISTERED
        )

    @Test
    fun `WHEN getLoanData() EXPECT state == LOADED`() {
        runTest {
            val viewModel = LoanDetailsFragmentViewModel(
                getLoanDataUsecase,
                converter
            )
            val id = 47L

            coEvery { getLoanDataUsecase(id) } returns testLoan
            every { converter.convertToLoanPresentation(testLoan) } returns testLoanPresentaion

            viewModel.getLoanData(id)
            coroutinesTestRule.testCoroutineScheduler.runCurrent()

            val actual = viewModel.state.getOrAwaitValue()
            val expected = FragmentState.LOADED
            MatcherAssert.assertThat(actual, `is`(expected))
        }
    }

    @Test
    fun `WHEN getLoanData() and Loan is approved EXPECT isApproved == true`() {
        runTest {
            val viewModel = LoanDetailsFragmentViewModel(
                getLoanDataUsecase,
                converter
            )
            val id = 47L
            val testApprovedLoan = mockkClass(Loan::class)
            every { testApprovedLoan.state } returns LoanState.APPROVED
            coEvery { getLoanDataUsecase(id) } returns testApprovedLoan
            every { converter.convertToLoanPresentation(testApprovedLoan) } returns testLoanPresentaion

            viewModel.getLoanData(id)
            coroutinesTestRule.testCoroutineScheduler.runCurrent()

            val actual = viewModel.isApproved.getOrAwaitValue()
            val expected = true
            MatcherAssert.assertThat(actual, `is`(expected))
        }
    }

    @Test
    fun `WHEN getLoanData() EXPECT testLoanPresentation`() {
        runTest {
            val viewModel = LoanDetailsFragmentViewModel(
                getLoanDataUsecase,
                converter
            )
            val id = 47L

            coEvery { getLoanDataUsecase(id) } returns testLoan
            every { converter.convertToLoanPresentation(testLoan) } returns testLoanPresentaion

            viewModel.getLoanData(id)
            coroutinesTestRule.testCoroutineScheduler.runCurrent()

            val actual = viewModel.loanPresentation.getOrAwaitValue()
            val expected = testLoanPresentaion
            MatcherAssert.assertThat(actual, `is`(expected))
        }
    }

    @Test
    fun `WHEN getLoanDataUsecase(id) throw UnknownHostException EXPECT state == UNKNOWN_HOST`() {
        runTest {
            val actual = stateWhenGetLoanDataUsecaseThrowException(UnknownHostException())
            val expected = FragmentState.UNKNOWN_HOST
            MatcherAssert.assertThat(actual, `is`(expected))
        }
    }

    @Test
    fun `WHEN getLoanDataUsecase(id) throw SocketTimeoutException EXPECT state == TIMEOUT_EXCEPTION`() {
        runTest {
            val actual = stateWhenGetLoanDataUsecaseThrowException(SocketTimeoutException())
            val expected = FragmentState.TIMEOUT
            MatcherAssert.assertThat(actual, `is`(expected))
        }
    }

    @Test
    fun `WHEN getLoanDataUsecase(id) throw Throwable() EXPECT state == UNKNOWN_ERROR`() {
        runTest {
            val actual = stateWhenGetLoanDataUsecaseThrowException(Throwable())
            val expected = FragmentState.UNKNOWN_ERROR
            MatcherAssert.assertThat(actual, `is`(expected))
        }
    }

    private fun stateWhenGetLoanDataUsecaseThrowException(exception: Throwable): FragmentState {
        val viewModel = LoanDetailsFragmentViewModel(
            getLoanDataUsecase,
            converter
        )
        val id = 47L

        coEvery { getLoanDataUsecase(id) } throws exception
        every { converter.convertToLoanPresentation(testLoan) } returns testLoanPresentaion

        viewModel.getLoanData(id)
        coroutinesTestRule.testCoroutineScheduler.runCurrent()

        return viewModel.state.getOrAwaitValue()
    }
}