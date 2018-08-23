package com.inspiringteam.transferxcompass.compass;


import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.inspiringteam.transferxcompass.ui.compass.CompassActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test Compass Screen
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class CompassScreenTest {
    @Rule
    public ActivityTestRule<CompassActivity> mTasksActivityTestRule =
            new ActivityTestRule<CompassActivity>(CompassActivity.class);

    // TODO NOT WORKING YET.
    // Need to access fragment view to perform click on insert destination
    @Test
    public void clickAddTaskButton_opensAddTaskUi() {
        // Click on the add task button
//        onView(withId(R.id.button_enter_destination)).perform(click());

        // Check if the add task screen is displayed
//        onView(withId(R.id.edit_lat)).check(matches(isDisplayed()));
    }
}





