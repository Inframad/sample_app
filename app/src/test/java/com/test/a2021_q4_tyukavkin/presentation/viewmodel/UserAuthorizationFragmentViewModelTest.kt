package com.test.a2021_q4_tyukavkin.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.test.a2021_q4_tyukavkin.domain.entity.Auth
import com.test.a2021_q4_tyukavkin.domain.entity.RequestError
import com.test.a2021_q4_tyukavkin.domain.entity.User
import com.test.a2021_q4_tyukavkin.domain.entity.UserRole
import com.test.a2021_q4_tyukavkin.domain.usecase.LoginUsecase
import com.test.a2021_q4_tyukavkin.domain.usecase.RegistrationUsecase
import com.test.a2021_q4_tyukavkin.presentation.state.UserAuthorizationFragmentState
import com.test.a2021_q4_tyukavkin.util.CoroutinesTestRule
import com.test.a2021_q4_tyukavkin.util.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.mockkClass
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@ExperimentalCoroutinesApi
class UserAuthorizationFragmentViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private lateinit var registrationUsecase: RegistrationUsecase
    private lateinit var loginUsecase: LoginUsecase

    private val testAuth = Auth(
        username = "TestUser",
        password = "pass123"
    )
    private val testUser = User(
        name = "TestUser",
        role = UserRole.USER
    )

    @Before
    fun setup() {
        registrationUsecase = mockkClass(RegistrationUsecase::class)
        loginUsecase = mockkClass(LoginUsecase::class)
    }

    @Test
    fun `WHEN init EXPECT state == DEFAULT`() {
        runTest {
            val viewModel = UserAuthorizationFragmentViewModel(
                registrationUsecase,
                loginUsecase
            )

            val actual = viewModel.state.getOrAwaitValue()
            val expected = UserAuthorizationFragmentState.DEFAULT
            assertThat(actual, `is`(expected))
        }
    }

    @Test
    fun `WHEN register(testAuth) EXPECT TestUser`() {
        runTest {
            val viewModel = UserAuthorizationFragmentViewModel(
                registrationUsecase,
                loginUsecase
            )
            coEvery { registrationUsecase(testAuth) } returns testUser

            viewModel.register(testAuth)
            coroutinesTestRule.testCoroutineScheduler.runCurrent()

            val actual = viewModel.user.getOrAwaitValue()
            val expected = testUser
            assertThat(actual, `is`(expected))
        }
    }

    @Test
    fun `WHEN login(testAuth) EXPECT state == LOADED`() {
        runTest {
            val viewModel = UserAuthorizationFragmentViewModel(
                registrationUsecase,
                loginUsecase
            )
            coEvery { loginUsecase(testAuth) } returns Unit

            viewModel.login(testAuth)
            coroutinesTestRule.testCoroutineScheduler.runCurrent()

            val actual = viewModel.state.getOrAwaitValue()
            val expected = UserAuthorizationFragmentState.LOADED
            assertThat(actual, `is`(expected))
        }
    }

    @Test
    fun `WHEN login() throw RequestError(404) EXPECT state == INVALID_CREDENTIALS`() {
        runTest {
            val actual = stateWhenLoginThrowException(RequestError(404))
            val expected = UserAuthorizationFragmentState.INVALID_CREDENTIALS
            assertThat(actual, `is`(expected))
        }
    }

    @Test
    fun `WHEN login() throw RequestError(500) EXPECT state == SERVER_ERROR`() {
        runTest {
            val actual = stateWhenLoginThrowException(RequestError(500))
            val expected = UserAuthorizationFragmentState.SERVER_ERROR
            assertThat(actual, `is`(expected))
        }
    }

    @Test
    fun `WHEN login() throw UnknownHostException EXPECT state == NO_INTERNET_CONNECTION`() {
        runTest {
            val actual = stateWhenLoginThrowException(UnknownHostException())
            val expected = UserAuthorizationFragmentState.NO_INTERNET_CONNECTION
            assertThat(actual, `is`(expected))
        }
    }

    @Test
    fun `WHEN login() throw SocketTimeoutException EXPECT state == TIMEOUT_EXCEPTION`() {
        runTest {
            val actual = stateWhenLoginThrowException(SocketTimeoutException())
            val expected = UserAuthorizationFragmentState.TIMEOUT_EXCEPTION
            assertThat(actual, `is`(expected))
        }
    }

    @Test
    fun `WHEN login() throw Throwable() EXPECT state == UNKNOWN_ERROR`() {
        runTest {
            val actual = stateWhenLoginThrowException(Throwable())
            val expected = UserAuthorizationFragmentState.UNKNOWN_ERROR
            assertThat(actual, `is`(expected))
        }
    }

    @Test
    fun `WHEN register() throw RequestError(500) EXPECT state == SERVER_ERROR`() {
        runTest {
            val actual = stateWhenRegisterThrowException(RequestError(500))
            val expected = UserAuthorizationFragmentState.SERVER_ERROR
            assertThat(actual, `is`(expected))
        }
    }

    @Test
    fun `WHEN register() throw UnknownHostException EXPECT state == NO_INTERNET_CONNECTION`() {
        runTest {
            val actual = stateWhenRegisterThrowException(UnknownHostException())
            val expected = UserAuthorizationFragmentState.NO_INTERNET_CONNECTION
            assertThat(actual, `is`(expected))
        }
    }

    @Test
    fun `WHEN register() throw SocketTimeoutException EXPECT state == TIMEOUT_EXCEPTION`() {
        runTest {
            val actual = stateWhenRegisterThrowException(SocketTimeoutException())
            val expected = UserAuthorizationFragmentState.TIMEOUT_EXCEPTION
            assertThat(actual, `is`(expected))
        }
    }

    @Test
    fun `WHEN register() throw RequestError(400) EXPECT state == BUSY_LOGIN`() {
        runTest {
            val actual = stateWhenRegisterThrowException(RequestError(400))
            val expected = UserAuthorizationFragmentState.BUSY_LOGIN
            assertThat(actual, `is`(expected))
        }
    }

    @Test
    fun `WHEN register() throw Throwable() EXPECT state == UNKNOWN_ERROR`() {
        runTest {
            val actual = stateWhenRegisterThrowException(Throwable())
            val expected = UserAuthorizationFragmentState.UNKNOWN_ERROR
            assertThat(actual, `is`(expected))
        }
    }

    private fun stateWhenLoginThrowException(exception: Throwable): UserAuthorizationFragmentState {
            val viewModel = UserAuthorizationFragmentViewModel(
                registrationUsecase,
                loginUsecase
            )
            coEvery { loginUsecase(testAuth) } throws exception

            viewModel.login(testAuth)
            coroutinesTestRule.testCoroutineScheduler.runCurrent()

            return viewModel.state.getOrAwaitValue()
    }

    private fun stateWhenRegisterThrowException(exception: Throwable): UserAuthorizationFragmentState {
        val viewModel = UserAuthorizationFragmentViewModel(
            registrationUsecase,
            loginUsecase
        )
        coEvery { registrationUsecase(testAuth) } throws exception

        viewModel.register(testAuth)
        coroutinesTestRule.testCoroutineScheduler.runCurrent()

        return viewModel.state.getOrAwaitValue()
    }
}