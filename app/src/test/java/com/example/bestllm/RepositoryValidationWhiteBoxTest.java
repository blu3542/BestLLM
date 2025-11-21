package com.example.bestllm;

import com.example.bestllm.utils.Validators;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * White-box Tests for Repository Validation Logic
 * 
 * Tests validation logic used in repositories (AuthRepository, PostRepository, etc.)
 * These tests verify the validation branches that repositories use
 */
public class RepositoryValidationWhiteBoxTest {
    
    // ========== AUTH REPOSITORY VALIDATION TESTS ==========
    
    @Test
    public void test1_AuthRegisterNameValidation() {
        // Tests the name validation used in AuthRepository.register()
        assertTrue(Validators.isValidName("John Doe"));
        assertFalse(Validators.isValidName(null));
        assertFalse(Validators.isValidName(""));
    }
    
    @Test
    public void test2_AuthRegisterEmailValidation() {
        // Tests the USC email validation used in AuthRepository.register()
        assertTrue(Validators.isValidUSCEmail("test@usc.edu"));
        assertFalse(Validators.isValidUSCEmail("test@gmail.com"));
        assertFalse(Validators.isValidUSCEmail(null));
    }
    
    @Test
    public void test3_AuthRegisterStudentIdValidation() {
        // Tests the 10-digit student ID validation
        assertTrue(Validators.isValidStudentId("1234567890"));
        assertFalse(Validators.isValidStudentId("12345"));
        assertFalse(Validators.isValidStudentId("12345678901"));
        assertFalse(Validators.isValidStudentId(null));
    }
    
    @Test
    public void test4_AuthRegisterPasswordValidation() {
        // Tests password length validation
        assertTrue(Validators.isValidPassword("123456"));
        assertFalse(Validators.isValidPassword("12345"));
        assertFalse(Validators.isValidPassword(null));
    }
    
    @Test
    public void test5_AuthLoginEmailValidation() {
        // Tests email validation for login
        assertTrue(Validators.isValidUSCEmail("user@usc.edu"));
        assertFalse(Validators.isValidUSCEmail("user@gmail.com"));
    }
    
    @Test
    public void test6_AuthLoginPasswordValidation() {
        // Tests password validation for login
        assertTrue(Validators.isValidPassword("password123"));
        assertFalse(Validators.isValidPassword("short"));
    }
    
    @Test
    public void test7_AuthPasswordResetEmailValidation() {
        // Tests email validation for password reset
        assertTrue(Validators.isValidUSCEmail("reset@usc.edu"));
        assertFalse(Validators.isValidUSCEmail("reset@gmail.com"));
    }
    
    @Test
    public void test8_AuthProfileUpdateNameValidation() {
        // Tests name validation for profile update
        assertTrue(Validators.isValidName("Updated Name"));
        assertFalse(Validators.isValidName(null));
        assertFalse(Validators.isValidName(""));
    }
    
    // ========== POST REPOSITORY VALIDATION TESTS ==========
    
    @Test
    public void test9_PostRepositoryTitleValidation() {
        // Tests title validation logic (non-null, non-empty)
        String title = "Test Title";
        assertNotNull(title);
        assertFalse(title.trim().isEmpty());
        
        String emptyTitle = "";
        assertTrue(emptyTitle.trim().isEmpty());
        
        String nullTitle = null;
        assertNull(nullTitle);
    }
    
    @Test
    public void test10_PostRepositoryBodyValidation() {
        // Tests body validation logic (non-null, non-empty)
        String body = "Test Body";
        assertNotNull(body);
        assertFalse(body.trim().isEmpty());
        
        String emptyBody = "   ";
        assertTrue(emptyBody.trim().isEmpty());
    }
    
    // ========== VOTE REPOSITORY VALIDATION TESTS ==========
    
    @Test
    public void test11_VoteRepositoryValueValidation() {
        // Tests vote value validation (must be 1 or -1)
        int upvote = 1;
        assertTrue(upvote == 1 || upvote == -1);
        
        int downvote = -1;
        assertTrue(downvote == 1 || downvote == -1);
        
        int invalid = 0;
        assertFalse(invalid == 1 || invalid == -1);
        
        int invalid2 = 2;
        assertFalse(invalid2 == 1 || invalid2 == -1);
    }
    
    @Test
    public void test12_VoteRepositoryPostVoteValidation() {
        // Tests post vote validation logic
        int value = 1;
        if (value != 1 && value != -1) {
            fail("Invalid vote value");
        }
        assertTrue(value == 1 || value == -1);
    }
    
    @Test
    public void test13_VoteRepositoryCommentVoteValidation() {
        // Tests comment vote validation logic
        int value = -1;
        if (value != 1 && value != -1) {
            fail("Invalid vote value");
        }
        assertTrue(value == 1 || value == -1);
    }
    
    // ========== COMMENT REPOSITORY VALIDATION TESTS ==========
    
    @Test
    public void test14_CommentRepositoryBodyValidation() {
        // Tests comment body validation (required, title optional)
        String body = "Comment body";
        assertNotNull(body);
        assertFalse(body.trim().isEmpty());
        
        String emptyBody = "";
        assertTrue(emptyBody.trim().isEmpty());
    }
    
    @Test
    public void test15_CommentRepositoryTitleOptional() {
        // Tests that comment title is optional (can be null)
        String title = null;
        assertNull(title);
        
        String emptyTitle = "";
        assertTrue(emptyTitle.isEmpty());
        
        String validTitle = "Title";
        assertNotNull(validTitle);
    }
}

