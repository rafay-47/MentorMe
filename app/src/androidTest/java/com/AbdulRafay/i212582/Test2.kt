package com.AbdulRafay.i212582

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class Test2 {

    @get:Rule
    var rule = ActivityScenarioRule(login::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }
    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun TestNavigation() {
        onView(withId(R.id.button2)).perform(click())
        Intents.intended(hasComponent(MainMenu::class.java.name))
    }
}