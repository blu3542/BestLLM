package com.example.bestllm;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.bestllm.utils.Validators;

import org.junit.Test;

/**
 * White-box tests for Validators utility methods.
 *
 * Feature coverage:
 *  - USC email validation
 *  - Student ID validation
 *  - Password validation
 *  - Name validation
 */
public class ValidatorsWhiteBoxTest {

    /**
     * White-box Test Case 1: Valid USC email is accepted.
     */
    @Test
    public void testValidUSCEmailAccepted() {
        assertTrue("Valid USC email should be accepted",
                Validators.isValidUSCEmail("test.user@usc.edu"));
        assertTrue("Uppercase USC email should be normalized and accepted",
                Validators.isValidUSCEmail("TEST.USER@USC.EDU"));
    }

    /**
     * White-box Test Case 2: Non-USC domains are rejected.
     */
    @Test
    public void testInvalidUSCEmailRejectedForNonUSCDomain() {
        assertFalse("Non-USC domain should be rejected",
                Validators.isValidUSCEmail("user@gmail.com"));
        assertFalse("USC substring but wrong domain should be rejected",
                Validators.isValidUSCEmail("user@somethingusc.edu"));
    }

    /**
     * White-box Test Case 3: Badly formatted emails are rejected.
     */
    @Test
    public void testInvalidUSCEmailRejectedForBadFormat() {
        assertFalse("Missing @ should be rejected",
                Validators.isValidUSCEmail("userusc.edu"));
        assertFalse("Missing local part should be rejected",
                Validators.isValidUSCEmail("@usc.edu"));
        assertFalse("Null email should be rejected",
                Validators.isValidUSCEmail(null));
        assertFalse("Empty email should be rejected",
                Validators.isValidUSCEmail(""));
    }

    /**
     * White-box Test Case 4: Exactly 10-digit student IDs are accepted.
     */
    @Test
    public void testValidStudentIdTenDigits() {
        assertTrue("10-digit numeric student ID should be valid",
                Validators.isValidStudentId("0123456789"));
    }

    /**
     * White-box Test Case 5: Wrong length or non-digit characters are rejected.
     */
    @Test
    public void testInvalidStudentIdWrongLengthOrNonDigits() {
        assertFalse("Too short ID should be invalid",
                Validators.isValidStudentId("123456789"));
        assertFalse("Too long ID should be invalid",
                Validators.isValidStudentId("12345678901"));
        assertFalse("Alphanumeric ID should be invalid",
                Validators.isValidStudentId("12345abcde"));
        assertFalse("Null ID should be invalid",
                Validators.isValidStudentId(null));
    }

    /**
     * White-box Test Case 6: Password of length >= 6 is accepted.
     */
    @Test
    public void testValidPasswordAtLeastSixCharacters() {
        assertTrue("Password of length 6 should be valid",
                Validators.isValidPassword("123456"));
        assertTrue("Longer password should be valid",
                Validators.isValidPassword("longpassword123"));
    }

    /**
     * White-box Test Case 7: Null or too-short passwords are rejected.
     */
    @Test
    public void testInvalidPasswordNullOrTooShort() {
        assertFalse("Null password should be invalid",
                Validators.isValidPassword(null));
        assertFalse("Empty password should be invalid",
                Validators.isValidPassword(""));
        assertFalse("Password shorter than 6 should be invalid",
                Validators.isValidPassword("12345"));
    }

    /**
     * White-box Test Case 8: Names with at least 2 non-blank characters are accepted.
     */
    @Test
    public void testValidNameAtLeastTwoNonBlankCharacters() {
        assertTrue("Simple name should be valid",
                Validators.isValidName("Al"));
        assertTrue("Name with spaces should be valid when trimmed",
                Validators.isValidName("  John "));
    }

    /**
     * White-box Test Case 9: Null, blank, or 1-char names are rejected.
     */
    @Test
    public void testInvalidNameNullOrBlank() {
        assertFalse("Null name should be invalid",
                Validators.isValidName(null));
        assertFalse("Empty name should be invalid",
                Validators.isValidName(""));
        assertFalse("Whitespace-only name should be invalid",
                Validators.isValidName("   "));
        assertFalse("Single-character name should be invalid",
                Validators.isValidName("A"));
    }
}
