package com.test.a2021_q4_tyukavkin.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.test.a2021_q4_tyukavkin.domain.entity.InputDataError
import com.test.a2021_q4_tyukavkin.domain.entity.Loan
import com.test.a2021_q4_tyukavkin.domain.entity.LoanConditions
import com.test.a2021_q4_tyukavkin.domain.entity.LoanRegistrationInputData
import com.test.a2021_q4_tyukavkin.domain.usecase.CreateLoanUsecase
import com.test.a2021_q4_tyukavkin.domain.usecase.GetLoanConditionsUsecase
import com.test.a2021_q4_tyukavkin.domain.usecase.GetLoanRegistrationInputDataErrorsUsecase
import com.test.a2021_q4_tyukavkin.presentation.converter.Converter
import com.test.a2021_q4_tyukavkin.presentation.model.LoanPresentaion
import com.test.a2021_q4_tyukavkin.presentation.state.FragmentState
import com.test.a2021_q4_tyukavkin.presentation.state.LoanRegistrationFragmentState
import com.test.a2021_q4_tyukavkin.util.CoroutinesTestRule
import com.test.a2021_q4_tyukavkin.util.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkClass
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@ExperimentalCoroutinesApi
class LoanRegistrationViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val getLoanConditionsUsecase = mockkClass(GetLoanConditionsUsecase::class)
    private val createLoanUsecase = mockkClass(CreateLoanUsecase::class)
    private val getLoanRegistrationInputDataErrorsUsecase =
        mockkClass(GetLoanRegistrationInputDataErrorsUsecase::class)
    private val converter = mockkClass(Converter::class)

    @Test
    fun `WHEN init EXPECT getLoanConditions()`() {
        coEvery { getLoanConditionsUsecase() } returns mockkClass(LoanConditions::class)

        LoanRegistrationViewModel(
            getLoanConditionsUsecase,
            createLoanUsecase,
            getLoanRegistrationInputDataErrorsUsecase,
            converter
        )
        coroutinesTestRule.testCoroutineScheduler.runCurrent()

        coVerify { getLoanConditionsUsecase() }
    }

    @Test
    fun `WHEN getLoanConditions() EXPECT conditionsState == LOADED`() {
        coEvery { getLoanConditionsUsecase() } returns mockkClass(LoanConditions::class)

        val viewModel = LoanRegistrationViewModel(
            getLoanConditionsUsecase,
            createLoanUsecase,
            getLoanRegistrationInputDataErrorsUsecase,
            converter
        )
        viewModel.getLoanConditions() //Second time after init
        coroutinesTestRule.testCoroutineScheduler.runCurrent()

        val actual = viewModel.conditionsState.getOrAwaitValue()
        val expected = FragmentState.LOADED
        assertThat(actual, `is`(expected))
    }

    @Test
    fun `WHEN getLoanConditions() EXPECT loanConditions == mockLoanConditions`() {
        val mockLoanConditions = mockkClass(LoanConditions::class)
        coEvery { getLoanConditionsUsecase() } returns mockLoanConditions

        val viewModel = LoanRegistrationViewModel(
            getLoanConditionsUsecase,
            createLoanUsecase,
            getLoanRegistrationInputDataErrorsUsecase,
            converter
        )
        viewModel.getLoanConditions() //Second time after init
        coroutinesTestRule.testCoroutineScheduler.runCurrent()

        val actual = viewModel.loanConditions.getOrAwaitValue()
        val expected = mockLoanConditions
        assertThat(actual, `is`(expected))
    }

    @Test
    fun `WHEN getLoanConditionsUsecase() throw UnknownHostException() EXPECT conditionsState == UNKNOWN_HOST`() {
        val actual = stateWhenGetLoanConditionsUsecaseThrowException(UnknownHostException())
        val expected = FragmentState.UNKNOWN_HOST
        assertThat(actual, `is`(expected))
    }

    @Test
    fun `WHEN getLoanConditionsUsecase() throw SocketTimeoutException() EXPECT conditionsState == TIMEOUT`() {
        val actual = stateWhenGetLoanConditionsUsecaseThrowException(SocketTimeoutException())
        val expected = FragmentState.TIMEOUT
        assertThat(actual, `is`(expected))
    }

    @Test
    fun `WHEN getLoanConditionsUsecase() throw Throwable() EXPECT conditionsState == UNKNOWN_ERROR`() {
        val actual = stateWhenGetLoanConditionsUsecaseThrowException(Throwable())
        val expected = FragmentState.UNKNOWN_ERROR
        assertThat(actual, `is`(expected))
    }

    @Test
    fun `WHEN checkInputData(mockInputData) EXPECT inputDataError == last inputMockDataError`() {
        val mockLoanConditions = mockkClass(LoanConditions::class)
        coEvery { getLoanConditionsUsecase() } returns mockLoanConditions

        val mockInputData = mockkClass(LoanRegistrationInputData::class)
        val mockInputDataError = mockkClass(InputDataError::class)
        val mockInputDataErrors = listOf(mockInputDataError)

        coEvery {
            getLoanRegistrationInputDataErrorsUsecase(mockInputData, mockLoanConditions)
        } returns mockInputDataErrors

        val viewModel = LoanRegistrationViewModel(
            getLoanConditionsUsecase,
            createLoanUsecase,
            getLoanRegistrationInputDataErrorsUsecase,
            converter
        )
        coroutinesTestRule.testCoroutineScheduler.runCurrent()
        viewModel.checkInputData(mockInputData)
        coroutinesTestRule.testCoroutineScheduler.runCurrent()

        val actual = viewModel.inputDataError.getOrAwaitValue()
        val expected = mockInputDataError
        assertThat(actual, `is`(expected))
    }

    @Test
    fun `WHEN registerLoan(mockIncorrectInputData) EXPECT loanRegistrationState == INCORRECT_INPUT_DATA`() {
        val mockLoanConditions = mockkClass(LoanConditions::class)
        coEvery { getLoanConditionsUsecase() } returns mockLoanConditions

        val mockIncorrectInputData = mockkClass(LoanRegistrationInputData::class)
        val mockInputDataError = mockkClass(InputDataError::class)
        val mockInputDataErrors = listOf(mockInputDataError)

        coEvery {
            getLoanRegistrationInputDataErrorsUsecase(mockIncorrectInputData, mockLoanConditions)
        } returns mockInputDataErrors

        val viewModel = LoanRegistrationViewModel(
            getLoanConditionsUsecase,
            createLoanUsecase,
            getLoanRegistrationInputDataErrorsUsecase,
            converter
        )
        coroutinesTestRule.testCoroutineScheduler.runCurrent()
        viewModel.registerLoan(mockIncorrectInputData)
        coroutinesTestRule.testCoroutineScheduler.runCurrent()

        val actual = viewModel.loanRegistrationState.getOrAwaitValue()
        val expected = LoanRegistrationFragmentState.INCORRECT_INPUT_DATA
        assertThat(actual, `is`(expected))
    }

    @Test
    fun `WHEN registerLoan(stubCorrectInputData) EXPECT loanRegistrationState == LOADED`() {
        val mockLoanConditions = mockkClass(LoanConditions::class)
        coEvery { getLoanConditionsUsecase() } returns mockLoanConditions

        val stubCorrectInputData = LoanRegistrationInputData(
            amount = "10000",
            firstName = "John",
            lastName = "Johnson",
            phoneNumber = "(123) 456-7890"
        )
        val mockInputDataErrors = emptyList<InputDataError>() //No errors
        //val mockInputDataErrors = listOf(mockkClass(InputDataError::class)) //With error

        coEvery {
            getLoanRegistrationInputDataErrorsUsecase(stubCorrectInputData, mockLoanConditions)
        } returns mockInputDataErrors

        val viewModel = LoanRegistrationViewModel(
            getLoanConditionsUsecase,
            createLoanUsecase,
            getLoanRegistrationInputDataErrorsUsecase,
            converter
        )
        coroutinesTestRule.testCoroutineScheduler.runCurrent()

        val mockLoan = mockkClass(Loan::class)
        every { mockLoanConditions.percent } returns 10.2
        every { mockLoanConditions.period } returns 47
        coEvery { createLoanUsecase(any()) } returns mockLoan

        every {
            converter.convertToLoanPresentation(mockLoan)
        } returns mockkClass(LoanPresentaion::class)

        viewModel.registerLoan(stubCorrectInputData)
        coroutinesTestRule.testCoroutineScheduler.runCurrent()

        val actual = viewModel.loanRegistrationState.getOrAwaitValue()
        val expected = LoanRegistrationFragmentState.LOADED
        assertThat(actual, `is`(expected))
    }

    @Test
    fun `WHEN createLoanUsecase() throw UnknownHostException() EXPECT conditionsState == UNKNOWN_HOST`() {
        val actual = stateWhenCreateLoanUsecaseThrowException(UnknownHostException())
        val expected = LoanRegistrationFragmentState.UNKNOWN_HOST
        assertThat(actual, `is`(expected))
    }

    @Test
    fun `WHEN createLoanUsecase() throw SocketTimeoutException() EXPECT conditionsState == TIMEOUT`() {
        val actual = stateWhenCreateLoanUsecaseThrowException(SocketTimeoutException())
        val expected = LoanRegistrationFragmentState.TIMEOUT
        assertThat(actual, `is`(expected))
    }

    @Test
    fun `WHEN getLoanRegistrationInputDataErrorsUsecase() throw NumberFormatException() EXPECT conditionsState == INCRORRECT_INPUT_DATA`() {
        val mockLoanConditions = mockkClass(LoanConditions::class)
        coEvery { getLoanConditionsUsecase() } returns mockLoanConditions

        val stubCorrectInputData = LoanRegistrationInputData(
            amount = "10000",
            firstName = "John",
            lastName = "Johnson",
            phoneNumber = "(123) 456-7890"
        )

        coEvery {
            getLoanRegistrationInputDataErrorsUsecase(stubCorrectInputData, mockLoanConditions)
        } throws NumberFormatException()

        val viewModel = LoanRegistrationViewModel(
            getLoanConditionsUsecase,
            createLoanUsecase,
            getLoanRegistrationInputDataErrorsUsecase,
            converter
        )
        coroutinesTestRule.testCoroutineScheduler.runCurrent()

        val mockLoan = mockkClass(Loan::class)
        every { mockLoanConditions.percent } returns 10.2
        every { mockLoanConditions.period } returns 47
        coEvery { createLoanUsecase(any()) } returns mockLoan

        every {
            converter.convertToLoanPresentation(mockLoan)
        } returns mockkClass(LoanPresentaion::class)

        viewModel.registerLoan(stubCorrectInputData)
        coroutinesTestRule.testCoroutineScheduler.runCurrent()

        val actual = viewModel.loanRegistrationState.getOrAwaitValue()
        val expected = LoanRegistrationFragmentState.INCORRECT_INPUT_DATA
        assertThat(actual, `is`(expected))
    }

    @Test
    fun `WHEN createLoanUsecase() throw Throwable() EXPECT conditionsState == UNKNOWN_ERROR`() {
        val actual = stateWhenCreateLoanUsecaseThrowException(Throwable())
        val expected = LoanRegistrationFragmentState.UNKNOWN_ERROR
        assertThat(actual, `is`(expected))
    }

    private fun stateWhenGetLoanConditionsUsecaseThrowException(exception: Throwable): FragmentState {
        val mockLoanConditions = mockkClass(LoanConditions::class)
        coEvery { getLoanConditionsUsecase() } returns mockLoanConditions
        val viewModel = LoanRegistrationViewModel(
            getLoanConditionsUsecase,
            createLoanUsecase,
            getLoanRegistrationInputDataErrorsUsecase,
            converter
        )
        coroutinesTestRule.testCoroutineScheduler.runCurrent()

        coEvery { getLoanConditionsUsecase() } throws exception
        viewModel.getLoanConditions() //Second time after init
        coroutinesTestRule.testCoroutineScheduler.runCurrent()

        return viewModel.conditionsState.getOrAwaitValue()
    }

    private fun stateWhenCreateLoanUsecaseThrowException(exception: Throwable): LoanRegistrationFragmentState {
        val mockLoanConditions = mockkClass(LoanConditions::class)
        coEvery { getLoanConditionsUsecase() } returns mockLoanConditions

        val stubCorrectInputData = LoanRegistrationInputData(
            amount = "10000",
            firstName = "John",
            lastName = "Johnson",
            phoneNumber = "(123) 456-7890"
        )
        val mockInputDataErrors = emptyList<InputDataError>() //No errors

        coEvery {
            getLoanRegistrationInputDataErrorsUsecase(stubCorrectInputData, mockLoanConditions)
        } returns mockInputDataErrors

        val viewModel = LoanRegistrationViewModel(
            getLoanConditionsUsecase,
            createLoanUsecase,
            getLoanRegistrationInputDataErrorsUsecase,
            converter
        )
        coroutinesTestRule.testCoroutineScheduler.runCurrent()

        val mockLoan = mockkClass(Loan::class)
        every { mockLoanConditions.percent } returns 10.2
        every { mockLoanConditions.period } returns 47
        coEvery { createLoanUsecase(any()) } throws exception

        every {
            converter.convertToLoanPresentation(mockLoan)
        } returns mockkClass(LoanPresentaion::class)

        viewModel.registerLoan(stubCorrectInputData)
        coroutinesTestRule.testCoroutineScheduler.runCurrent()

        return viewModel.loanRegistrationState.getOrAwaitValue()
    }
}