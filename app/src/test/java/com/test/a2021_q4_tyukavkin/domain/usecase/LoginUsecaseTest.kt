package com.test.a2021_q4_tyukavkin.domain.usecase

import com.test.a2021_q4_tyukavkin.domain.entity.Auth
import com.test.a2021_q4_tyukavkin.domain.repository.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito

class LoginUsecaseTest {

    private val testAuth =
        Auth(
            username = "User",
            password = "qwe123"
        )

    @ExperimentalCoroutinesApi
    @Test
    fun `WHEN invoke(testAuth) EXPECT login(testAuth)`() {
        runTest {
            val mockRepository = Mockito.mock(Repository::class.java)

            val loginUsecase = LoginUsecase(mockRepository)

            loginUsecase(testAuth)

            Mockito.verify(mockRepository, Mockito.times(1)).login(testAuth)
        }
    }
}