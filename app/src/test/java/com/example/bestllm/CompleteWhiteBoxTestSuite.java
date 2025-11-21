package com.example.bestllm;

import com.example.bestllm.models.Comment;
import com.example.bestllm.models.Post;
import com.example.bestllm.models.Prompt;
import com.example.bestllm.models.User;
import com.example.bestllm.models.Vote;
import com.example.bestllm.utils.Validators;
import com.google.firebase.Timestamp;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Complete White-box Test Suite - 25 JUnit Tests
 * 
 * Covers all models and utility classes with comprehensive test cases
 * 
 * Test Cases 1-5: Post Model
 * Test Cases 6-10: Comment Model
 * Test Cases 11-15: User Model
 * Test Cases 16-20: Prompt Model
 * Test Cases 21-25: Vote Model and Validators
 */
public class CompleteWhiteBoxTestSuite {
    
    // ========== POST MODEL TESTS (1-5) ==========
    
    @Test
    public void test1_PostCreationWithAllFields() {
        Post post = new Post("post1", "Title", "Body", Arrays.asList("tag1", "tag2"), "author1", "Author Name");
        assertEquals("post1", post.getPostId());
        assertEquals("Title", post.getTitle());
        assertEquals("Body", post.getBody());
        assertEquals(2, post.getTags().size());
        assertEquals(0, post.getUpvotes());
        assertEquals(0, post.getCommentCount());
        assertNotNull(post.getCreatedAt());
    }
    
    @Test
    public void test2_PostNetVotesCalculation() {
        Post post = new Post("post1", "Title", "Body", null, "author1", "Author");
        post.setUpvotes(10);
        post.setDownvotes(3);
        assertEquals(7, post.getNetVotes());
        post.setUpvotes(2);
        post.setDownvotes(8);
        assertEquals(-6, post.getNetVotes());
    }
    
    @Test
    public void test3_PostWithNullTags() {
        Post post = new Post("post1", "Title", "Body", null, "author1", "Author");
        assertNotNull(post.getTags());
        assertTrue(post.getTags().isEmpty());
    }
    
    @Test
    public void test4_PostDefaultConstructor() {
        Post post = new Post();
        assertNotNull(post);
        assertNotNull(post.getTags());
        post.setPostId("newId");
        post.setTitle("New Title");
        assertEquals("newId", post.getPostId());
        assertEquals("New Title", post.getTitle());
    }
    
    @Test
    public void test5_PostTagManipulation() {
        Post post = new Post();
        List<String> tags = Arrays.asList("tag1", "tag2", "tag3");
        post.setTags(tags);
        assertEquals(3, post.getTags().size());
        post.setCommentCount(5);
        assertEquals(5, post.getCommentCount());
    }
    
    // ========== COMMENT MODEL TESTS (6-10) ==========
    
    @Test
    public void test6_CommentCreationWithTitle() {
        Comment comment = new Comment("comment1", "post1", "author1", "Author", "Title", "Body");
        assertEquals("comment1", comment.getCommentId());
        assertEquals("Title", comment.getTitle());
        assertEquals("Body", comment.getBody());
        assertEquals(0, comment.getUpvotes());
        assertNotNull(comment.getCreatedAt());
    }
    
    @Test
    public void test7_CommentCreationWithoutTitle() {
        Comment comment = new Comment("comment1", "post1", "author1", "Author", null, "Body");
        assertNull(comment.getTitle());
        assertEquals("Body", comment.getBody());
    }
    
    @Test
    public void test8_CommentNetVotesCalculation() {
        Comment comment = new Comment("comment1", "post1", "author1", "Author", null, "Body");
        comment.setUpvotes(5);
        comment.setDownvotes(2);
        assertEquals(3, comment.getNetVotes());
        comment.setUpvotes(0);
        comment.setDownvotes(0);
        assertEquals(0, comment.getNetVotes());
    }
    
    @Test
    public void test9_CommentTitleGetterSetter() {
        Comment comment = new Comment("comment1", "post1", "author1", "Author", null, "Body");
        comment.setTitle("New Title");
        assertEquals("New Title", comment.getTitle());
        comment.setTitle(null);
        assertNull(comment.getTitle());
        comment.setTitle("");
        assertEquals("", comment.getTitle());
    }
    
    @Test
    public void test10_CommentDefaultConstructor() {
        Comment comment = new Comment();
        assertNotNull(comment);
        comment.setCommentId("newId");
        comment.setBody("New Body");
        assertEquals("newId", comment.getCommentId());
        assertEquals("New Body", comment.getBody());
    }
    
    // ========== USER MODEL TESTS (11-15) ==========
    
    @Test
    public void test11_UserCreationWithRequiredFields() {
        User user = new User("user1", "John Doe", "john@usc.edu");
        assertEquals("user1", user.getUserId());
        assertEquals("John Doe", user.getName());
        assertEquals("john@usc.edu", user.getEmail());
        assertEquals("", user.getBio());
        assertEquals(0, user.getReputationScore());
        assertTrue(user.isUpvotableStatus());
    }
    
    @Test
    public void test12_UserStudentId() {
        User user = new User("user1", "John", "john@usc.edu");
        user.setStudentId("1234567890");
        assertEquals("1234567890", user.getStudentId());
        user.setStudentId(null);
        assertNull(user.getStudentId());
    }
    
    @Test
    public void test13_UserProfileFields() {
        User user = new User("user1", "John", "john@usc.edu");
        user.setDepartment("Computer Science");
        user.setSchool("Viterbi");
        user.setBio("Test bio");
        assertEquals("Computer Science", user.getDepartment());
        assertEquals("Viterbi", user.getSchool());
        assertEquals("Test bio", user.getBio());
    }
    
    @Test
    public void test14_UserReputationScore() {
        User user = new User("user1", "John", "john@usc.edu");
        user.setReputationScore(100);
        assertEquals(100, user.getReputationScore());
        user.setReputationScore(-10);
        assertEquals(-10, user.getReputationScore());
        user.setReputationScore(0);
        assertEquals(0, user.getReputationScore());
    }
    
    @Test
    public void test15_UserDefaultConstructor() {
        User user = new User();
        assertNotNull(user);
        user.setUserId("newId");
        user.setName("New Name");
        user.setEmail("new@usc.edu");
        assertEquals("newId", user.getUserId());
        assertEquals("New Name", user.getName());
        assertEquals("new@usc.edu", user.getEmail());
    }
    
    // ========== PROMPT MODEL TESTS (16-20) ==========
    
    @Test
    public void test16_PromptCreation() {
        Prompt prompt = new Prompt("prompt1", "Title", "Text", Arrays.asList("tag1"), "author1", "Author");
        assertEquals("prompt1", prompt.getPromptId());
        assertEquals("Title", prompt.getTitle());
        assertEquals("Text", prompt.getText());
        assertEquals(1, prompt.getTags().size());
        assertNotNull(prompt.getCreatedAt());
    }
    
    @Test
    public void test17_PromptWithNullTitle() {
        Prompt prompt = new Prompt("prompt1", null, "Text", null, "author1", "Author");
        assertNull(prompt.getTitle());
        assertNotNull(prompt.getTags());
    }
    
    @Test
    public void test18_PromptTagHandling() {
        Prompt prompt = new Prompt("prompt1", "Title", "Text", null, "author1", "Author");
        assertNotNull(prompt.getTags());
        List<String> tags = Arrays.asList("gpt-4", "claude");
        prompt.setTags(tags);
        assertEquals(2, prompt.getTags().size());
    }
    
    @Test
    public void test19_PromptDefaultConstructor() {
        Prompt prompt = new Prompt();
        assertNotNull(prompt);
        prompt.setPromptId("newId");
        prompt.setText("New Text");
        prompt.setTitle("New Title");
        assertEquals("newId", prompt.getPromptId());
        assertEquals("New Text", prompt.getText());
        assertEquals("New Title", prompt.getTitle());
    }
    
    @Test
    public void test20_PromptGetterSetter() {
        Prompt prompt = new Prompt();
        prompt.setTitle("New Title");
        prompt.setAuthorName("New Author");
        prompt.setAuthorId("author123");
        assertEquals("New Title", prompt.getTitle());
        assertEquals("New Author", prompt.getAuthorName());
        assertEquals("author123", prompt.getAuthorId());
    }
    
    // ========== VOTE MODEL AND VALIDATORS TESTS (21-25) ==========
    
    @Test
    public void test21_VoteCreation() {
        Vote vote = new Vote(1);
        assertEquals(1, vote.getValue());
        assertNotNull(vote.getUpdatedAt());
        Vote downvote = new Vote(-1);
        assertEquals(-1, downvote.getValue());
        assertNotNull(downvote.getUpdatedAt());
    }
    
    @Test
    public void test22_VoteDefaultConstructor() {
        Vote vote = new Vote();
        assertNotNull(vote);
        vote.setValue(1);
        assertEquals(1, vote.getValue());
        vote.setValue(-1);
        assertEquals(-1, vote.getValue());
    }
    
    @Test
    public void test23_ValidatorsUSCEmail() {
        assertTrue(Validators.isValidUSCEmail("test@usc.edu"));
        assertTrue(Validators.isValidUSCEmail("student123@usc.edu"));
        assertFalse(Validators.isValidUSCEmail("test@gmail.com"));
        assertFalse(Validators.isValidUSCEmail(null));
        assertFalse(Validators.isValidUSCEmail(""));
    }
    
    @Test
    public void test24_ValidatorsStudentIdAndPassword() {
        assertTrue(Validators.isValidStudentId("1234567890"));
        assertFalse(Validators.isValidStudentId("12345"));
        assertFalse(Validators.isValidStudentId("12345678901"));
        assertTrue(Validators.isValidPassword("123456"));
        assertFalse(Validators.isValidPassword("12345"));
        assertFalse(Validators.isValidPassword(null));
    }
    
    @Test
    public void test25_ValidatorsName() {
        assertTrue(Validators.isValidName("John Doe"));
        assertTrue(Validators.isValidName("AB"));
        assertFalse(Validators.isValidName("A"));
        assertFalse(Validators.isValidName(null));
        assertFalse(Validators.isValidName(""));
        assertFalse(Validators.isValidName("   "));
    }
}

