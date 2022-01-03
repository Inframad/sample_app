package com.test.a2021_q4_tyukavkin.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.test.a2021_q4_tyukavkin.domain.repository.Repository
import com.test.a2021_q4_tyukavkin.domain.usecase.CheckAuthorizationUsecase
import com.test.a2021_q4_tyukavkin.domain.usecase.LogoutUsecase
import com.test.a2021_q4_tyukavkin.util.CoroutinesTestRule
import com.test.a2021_q4_tyukavkin.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
class MainActivityViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private lateinit var repository: Repository
    private lateinit var checkAuthorizationUsecase: CheckAuthorizationUsecase
    private lateinit var logoutUsecase: LogoutUsecase

    @Before
    fun setup() {
        runTest {
            repository = mock(Repository::class.java)
            checkAuthorizationUsecase = CheckAuthorizationUsecase(repository)
            logoutUsecase = LogoutUsecase(repository)
        }
    }

    @Test
    fun `WHEN init EXPECT isAuthorized == true`() {
        runTest {
            Mockito.`when`(repository.checkAuthorization()).thenReturn(true)
            val viewModel = MainActivityViewModel(
                checkAuthorizationUsecase,
                logoutUsecase
            )
            coroutinesTestRule.testCoroutineScheduler.runCurrent()

            val actual = viewModel.isAuthorized.getOrAwaitValue()
            val expected = true
            assertThat(actual, `is`(expected))
        }
    }

    @Test
    fun `WHEN logout() EXPECT repository logout() invoked`() {
        runTest {
            val viewModel = MainActivityViewModel(
                checkAuthorizationUsecase,
                logoutUsecase
            )
            viewModel.logout()
            coroutinesTestRule.testCoroutineScheduler.runCurrent()

            verify(repository).logout()
        }
    }

}