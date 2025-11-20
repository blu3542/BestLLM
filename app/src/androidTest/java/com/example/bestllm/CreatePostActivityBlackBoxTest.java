package com.example.bestllm;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.bestllm.ui.post.CreatePostActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Black-box tests for CreatePostActivity.
 *
 * Feature coverage:
 *  - Required title field
 *  - Required body field
 *  - Loading state when submitting a valid post
 */
@RunWith(AndroidJUnit4.class)
public class CreatePostActivityBlackBoxTest {

    /**
     * Black-box Test Case 1:
     * Empty title should show "Title is required" error on the title field.
     */
    @Test
    public void testCreatePostWithEmptyTitleShowsErrorOnTitleField() {
        Intent intent = new Intent(
                InstrumentationRegistry.getInstrumentation().getTargetContext(),
                CreatePostActivity.class
        );

        try (ActivityScenario<CreatePostActivity> scenario = ActivityScenario.launch(intent)) {
            // Leave title empty, fill body and tags
            Espresso.onView(ViewMatchers.withId(R.id.editTextBody))
                    .perform(ViewActions.replaceText("Body content"), ViewActions.closeSoftKeyboard());

            Espresso.onView(ViewMatchers.withId(R.id.editTextTags))
                    .perform(ViewActions.replaceText("gpt-4"), ViewActions.closeSoftKeyboard());

            Espresso.onView(ViewMatchers.withId(R.id.buttonCreatePost))
                    .perform(ViewActions.click());

            Espresso.onView(ViewMatchers.withId(R.id.editTextTitle))
                    .check(ViewAssertions.matches(
                            ViewMatchers.hasErrorText("Title is required")));
        }
    }

    /**
     * Black-box Test Case 2:
     * Empty body should show "Body is required" error on the body field.
     */
    @Test
    public void testCreatePostWithEmptyBodyShowsErrorOnBodyField() {
        Intent intent = new Intent(
                InstrumentationRegistry.getInstrumentation().getTargetContext(),
                CreatePostActivity.class
        );

        try (ActivityScenario<CreatePostActivity> scenario = ActivityScenario.launch(intent)) {
            Espresso.onView(ViewMatchers.withId(R.id.editTextTitle))
                    .perform(ViewActions.replaceText("My test post"), ViewActions.closeSoftKeyboard());

            // Body left empty
            Espresso.onView(ViewMatchers.withId(R.id.editTextTags))
                    .perform(ViewActions.replaceText("gpt-4"), ViewActions.closeSoftKeyboard());

            Espresso.onView(ViewMatchers.withId(R.id.buttonCreatePost))
                    .perform(ViewActions.click());

            Espresso.onView(ViewMatchers.withId(R.id.editTextBody))
                    .check(ViewAssertions.matches(
                            ViewMatchers.hasErrorText("Body is required")));
        }
    }

    /**
     * Black-box Test Case 3:
     * For a valid post, pressing Create should show loading (progress bar visible, button disabled).
     *
     * This verifies that the loading UI state toggles correctly, not the backend Firestore logic.
     */
    @Test
    public void testCreatePostWithValidFieldsTriggersLoadingState() {
        Intent intent = new Intent(
                InstrumentationRegistry.getInstrumentation().getTargetContext(),
                CreatePostActivity.class
        );

        try (ActivityScenario<CreatePostActivity> scenario = ActivityScenario.launch(intent)) {
            Espresso.onView(ViewMatchers.withId(R.id.editTextTitle))
                    .perform(ViewActions.replaceText("Valid title"), ViewActions.closeSoftKeyboard());

            Espresso.onView(ViewMatchers.withId(R.id.editTextBody))
                    .perform(ViewActions.replaceText("Some detailed body text"), ViewActions.closeSoftKeyboard());

            Espresso.onView(ViewMatchers.withId(R.id.editTextTags))
                    .perform(ViewActions.replaceText("gpt-4, openai"), ViewActions.closeSoftKeyboard());

            Espresso.onView(ViewMatchers.withId(R.id.buttonCreatePost))
                    .perform(ViewActions.click());

            // Immediately after click, progress bar should be visible
            Espresso.onView(ViewMatchers.withId(R.id.progressBar))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

            // And the button should be disabled
            Espresso.onView(ViewMatchers.withId(R.id.buttonCreatePost))
                    .check(ViewAssertions.matches(ViewMatchers.isNotEnabled()));
        }
    }
}
