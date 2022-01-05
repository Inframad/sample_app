package com.test.a2021_q4_tyukavkin.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.test.a2021_q4_tyukavkin.domain.entity.Loan
import com.test.a2021_q4_tyukavkin.domain.entity.LoanState
import com.test.a2021_q4_tyukavkin.domain.usecase.GetAllLoansUsecase
import com.test.a2021_q4_tyukavkin.domain.usecase.UpdateLoansListUsecase
import com.test.a2021_q4_tyukavkin.presentation.converter.Converter
import com.test.a2021_q4_tyukavkin.presentation.model.LoanPresentaion
import com.test.a2021_q4_tyukavkin.presentation.state.FragmentState
import com.test.a2021_q4_tyukavkin.util.CoroutinesTestRule
import com.test.a2021_q4_tyukavkin.util.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockkClass
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.time.OffsetDateTime

@ExperimentalCoroutinesApi
class LoanHistoryFragmentViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private val getLoanListUsecase = mockkClass(GetAllLoansUsecase::class)
    private val updateLoansListUsecase = mockkClass(UpdateLoansListUsecase::class)
    private val converter = mockkClass(Converter::class)

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
    fun `WHEN updateLoanList() EXPECT state == LOADED`() {
        runTest {
            val viewModel = LoanHistoryFragmentViewModel(
                getLoanListUsecase,
                updateLoansListUsecase,
                converter
            )

            coEvery { updateLoansListUsecase() } returns true
            viewModel.updateLoans()
            coroutinesTestRule.testCoroutineScheduler.runCurrent()

            val actual = viewModel.state.getOrAwaitValue()
            val expected = FragmentState.LOADED
            assertThat(actual, `is`(expected))
        }
    }

    @Test
    fun `WHEN updateLoanList() EXPECT isLoansEmpty == false`() {
        runTest {
            val viewModel = LoanHistoryFragmentViewModel(
                getLoanListUsecase,
                updateLoansListUsecase,
                converter
            )

            coEvery { updateLoansListUsecase() } returns false
            viewModel.updateLoans()
            coroutinesTestRule.testCoroutineScheduler.runCurrent()

            val actual = viewModel.isLoansEmpty.getOrAwaitValue()
            val expected = false
            assertThat(actual, `is`(expected))
        }
    }

    @Test
    fun `WHEN getLoans() EXPECT flow of mockLoanPresentationList`() {
        runTest {
            val viewModel = LoanHistoryFragmentViewModel(
                getLoanListUsecase,
                updateLoansListUsecase,
                converter
            )
            val mockLoanList = listOf(testLoan)
            val fakeLoanListSource = flow {
                emit(mockLoanList)
            }
            val mockLoanPresentationList = listOf(testLoanPresentaion)
            val fakeLoanPresentationListSource = flow {
                emit(mockLoanPresentationList)
            }

            every { converter.convertToLoanPresentation(testLoan) } returns testLoanPresentaion
            coEvery { getLoanListUsecase() } returns fakeLoanListSource

            val actual = viewModel.getLoans().first()
            coroutinesTestRule.testCoroutineScheduler.runCurrent()
            val expected = fakeLoanPresentationListSource.first()
            assertThat(actual, `is`(expected))
        }
    }

    @Test
    fun `WHEN updateLoanList() throw SocketTimeoutException EXPECT state == TIMEOUT`() {
        runTest {
            val actual = stateWhenUpdateLoansListThrowException(SocketTimeoutException())
            val expected = FragmentState.TIMEOUT
            assertThat(actual, `is`(expected))
        }
    }

    @Test
    fun `WHEN updateLoanList() throw UnknownHostException EXPECT state == UKNOWN_HOST`() {
        runTest {
            val actual = stateWhenUpdateLoansListThrowException(UnknownHostException())
            val expected = FragmentState.UNKNOWN_HOST
            assertThat(actual, `is`(expected))
        }
    }

    @Test
    fun `WHEN updateLoanList() throw Throwable() EXPECT state == UNKNOWN_ERROR`() {
        runTest {
            val actual = stateWhenUpdateLoansListThrowException(Throwable())
            val expected = FragmentState.UNKNOWN_ERROR
            assertThat(actual, `is`(expected))
        }
    }

    private fun stateWhenUpdateLoansListThrowException(exception: Throwable): FragmentState {
        val viewModel = LoanHistoryFragmentViewModel(
            getLoanListUsecase,
            updateLoansListUsecase,
            converter
        )

        coEvery { updateLoansListUsecase() } throws exception

        viewModel.updateLoans()
        coroutinesTestRule.testCoroutineScheduler.runCurrent()

        return viewModel.state.getOrAwaitValue()
    }
}