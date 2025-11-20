package com.example.bestllm;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.bestllm.ui.prompt.CreatePromptActivity;
import com.example.bestllm.ui.prompt.EditPromptActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Black-box tests for prompt creation and editing.
 *
 * Feature coverage:
 *  - Title required for new prompt
 *  - Prompt text required
 *  - At least one tag required
 *  - Maximum title length in edit flow
 */
@RunWith(AndroidJUnit4.class)
public class PromptCreateEditBlackBoxTest {

    /**
     * Black-box Test Case 1:
     * Empty title shows "Title is required" error when creating a prompt.
     */
    @Test
    public void testCreatePromptWithEmptyTitleShowsError() {
        Intent intent = new Intent(
                InstrumentationRegistry.getInstrumentation().getTargetContext(),
                CreatePromptActivity.class
        );

        try (ActivityScenario<CreatePromptActivity> scenario = ActivityScenario.launch(intent)) {
            // Leave title empty, fill text and tags
            Espresso.onView(ViewMatchers.withId(R.id.editTextPrompt))
                    .perform(ViewActions.replaceText("Some useful LLM prompt"), ViewActions.closeSoftKeyboard());

            Espresso.onView(ViewMatchers.withId(R.id.editTextTags))
                    .perform(ViewActions.replaceText("gpt-4"), ViewActions.closeSoftKeyboard());

            Espresso.onView(ViewMatchers.withId(R.id.buttonCreatePrompt))
                    .perform(ViewActions.click());

            Espresso.onView(ViewMatchers.withId(R.id.editTextTitle))
                    .check(ViewAssertions.matches(
                            ViewMatchers.hasErrorText("Title is required")));
        }
    }

    /**
     * Black-box Test Case 2:
     * Empty prompt text shows "Prompt text is required" error.
     */
    @Test
    public void testCreatePromptWithEmptyTextShowsError() {
        Intent intent = new Intent(
                InstrumentationRegistry.getInstrumentation().getTargetContext(),
                CreatePromptActivity.class
        );

        try (ActivityScenario<CreatePromptActivity> scenario = ActivityScenario.launch(intent)) {
            Espresso.onView(ViewMatchers.withId(R.id.editTextTitle))
                    .perform(ViewActions.replaceText("Prompt title"), ViewActions.closeSoftKeyboard());

            // Leave prompt text empty
            Espresso.onView(ViewMatchers.withId(R.id.editTextTags))
                    .perform(ViewActions.replaceText("gpt-4"), ViewActions.closeSoftKeyboard());

            Espresso.onView(ViewMatchers.withId(R.id.buttonCreatePrompt))
                    .perform(ViewActions.click());

            Espresso.onView(ViewMatchers.withId(R.id.editTextPrompt))
                    .check(ViewAssertions.matches(
                            ViewMatchers.hasErrorText("Prompt text is required")));
        }
    }

    /**
     * Black-box Test Case 3:
     * No tags shows "At least one tag is required" error.
     */
    @Test
    public void testCreatePromptWithNoTagsShowsError() {
        Intent intent = new Intent(
                InstrumentationRegistry.getInstrumentation().getTargetContext(),
                CreatePromptActivity.class
        );

        try (ActivityScenario<CreatePromptActivity> scenario = ActivityScenario.launch(intent)) {
            Espresso.onView(ViewMatchers.withId(R.id.editTextTitle))
                    .perform(ViewActions.replaceText("Prompt title"), ViewActions.closeSoftKeyboard());

            Espresso.onView(ViewMatchers.withId(R.id.editTextPrompt))
                    .perform(ViewActions.replaceText("Prompt body text"), ViewActions.closeSoftKeyboard());

            // Leave tags empty
            Espresso.onView(ViewMatchers.withId(R.id.buttonCreatePrompt))
                    .perform(ViewActions.click());

            Espresso.onView(ViewMatchers.withId(R.id.editTextTags))
                    .check(ViewAssertions.matches(
                            ViewMatchers.hasErrorText("At least one tag is required")));
        }
    }

    /**
     * Black-box Test Case 4:
     * Editing a prompt with a title longer than 100 chars should show length error.
     */
    @Test
    public void testEditPromptWithTitleTooLongShowsError() {
        Intent intent = new Intent(
                InstrumentationRegistry.getInstrumentation().getTargetContext(),
                EditPromptActivity.class
        );
        // In a real test, you would put extras for an existing prompt ID.

        try (ActivityScenario<EditPromptActivity> scenario = ActivityScenario.launch(intent)) {
            StringBuilder longTitle = new StringBuilder();
            for (int i = 0; i < 120; i++) {
                longTitle.append("a");
            }

            Espresso.onView(ViewMatchers.withId(R.id.editTextTitle))
                    .perform(ViewActions.replaceText(longTitle.toString()), ViewActions.closeSoftKeyboard());

            Espresso.onView(ViewMatchers.withId(R.id.editTextPrompt))
                    .perform(ViewActions.replaceText("Prompt text"), ViewActions.closeSoftKeyboard());

            Espresso.onView(ViewMatchers.withId(R.id.editTextTags))
                    .perform(ViewActions.replaceText("gpt-4"), ViewActions.closeSoftKeyboard());

            Espresso.onView(ViewMatchers.withId(R.id.buttonUpdatePrompt))
                    .perform(ViewActions.click());

            Espresso.onView(ViewMatchers.withId(R.id.editTextTitle))
                    .check(ViewAssertions.matches(
                            ViewMatchers.hasErrorText("Title must be 100 characters or less")));
        }
    }
}
