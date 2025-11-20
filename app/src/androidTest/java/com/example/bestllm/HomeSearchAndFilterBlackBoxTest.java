package com.example.bestllm;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.bestllm.ui.home.HomeActivity;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.instanceOf;

/**
 * Black-box tests focused on search mode selection in HomeActivity.
 *
 * Feature coverage:
 *  - Switching search mode to Tag
 *  - Switching search mode to Author
 */
@RunWith(AndroidJUnit4.class)
public class HomeSearchAndFilterBlackBoxTest {

    /**
     * Black-box Test Case 1:
     * Spinner can switch to "Tag" mode and still keep search UI responsive.
     */
    @Test
    public void testSearchModeSpinnerCanSwitchToTag() {
        Intent intent = new Intent(
                InstrumentationRegistry.getInstrumentation().getTargetContext(),
                HomeActivity.class
        );

        try (ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(intent)) {
            // Open spinner
            Espresso.onView(ViewMatchers.withId(R.id.spinnerSearchMode))
                    .perform(ViewActions.click());

            onData(allOf(is(instanceOf(String.class)), is("Tag")))
                    .inRoot(RootMatchers.isPlatformPopup())
                    .perform(ViewActions.click());

            Espresso.onView(ViewMatchers.withId(R.id.spinnerSearchMode))
                    .check(ViewAssertions.matches(withSpinnerText(Matchers.containsString("Tag"))));

            // Type a tag-based query and ensure search field works
            Espresso.onView(ViewMatchers.withId(R.id.editTextSearch))
                    .perform(ViewActions.replaceText("gpt-4"), ViewActions.closeSoftKeyboard());

            // RecyclerView should still be visible (results may or may not be empty depending on data)
            Espresso.onView(ViewMatchers.withId(R.id.recyclerViewPosts))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        }
    }

    /**
     * Black-box Test Case 2:
     * Spinner can switch to "Author" mode.
     */
    @Test
    public void testSearchModeSpinnerCanSwitchToAuthor() {
        Intent intent = new Intent(
                InstrumentationRegistry.getInstrumentation().getTargetContext(),
                HomeActivity.class
        );

        try (ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(intent)) {
            Espresso.onView(ViewMatchers.withId(R.id.spinnerSearchMode))
                    .perform(ViewActions.click());

            onData(allOf(is(instanceOf(String.class)), is("Author")))
                    .inRoot(RootMatchers.isPlatformPopup())
                    .perform(ViewActions.click());

            Espresso.onView(ViewMatchers.withId(R.id.spinnerSearchMode))
                    .check(ViewAssertions.matches(withSpinnerText(Matchers.containsString("Author"))));

            Espresso.onView(ViewMatchers.withId(R.id.editTextSearch))
                    .perform(ViewActions.replaceText("Test User"), ViewActions.closeSoftKeyboard());

            Espresso.onView(ViewMatchers.withId(R.id.recyclerViewPosts))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        }
    }
}
