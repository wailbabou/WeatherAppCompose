/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.views.MyApp
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class WeatherAppTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun goodMorningTest() {
        val txt = composeTestRule.activity.getString(R.string.good_morning)
        composeTestRule.onNodeWithText(txt).assertExists()
    }

    private val themeIsDark = MutableStateFlow(false)

    @ExperimentalMaterialApi
    @Before
    fun setUp() {
        composeTestRule.setContent {
            MyTheme(
                darkTheme = themeIsDark.collectAsState(false).value
            ) {
                MyApp()
            }
        }
    }

    @Test
    fun changeTheme_textDisplayed() {
        val txt = composeTestRule.activity.getString(R.string.good_morning)
        // Set theme to dark
        themeIsDark.value = true

        // Check that we're still on the same page
        composeTestRule.onNodeWithText(txt).assertIsDisplayed()
    }
}
