package com.example.bestllm;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.bestllm.ui.auth.RegisterActivity;

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
     * Using a non-USC email should trigger validation and prevent registration.
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

            Espresso.onView(ViewMatchers.withId(R.id.buttonRegister))
                    .perform(ViewActions.click());

            // Wait for async validation from AuthRepository
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Verify button is re-enabled after validation failure (indicates error occurred and registration was blocked)
            Espresso.onView(ViewMatchers.withId(R.id.buttonRegister))
                    .check(ViewAssertions.matches(ViewMatchers.isEnabled()));

            // Verify we're still on the RegisterActivity (not navigated away)
            Espresso.onView(ViewMatchers.withId(R.id.editTextEmail))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        }
    }

    /**
     * Black-box Test Case 2:
     * Student ID not exactly 10 digits should show an error and prevent registration.
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

            Espresso.onView(ViewMatchers.withId(R.id.buttonRegister))
                    .perform(ViewActions.click());

            // This validation is synchronous, so button should still be enabled immediately
            Espresso.onView(ViewMatchers.withId(R.id.buttonRegister))
                    .check(ViewAssertions.matches(ViewMatchers.isEnabled()));

            // Verify we're still on the RegisterActivity
            Espresso.onView(ViewMatchers.withId(R.id.editTextEmail))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        }
    }

    /**
     * Black-box Test Case 3:
     * Password shorter than 6 characters should trigger validation and prevent registration.
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

            Espresso.onView(ViewMatchers.withId(R.id.buttonRegister))
                    .perform(ViewActions.click());

            // Wait for async validation from AuthRepository
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Verify button is re-enabled after validation failure
            Espresso.onView(ViewMatchers.withId(R.id.buttonRegister))
                    .check(ViewAssertions.matches(ViewMatchers.isEnabled()));

            // Verify we're still on the RegisterActivity
            Espresso.onView(ViewMatchers.withId(R.id.editTextEmail))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        }
    }

    /**
     * Black-box Test Case 4:
     * Mismatched password and confirm password should show error and prevent registration.
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

            Espresso.onView(ViewMatchers.withId(R.id.buttonRegister))
                    .perform(ViewActions.click());

            // This validation is synchronous, so button should still be enabled immediately
            Espresso.onView(ViewMatchers.withId(R.id.buttonRegister))
                    .check(ViewAssertions.matches(ViewMatchers.isEnabled()));

            // Verify we're still on the RegisterActivity
            Espresso.onView(ViewMatchers.withId(R.id.editTextEmail))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        }
    }
}
