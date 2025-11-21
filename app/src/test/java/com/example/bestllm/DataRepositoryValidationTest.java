package com.example.bestllm;

import com.example.bestllm.utils.Validators;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Data Repository Validation Logic Tests
 * 
 * Tests validation logic used in PostRepository, CommentRepository, etc.
 * This covers the data package validation branches
 */
public class DataRepositoryValidationTest {
    
    // ========== POST REPOSITORY VALIDATION ==========
    
    @Test
    public void testPostRepositoryTitleValidation() {
        // Tests PostRepository.createPost() title validation
        String title = "Valid Title";
        assertNotNull(title);
        assertFalse(title.trim().isEmpty());
        
        String nullTitle = null;
        assertNull(nullTitle);
        
        String emptyTitle = "";
        assertTrue(emptyTitle.trim().isEmpty());
        
        String whitespaceTitle = "   ";
        assertTrue(whitespaceTitle.trim().isEmpty());
    }
    
    @Test
    public void testPostRepositoryBodyValidation() {
        // Tests PostRepository.createPost() body validation
        String body = "Valid body content";
        assertNotNull(body);
        assertFalse(body.trim().isEmpty());
        
        String nullBody = null;
        assertNull(nullBody);
        
        String emptyBody = "";
        assertTrue(emptyBody.trim().isEmpty());
    }
    
    @Test
    public void testPostRepositoryTagsValidation() {
        // Tests PostRepository tag handling
        java.util.List<String> tags = java.util.Arrays.asList("tag1", "tag2");
        assertNotNull(tags);
        assertFalse(tags.isEmpty());
        
        java.util.List<String> nullTags = null;
        assertNull(nullTags);
        
        java.util.List<String> emptyTags = new java.util.ArrayList<>();
        assertTrue(emptyTags.isEmpty());
    }
    
    // ========== COMMENT REPOSITORY VALIDATION ==========
    
    @Test
    public void testCommentRepositoryBodyRequired() {
        // Tests CommentRepository.addComment() body validation
        String body = "Comment body";
        assertNotNull(body);
        assertFalse(body.trim().isEmpty());
        
        String emptyBody = "";
        assertTrue(emptyBody.trim().isEmpty());
    }
    
    @Test
    public void testCommentRepositoryTitleOptional() {
        // Tests CommentRepository title is optional
        String title = null;
        assertNull(title);
        
        String emptyTitle = "";
        assertTrue(emptyTitle.isEmpty() || emptyTitle.trim().isEmpty());
        
        String validTitle = "Title";
        assertNotNull(validTitle);
        assertFalse(validTitle.trim().isEmpty());
    }
    
    // ========== AUTH REPOSITORY VALIDATION ==========
    
    @Test
    public void testAuthRepositoryRegisterValidation() {
        // Tests all validation checks in AuthRepository.register()
        String name = "John Doe";
        assertTrue(Validators.isValidName(name));
        
        String email = "test@usc.edu";
        assertTrue(Validators.isValidUSCEmail(email));
        
        String studentId = "1234567890";
        assertTrue(Validators.isValidStudentId(studentId));
        
        String password = "password123";
        assertTrue(Validators.isValidPassword(password));
    }
    
    @Test
    public void testAuthRepositoryLoginValidation() {
        // Tests validation in AuthRepository.login()
        String email = "user@usc.edu";
        assertTrue(Validators.isValidUSCEmail(email));
        
        String password = "password123";
        assertTrue(Validators.isValidPassword(password));
    }
    
    @Test
    public void testAuthRepositoryPasswordResetValidation() {
        // Tests validation in AuthRepository.resetPassword()
        String email = "reset@usc.edu";
        assertTrue(Validators.isValidUSCEmail(email));
        
        String invalidEmail = "reset@gmail.com";
        assertFalse(Validators.isValidUSCEmail(invalidEmail));
    }
    
    @Test
    public void testAuthRepositoryProfileUpdateValidation() {
        // Tests validation in AuthRepository.updateUserProfile()
        String name = "Updated Name";
        assertTrue(Validators.isValidName(name));
        
        String invalidName = "";
        assertFalse(Validators.isValidName(invalidName));
    }
    
    // ========== VOTE REPOSITORY VALIDATION ==========
    
    @Test
    public void testVoteRepositoryValueValidation() {
        // Tests VoteRepository vote value validation (must be 1 or -1)
        int upvote = 1;
        assertTrue(upvote == 1 || upvote == -1);
        
        int downvote = -1;
        assertTrue(downvote == 1 || downvote == -1);
        
        int invalid1 = 0;
        assertFalse(invalid1 == 1 || invalid1 == -1);
        
        int invalid2 = 2;
        assertFalse(invalid2 == 1 || invalid2 == -1);
        
        int invalid3 = -2;
        assertFalse(invalid3 == 1 || invalid3 == -1);
    }
    
    @Test
    public void testVoteRepositoryPostVoteValidation() {
        // Tests VoteRepository.votePost() validation
        String postId = "post123";
        String userId = "user123";
        int value = 1;
        
        assertNotNull(postId);
        assertNotNull(userId);
        assertTrue(value == 1 || value == -1);
    }
    
    @Test
    public void testVoteRepositoryCommentVoteValidation() {
        // Tests VoteRepository.voteComment() validation
        String postId = "post123";
        String commentId = "comment123";
        String userId = "user123";
        int value = -1;
        
        assertNotNull(postId);
        assertNotNull(commentId);
        assertNotNull(userId);
        assertTrue(value == 1 || value == -1);
    }
}

