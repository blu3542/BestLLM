package com.example.bestllm;

import android.app.Activity;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.bestllm.ui.auth.RegisterActivity;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Black-box tests for RegisterActivity.
 *
 * Feature coverage:
 *  - USC email validation
 *  - 10-digit student ID validation
 *  - Minimum password length
 *  - Password confirmation matching
 */
@RunWith(AndroidJUnit4.class)
public class RegisterActivityBlackBoxTest {

    /**
     * Black-box Test Case 1:
     * Using a non-USC email should show an error toast.
     */
    @Test
    public void testRegisterWithInvalidUSCEmailShowsErrorToast() {
        Intent intent = new Intent(
                InstrumentationRegistry.getInstrumentation().getTargetContext(),
                RegisterActivity.class
        );

        try (ActivityScenario<RegisterActivity> scenario = ActivityScenario.launch(intent)) {
            // Fill valid name, ID, password; invalid email domain
            Espresso.onView(ViewMatchers.withId(R.id.editTextName))
                    .perform(ViewActions.replaceText("Test User"), ViewActions.closeSoftKeyboard());

            Espresso.onView(ViewMatchers.withId(R.id.editTextEmail))
                    .perform(ViewActions.replaceText("user@gmail.com"), ViewActions.closeSoftKeyboard());

            Espresso.onView(ViewMatchers.withId(R.id.editStudentId))
                    .perform(ViewActions.replaceText("1234567890"), ViewActions.closeSoftKeyboard());

            Espresso.onView(ViewMatchers.withId(R.id.editTextPassword))
                    .perform(ViewActions.replaceText("secret1"), ViewActions.closeSoftKeyboard());

            Espresso.onView(ViewMatchers.withId(R.id.editTextConfirmPassword))
                    .perform(ViewActions.replaceText("secret1"), ViewActions.closeSoftKeyboard());

            final Activity[] activityRef = new Activity[1];
            scenario.onActivity(activity -> activityRef[0] = activity);

            Espresso.onView(ViewMatchers.withId(R.id.buttonRegister))
                    .perform(ViewActions.click());

            // Error message comes from AuthRepository: "Please use a valid USC email (@usc.edu)"
            Espresso.onView(ViewMatchers.withText("Please use a valid USC email (@usc.edu)"))
                    .inRoot(RootMatchers.withDecorView(
                            Matchers.not(activityRef[0].getWindow().getDecorView())))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        }
    }

    /**
     * Black-box Test Case 2:
     * Student ID not exactly 10 digits should show an error toast.
     */
    @Test
    public void testRegisterWithInvalidStudentIdShowsErrorToast() {
        Intent intent = new Intent(
                InstrumentationRegistry.getInstrumentation().getTargetContext(),
                RegisterActivity.class
        );

        try (ActivityScenario<RegisterActivity> scenario = ActivityScenario.launch(intent)) {
            Espresso.onView(ViewMatchers.withId(R.id.editTextName))
                    .perform(ViewActions.replaceText("Test User"), ViewActions.closeSoftKeyboard());

            Espresso.onView(ViewMatchers.withId(R.id.editTextEmail))
                    .perform(ViewActions.replaceText("user@usc.edu"), ViewActions.closeSoftKeyboard());

            // 9 digits instead of 10
            Espresso.onView(ViewMatchers.withId(R.id.editStudentId))
                    .perform(ViewActions.replaceText("123456789"), ViewActions.closeSoftKeyboard());

            Espresso.onView(ViewMatchers.withId(R.id.editTextPassword))
                    .perform(ViewActions.replaceText("secret1"), ViewActions.closeSoftKeyboard());

            Espresso.onView(ViewMatchers.withId(R.id.editTextConfirmPassword))
                    .perform(ViewActions.replaceText("secret1"), ViewActions.closeSoftKeyboard());

            final Activity[] activityRef = new Activity[1];
            scenario.onActivity(activity -> activityRef[0] = activity);

            Espresso.onView(ViewMatchers.withId(R.id.buttonRegister))
                    .perform(ViewActions.click());

            Espresso.onView(ViewMatchers.withText("Student ID must be exactly 10 digits"))
                    .inRoot(RootMatchers.withDecorView(
                            Matchers.not(activityRef[0].getWindow().getDecorView())))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        }
    }

    /**
     * Black-box Test Case 3:
     * Password shorter than 6 characters should show an error toast.
     */
    @Test
    public void testRegisterWithPasswordTooShortShowsErrorToast() {
        Intent intent = new Intent(
                InstrumentationRegistry.getInstrumentation().getTargetContext(),
                RegisterActivity.class
        );

        try (ActivityScenario<RegisterActivity> scenario = ActivityScenario.launch(intent)) {
            Espresso.onView(ViewMatchers.withId(R.id.editTextName))
                    .perform(ViewActions.replaceText("Test User"), ViewActions.closeSoftKeyboard());

            Espresso.onView(ViewMatchers.withId(R.id.editTextEmail))
                    .perform(ViewActions.replaceText("user@usc.edu"), ViewActions.closeSoftKeyboard());

            Espresso.onView(ViewMatchers.withId(R.id.editStudentId))
                    .perform(ViewActions.replaceText("1234567890"), ViewActions.closeSoftKeyboard());

            // Too short password
            Espresso.onView(ViewMatchers.withId(R.id.editTextPassword))
                    .perform(ViewActions.replaceText("12345"), ViewActions.closeSoftKeyboard());

            Espresso.onView(ViewMatchers.withId(R.id.editTextConfirmPassword))
                    .perform(ViewActions.replaceText("12345"), ViewActions.closeSoftKeyboard());

            final Activity[] activityRef = new Activity[1];
            scenario.onActivity(activity -> activityRef[0] = activity);

            Espresso.onView(ViewMatchers.withId(R.id.buttonRegister))
                    .perform(ViewActions.click());

            Espresso.onView(ViewMatchers.withText("Password must be at least 6 characters"))
                    .inRoot(RootMatchers.withDecorView(
                            Matchers.not(activityRef[0].getWindow().getDecorView())))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        }
    }

    /**
     * Black-box Test Case 4:
     * Mismatched password and confirm password should show "Passwords do not match" toast.
     */
    @Test
    public void testRegisterWithMismatchedPasswordsShowsInlineToast() {
        Intent intent = new Intent(
                InstrumentationRegistry.getInstrumentation().getTargetContext(),
                RegisterActivity.class
        );

        try (ActivityScenario<RegisterActivity> scenario = ActivityScenario.launch(intent)) {
            Espresso.onView(ViewMatchers.withId(R.id.editTextName))
                    .perform(ViewActions.replaceText("Test User"), ViewActions.closeSoftKeyboard());

            Espresso.onView(ViewMatchers.withId(R.id.editTextEmail))
                    .perform(ViewActions.replaceText("user@usc.edu"), ViewActions.closeSoftKeyboard());

            Espresso.onView(ViewMatchers.withId(R.id.editStudentId))
                    .perform(ViewActions.replaceText("1234567890"), ViewActions.closeSoftKeyboard());

            Espresso.onView(ViewMatchers.withId(R.id.editTextPassword))
                    .perform(ViewActions.replaceText("secret1"), ViewActions.closeSoftKeyboard());

            Espresso.onView(ViewMatchers.withId(R.id.editTextConfirmPassword))
                    .perform(ViewActions.replaceText("different"), ViewActions.closeSoftKeyboard());

            final Activity[] activityRef = new Activity[1];
            scenario.onActivity(activity -> activityRef[0] = activity);

            Espresso.onView(ViewMatchers.withId(R.id.buttonRegister))
                    .perform(ViewActions.click());

            Espresso.onView(ViewMatchers.withText("Passwords do not match"))
                    .inRoot(RootMatchers.withDecorView(
                            Matchers.not(activityRef[0].getWindow().getDecorView())))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        }
    }
}
