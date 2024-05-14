package com.AbdulRafay.i212582

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class Test1 {

    @Test
    fun TestMessage() {
        ActivityScenario.launch(MainMenu::class.java)
        onView(withId(R.id.textView37)).check(matches(isDisplayed()))
        }
}