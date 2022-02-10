package com.test.a2021_q4_tyukavkin.ui

import androidx.test.espresso.Espresso
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.test.a2021_q4_tyukavkin.R
import com.test.a2021_q4_tyukavkin.screen.AuthorizationScreen
import com.test.a2021_q4_tyukavkin.tools.TestCase
import com.test.a2021_q4_tyukavkin.util.readStringFromFile
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthorizationScreenTest {

    @get:Rule
    var activityScenarioRule = activityScenarioRule<MainActivity>()

    private val mockWebServer = MockWebServer()

    @Before
    fun setup() {
        mockWebServer.start(8080)
        enterCredentials()
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    @TestCase("Test-1", description = "Проверить корректное отображение view")
    fun checkViewsDisplay() {
        AuthorizationScreen {
            loginEt {
                isDisplayed()
                hasHint(R.string.login)
            }
            passwordEt {
                isDisplayed()
                hasHint(R.string.password)
            }
            loginBtn {
                isEnabled()
                hasText(R.string.sign_in)
            }
            registerBtn {
                isEnabled()
                hasText(R.string.register)
            }
        }
    }

    @Test
    @TestCase("Test-2", "Проверить отображение ошибок при входе")
    fun checkLoginErrors() {
        AuthorizationScreen {
            mockWebServer.enqueue(MockResponse().setResponseCode(404))
            loginBtn {
                click()
            }
            warningTv {
                hasText(R.string.invalid_credentials_msg)
            }

            mockWebServer.enqueue(MockResponse().setResponseCode(500))
            loginBtn {
                click()
            }
            warningTv {
                hasText(R.string.server_unavailable)
            }

            mockWebServer.enqueue(MockResponse().setResponseCode(-1))
            loginBtn {
                click()
            }
            warningTv {
                hasText(R.string.unknown_error_msg)
            }
        }
    }

    @Test
    @TestCase("Test-3", "Проверить отображение ошибок при регистрации")
    fun checkRegistrationErrors() {
        AuthorizationScreen {
            mockWebServer.enqueue(MockResponse().setResponseCode(500))
            registerBtn {
                click()
            }
            warningTv {
                hasText(R.string.server_unavailable)
            }

            mockWebServer.enqueue(MockResponse().setResponseCode(-1))
            registerBtn {
                click()
            }
            warningTv {
                hasText(R.string.unknown_error_msg)
            }
        }
    }

    @Test
    @TestCase("Test-4", "Проверить успешную регистрацию")
    fun checkSuccessfulRegistration() {
        AuthorizationScreen {
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody(readStringFromFile("TestUser.json"))
            )
            registerBtn {
                click()
            }
            warningTv {
                hasEmptyText()
            }
        }
    }

    private fun enterCredentials() {
        AuthorizationScreen {
            loginEt {
                typeText("TestUser")
            }
            Espresso.closeSoftKeyboard()
            passwordEt {
                typeText("pass123")
            }
            Espresso.closeSoftKeyboard()
        }
    }

}