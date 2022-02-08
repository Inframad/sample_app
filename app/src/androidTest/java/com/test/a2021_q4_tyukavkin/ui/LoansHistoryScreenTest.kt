package com.test.a2021_q4_tyukavkin.ui

import androidx.test.espresso.Espresso
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.test.a2021_q4_tyukavkin.R
import com.test.a2021_q4_tyukavkin.screen.AuthorizationScreen
import com.test.a2021_q4_tyukavkin.screen.LoansHistoryScreen
import com.test.a2021_q4_tyukavkin.tools.TestCase
import com.test.a2021_q4_tyukavkin.util.readStringFromFile
import com.test.a2021_q4_tyukavkin.data.model.AuthToken
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoansHistoryScreenTest {

    @get:Rule
    var activityScenarioRule = activityScenarioRule<MainActivity>()

    private val mockWebServer = MockWebServer()

    @Before
    fun setup() {
        mockWebServer.start(8080)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    @TestCase("Test-1", "Проверить отображение 3 элементов списка")
    fun checkItemsDisplayed() {
        moveToLoansHistoryScreen {
            mockWebServer.enqueue(MockResponse().setBody(readStringFromFile("TestLoans.json")))
        }
        checkLoanItems(
            LoanItem(
                amount = "5000.0",
                percent = "2.0%",
                stateId = R.string.approved,
                stateColorId = R.color.positive
            ),
            LoanItem(
                amount = "10000.0",
                percent = "10.4%",
                stateId = R.string.rejected,
                stateColorId = R.color.negative
            ),
            LoanItem(
                amount = "15000.0",
                percent = "15.9%",
                stateId = R.string.registered,
                stateColorId = R.color.neutral
            )
        )
    }

    @Test
    @TestCase("Test-2", "Проверить отображение и кликабельность FAB")
    fun checkFab() {
        moveToLoansHistoryScreen {
            mockWebServer.enqueue(MockResponse().setBody(readStringFromFile("TestLoans.json")))
        }

        run {
            LoansHistoryScreen {
                fab {
                    isDisplayed()
                    isVisible()
                    isClickable()
                }
            }
        }
    }

    @Test
    @TestCase("Test-3", "Проверить отображение 3 элементов списка после обновления")
    fun checkSwipeRefreshLayout() {
        moveToLoansHistoryScreen {
            mockWebServer.enqueue(MockResponse().setBody(readStringFromFile("TestLoans.json")))
            mockWebServer.enqueue(MockResponse().setBody(readStringFromFile("TestRefreshedLoans.json")))
        }

        run {
            LoansHistoryScreen {
                swipeRefreshLayout {
                    swipeDown()
                }
            }
        }
        checkLoanItems(
            LoanItem(
                amount = "10000.0",
                percent = "4.0%",
                stateId = R.string.registered,
                stateColorId = R.color.neutral
            ),
            LoanItem(
                amount = "20000.0",
                percent = "20.8%",
                stateId = R.string.approved,
                stateColorId = R.color.positive
            ),
            LoanItem(
                amount = "30000.0",
                percent = "31.8%",
                stateId = R.string.rejected,
                stateColorId = R.color.negative
            )
        )
    }

    private fun moveToLoansHistoryScreen(enqueueResponse: () -> Unit) {
        run {
            AuthorizationScreen {
                loginEt {
                    typeText("TestUser")
                }
                Espresso.closeSoftKeyboard()
                passwordEt {
                    typeText("pass123")
                }
                Espresso.closeSoftKeyboard()

                mockWebServer.enqueue(MockResponse().setBody(readStringFromFile("TestToken.json")))
                enqueueResponse()
                loginBtn {
                    click()
                }
            }
        }
    }

    data class LoanItem(
        val amount: String,
        val percent: String,
        val stateId: Int,
        val stateColorId: Int
    )

    private fun checkLoanItems(vararg transactions: LoanItem) {
        transactions.forEachIndexed { index, loan ->
            LoansHistoryScreen {
                loansRv {
                    childAt<LoansHistoryScreen.LoanItem>(index) {
                        amountTv {
                            isDisplayed()
                            hasText(loan.amount)
                        }

                        percentTv {
                            isDisplayed()
                            hasText(loan.percent)
                        }

                        statusTv {
                            isDisplayed()
                            hasText(loan.stateId)
                            hasTextColor(loan.stateColorId)
                        }
                    }
                }
            }
        }
    }
}