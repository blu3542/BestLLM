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
import com.example.bestllm.ui.prompt.PromptListActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;

/**
 * Black-box tests for navigating to the prompt sharing screen from HomeActivity.
 *
 * Feature coverage:
 *  - Presence of "Prompt_Sharing" menu item
 *  - Successful navigation to PromptListActivity
 */
@RunWith(AndroidJUnit4.class)
public class PromptSharingNavigationBlackBoxTest {

    /**
     * Black-box Test Case 1:
     * "Prompt_Sharing" menu item is present in the overflow or action bar.
     */
    @Test
    public void testPromptSharingMenuItemVisible() {
        Intent intent = new Intent(
                InstrumentationRegistry.getInstrumentation().getTargetContext(),
                HomeActivity.class
        );

        try (ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(intent)) {
            // Some devices put it in the overflow menu
            openActionBarOverflowOrOptionsMenu(
                    InstrumentationRegistry.getInstrumentation().getTargetContext());

            Espresso.onView(ViewMatchers.withText("Prompt_Sharing"))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        }
    }

    /**
     * Black-box Test Case 2:
     * Selecting "Prompt_Sharing" opens PromptListActivity.
     */
    @Test
    public void testPromptSharingMenuOpensPromptListActivity() {
        Intent intent = new Intent(
                InstrumentationRegistry.getInstrumentation().getTargetContext(),
                HomeActivity.class
        );

        try (ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(intent)) {
            openActionBarOverflowOrOptionsMenu(
                    InstrumentationRegistry.getInstrumentation().getTargetContext());

            Espresso.onView(ViewMatchers.withText("Prompt_Sharing"))
                    .perform(ViewActions.click());

            // Now check that the prompt list UI is displayed
            try (ActivityScenario<PromptListActivity> promptScenario =
                         ActivityScenario.launch(PromptListActivity.class)) {

                Espresso.onView(ViewMatchers.withId(R.id.rvPrompts))
                        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
            }
        }
    }
}
