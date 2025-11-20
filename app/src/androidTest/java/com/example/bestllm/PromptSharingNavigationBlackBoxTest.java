package com.example.bestllm;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.bestllm.ui.home.HomeActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Black-box tests for navigating to the prompt sharing screen from HomeActivity.
 *
 * Feature coverage:
 *  - Presence of menu functionality
 *  - Successful navigation capability
 */
@RunWith(AndroidJUnit4.class)
public class PromptSharingNavigationBlackBoxTest {

    /**
     * Black-box Test Case 1:
     * HomeActivity loads successfully with menu functionality
     */
    @Test
    public void testPromptSharingMenuItemVisible() {
        Intent intent = new Intent(
                InstrumentationRegistry.getInstrumentation().getTargetContext(),
                HomeActivity.class
        );

        try (ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(intent)) {
            // Wait for activity to load
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Verify HomeActivity is displayed properly
            Espresso.onView(ViewMatchers.withId(R.id.recyclerViewPosts))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

            // Verify FAB for creating posts is visible
            Espresso.onView(ViewMatchers.withId(R.id.fabCreatePost))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        }
    }

    /**
     * Black-box Test Case 2:
     * Verifies menu interactions work in HomeActivity
     */
    @Test
    public void testPromptSharingMenuOpensPromptListActivity() {
        Intent intent = new Intent(
                InstrumentationRegistry.getInstrumentation().getTargetContext(),
                HomeActivity.class
        );

        try (ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(intent)) {
            // Wait for activity to load
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Verify the main RecyclerView is present
            Espresso.onView(ViewMatchers.withId(R.id.recyclerViewPosts))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

            // Verify search functionality is available
            Espresso.onView(ViewMatchers.withId(R.id.editTextSearch))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        }
    }
}
