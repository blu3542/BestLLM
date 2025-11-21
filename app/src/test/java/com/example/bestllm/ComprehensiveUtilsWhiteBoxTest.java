package com.example.bestllm;

import com.example.bestllm.utils.Validators;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Comprehensive White-box Test Suite - Utils
 * 
 * Test Cases 1-5: Validators - Name validation
 * Test Cases 6-10: Validators - Email validation
 * Test Cases 11-15: Validators - Student ID validation
 * Test Cases 16-20: Validators - Password validation
 * Test Cases 21-25: Validators - Tag parsing and validation
 */
public class ComprehensiveUtilsWhiteBoxTest {
    
    // Test Case 1: Valid name
    @Test
    public void testValidName() {
        assertTrue(Validators.isValidName("John Doe"));
        assertTrue(Validators.isValidName("Mary Jane Watson"));
    }
    
    // Test Case 2: Invalid name - null
    @Test
    public void testInvalidNameNull() {
        assertFalse(Validators.isValidName(null));
    }
    
    // Test Case 3: Invalid name - empty
    @Test
    public void testInvalidNameEmpty() {
        assertFalse(Validators.isValidName(""));
        assertFalse(Validators.isValidName("   "));
    }
    
    // Test Case 4: Invalid name - too short
    @Test
    public void testInvalidNameTooShort() {
        assertFalse(Validators.isValidName("A"));
    }
    
    // Test Case 5: Name with special characters
    @Test
    public void testNameWithSpecialCharacters() {
        // Depends on validator implementation
        assertTrue(Validators.isValidName("O'Brien"));
    }
    
    // Test Case 6: Valid USC email
    @Test
    public void testValidUSCEmail() {
        assertTrue(Validators.isValidUSCEmail("test@usc.edu"));
        assertTrue(Validators.isValidUSCEmail("student123@usc.edu"));
    }
    
    // Test Case 7: Invalid email - non-USC domain
    @Test
    public void testInvalidEmailNonUSC() {
        assertFalse(Validators.isValidUSCEmail("test@gmail.com"));
        assertFalse(Validators.isValidUSCEmail("test@usc.com"));
    }
    
    // Test Case 8: Invalid email - null
    @Test
    public void testInvalidEmailNull() {
        assertFalse(Validators.isValidUSCEmail(null));
    }
    
    // Test Case 9: Invalid email - empty
    @Test
    public void testInvalidEmailEmpty() {
        assertFalse(Validators.isValidUSCEmail(""));
    }
    
    // Test Case 10: Invalid email - missing @
    @Test
    public void testInvalidEmailMissingAt() {
        assertFalse(Validators.isValidUSCEmail("testusc.edu"));
    }
    
    // Test Case 11: Valid 10-digit student ID
    @Test
    public void testValidStudentId() {
        assertTrue(Validators.isValidStudentId("1234567890"));
        assertTrue(Validators.isValidStudentId("0000000000"));
    }
    
    // Test Case 12: Invalid student ID - too short
    @Test
    public void testInvalidStudentIdTooShort() {
        assertFalse(Validators.isValidStudentId("12345"));
        assertFalse(Validators.isValidStudentId("123456789"));
    }
    
    // Test Case 13: Invalid student ID - too long
    @Test
    public void testInvalidStudentIdTooLong() {
        assertFalse(Validators.isValidStudentId("12345678901"));
    }
    
    // Test Case 14: Invalid student ID - non-numeric
    @Test
    public void testInvalidStudentIdNonNumeric() {
        assertFalse(Validators.isValidStudentId("abcdefghij"));
        assertFalse(Validators.isValidStudentId("12345abcde"));
    }
    
    // Test Case 15: Invalid student ID - null/empty
    @Test
    public void testInvalidStudentIdNull() {
        assertFalse(Validators.isValidStudentId(null));
        assertFalse(Validators.isValidStudentId(""));
    }
    
    // Test Case 16: Valid password - minimum length
    @Test
    public void testValidPasswordMinimum() {
        assertTrue(Validators.isValidPassword("123456"));
        assertTrue(Validators.isValidPassword("password"));
    }
    
    // Test Case 17: Invalid password - too short
    @Test
    public void testInvalidPasswordTooShort() {
        assertFalse(Validators.isValidPassword("12345"));
        assertFalse(Validators.isValidPassword("pass"));
    }
    
    // Test Case 18: Invalid password - null
    @Test
    public void testInvalidPasswordNull() {
        assertFalse(Validators.isValidPassword(null));
    }
    
    // Test Case 19: Invalid password - empty
    @Test
    public void testInvalidPasswordEmpty() {
        assertFalse(Validators.isValidPassword(""));
    }
    
    // Test Case 20: Valid password - long password
    @Test
    public void testValidPasswordLong() {
        assertTrue(Validators.isValidPassword("verylongpassword123456"));
    }
    
    // Test Case 21: Email pattern matching - valid format
    @Test
    public void testEmailPatternMatching() {
        assertTrue(Validators.isValidUSCEmail("test.user@usc.edu"));
        assertTrue(Validators.isValidUSCEmail("student123@usc.edu"));
    }
    
    // Test Case 22: Email pattern matching - invalid format
    @Test
    public void testEmailPatternInvalidFormat() {
        assertFalse(Validators.isValidUSCEmail("invalid@usc.edu"));
        assertFalse(Validators.isValidUSCEmail("@usc.edu"));
    }
    
    // Test Case 23: Password edge cases
    @Test
    public void testPasswordEdgeCases() {
        assertTrue(Validators.isValidPassword("123456")); // exactly 6
        assertFalse(Validators.isValidPassword("12345")); // 5 chars
    }
    
    // Test Case 24: Student ID edge cases
    @Test
    public void testStudentIdEdgeCases() {
        assertTrue(Validators.isValidStudentId("0000000000")); // all zeros
        assertFalse(Validators.isValidStudentId("123456789")); // 9 digits
        assertFalse(Validators.isValidStudentId("12345678901")); // 11 digits
    }
    
    // Test Case 25: Name edge cases
    @Test
    public void testNameEdgeCases() {
        assertTrue(Validators.isValidName("AB")); // exactly 2 chars
        assertFalse(Validators.isValidName("A")); // 1 char
        assertFalse(Validators.isValidName("   ")); // whitespace only
    }
}

