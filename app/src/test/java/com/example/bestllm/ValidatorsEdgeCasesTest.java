package com.example.bestllm;

import com.example.bestllm.utils.Validators;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Validators Edge Cases and Comprehensive Coverage Tests
 * 
 * Tests all edge cases and branches in Validators class
 * This should push utils package from 0% to near 100%
 */
public class ValidatorsEdgeCasesTest {
    
    // ========== USC EMAIL VALIDATION - ALL EDGE CASES ==========
    
    @Test
    public void testUSCEmailValidFormats() {
        assertTrue(Validators.isValidUSCEmail("test@usc.edu"));
        assertTrue(Validators.isValidUSCEmail("student123@usc.edu"));
        assertTrue(Validators.isValidUSCEmail("john.doe@usc.edu"));
        assertTrue(Validators.isValidUSCEmail("test_user@usc.edu"));
        assertTrue(Validators.isValidUSCEmail("test+tag@usc.edu"));
        assertTrue(Validators.isValidUSCEmail("test-user@usc.edu"));
    }
    
    @Test
    public void testUSCEmailInvalidDomains() {
        assertFalse(Validators.isValidUSCEmail("test@gmail.com"));
        assertFalse(Validators.isValidUSCEmail("test@usc.com"));
        assertFalse(Validators.isValidUSCEmail("test@usc"));
        assertFalse(Validators.isValidUSCEmail("test@yahoo.com"));
        assertFalse(Validators.isValidUSCEmail("test@outlook.com"));
    }
    
    @Test
    public void testUSCEmailNullAndEmpty() {
        assertFalse(Validators.isValidUSCEmail(null));
        assertFalse(Validators.isValidUSCEmail(""));
        assertFalse(Validators.isValidUSCEmail("   "));
    }
    
    @Test
    public void testUSCEmailInvalidFormats() {
        assertFalse(Validators.isValidUSCEmail("@usc.edu"));
        assertFalse(Validators.isValidUSCEmail("test@"));
        assertFalse(Validators.isValidUSCEmail("test@usc"));
        assertFalse(Validators.isValidUSCEmail("usc.edu"));
    }
    
    @Test
    public void testUSCEmailCaseInsensitive() {
        assertTrue(Validators.isValidUSCEmail("TEST@USC.EDU"));
        assertTrue(Validators.isValidUSCEmail("Test@Usc.Edu"));
        assertTrue(Validators.isValidUSCEmail("test@USC.EDU"));
    }
    
    // ========== PASSWORD VALIDATION - ALL EDGE CASES ==========
    
    @Test
    public void testPasswordValidCases() {
        assertTrue(Validators.isValidPassword("123456"));
        assertTrue(Validators.isValidPassword("password"));
        assertTrue(Validators.isValidPassword("verylongpassword123456"));
        assertTrue(Validators.isValidPassword("abc123"));
        assertTrue(Validators.isValidPassword("!@#$%^"));
    }
    
    @Test
    public void testPasswordInvalidCases() {
        assertFalse(Validators.isValidPassword("12345"));
        assertFalse(Validators.isValidPassword("pass"));
        assertFalse(Validators.isValidPassword(""));
        assertFalse(Validators.isValidPassword(null));
    }
    
    @Test
    public void testPasswordExactMinimum() {
        assertTrue(Validators.isValidPassword("123456")); // exactly 6
        assertFalse(Validators.isValidPassword("12345")); // 5 chars
    }
    
    // ========== STUDENT ID VALIDATION - ALL EDGE CASES ==========
    
    @Test
    public void testStudentIdValidCases() {
        assertTrue(Validators.isValidStudentId("1234567890"));
        assertTrue(Validators.isValidStudentId("0000000000"));
        assertTrue(Validators.isValidStudentId("9999999999"));
        assertTrue(Validators.isValidStudentId("0123456789"));
    }
    
    @Test
    public void testStudentIdInvalidLength() {
        assertFalse(Validators.isValidStudentId("12345"));
        assertFalse(Validators.isValidStudentId("123456789"));
        assertFalse(Validators.isValidStudentId("12345678901"));
        assertFalse(Validators.isValidStudentId("123456789012"));
    }
    
    @Test
    public void testStudentIdInvalidCharacters() {
        assertFalse(Validators.isValidStudentId("abcdefghij"));
        assertFalse(Validators.isValidStudentId("12345abcde"));
        assertFalse(Validators.isValidStudentId("12345-6789"));
        assertFalse(Validators.isValidStudentId("12345 6789"));
    }
    
    @Test
    public void testStudentIdNullAndEmpty() {
        assertFalse(Validators.isValidStudentId(null));
        assertFalse(Validators.isValidStudentId(""));
        assertFalse(Validators.isValidStudentId("   "));
    }
    
    // ========== NAME VALIDATION - ALL EDGE CASES ==========
    
    @Test
    public void testNameValidCases() {
        assertTrue(Validators.isValidName("John Doe"));
        assertTrue(Validators.isValidName("AB"));
        assertTrue(Validators.isValidName("Mary Jane Watson"));
        assertTrue(Validators.isValidName("O'Brien"));
        assertTrue(Validators.isValidName("Jean-Pierre"));
    }
    
    @Test
    public void testNameInvalidLength() {
        assertFalse(Validators.isValidName("A"));
        assertFalse(Validators.isValidName(""));
    }
    
    @Test
    public void testNameNullAndEmpty() {
        assertFalse(Validators.isValidName(null));
        assertFalse(Validators.isValidName(""));
        assertFalse(Validators.isValidName("   "));
        assertFalse(Validators.isValidName("\t"));
        assertFalse(Validators.isValidName("\n"));
    }
    
    @Test
    public void testNameExactMinimum() {
        assertTrue(Validators.isValidName("AB")); // exactly 2
        assertFalse(Validators.isValidName("A")); // 1 char
    }
    
    @Test
    public void testNameWithWhitespace() {
        assertFalse(Validators.isValidName("   "));
        assertFalse(Validators.isValidName("\t\t"));
        assertTrue(Validators.isValidName("  John  ")); // trimmed should be valid
    }
}

