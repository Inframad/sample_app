package com.test.a2021_q4_tyukavkin.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class CoroutinesTestRule (
    val testCoroutineScheduler: TestCoroutineScheduler = TestCoroutineScheduler(),
    val testDispatcher: TestDispatcher = StandardTestDispatcher(testCoroutineScheduler)
) : TestWatcher() {

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}