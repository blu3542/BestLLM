package com.example.bestllm;

import com.example.bestllm.models.Comment;
import com.example.bestllm.models.Post;
import com.example.bestllm.models.Prompt;
import com.example.bestllm.models.User;
import com.example.bestllm.models.Vote;
import com.google.firebase.Timestamp;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Comprehensive Model Coverage Tests
 * 
 * Tests ALL getters and setters to achieve maximum coverage
 * This should push models package from 64% method coverage to near 100%
 */
public class ComprehensiveModelCoverageTest {
    
    // ========== POST MODEL - ALL GETTERS/SETTERS ==========
    
    @Test
    public void testPostAllGettersAndSetters() {
        Post post = new Post();
        
        // Test all setters and getters
        post.setPostId("post123");
        assertEquals("post123", post.getPostId());
        
        post.setTitle("Test Title");
        assertEquals("Test Title", post.getTitle());
        
        post.setBody("Test Body");
        assertEquals("Test Body", post.getBody());
        
        List<String> tags = Arrays.asList("tag1", "tag2");
        post.setTags(tags);
        assertEquals(tags, post.getTags());
        
        post.setAuthorId("author123");
        assertEquals("author123", post.getAuthorId());
        
        post.setAuthorName("Author Name");
        assertEquals("Author Name", post.getAuthorName());
        
        post.setUpvotes(10);
        assertEquals(10, post.getUpvotes());
        
        post.setDownvotes(5);
        assertEquals(5, post.getDownvotes());
        
        post.setCommentCount(3);
        assertEquals(3, post.getCommentCount());
        
        Timestamp now = Timestamp.now();
        post.setCreatedAt(now);
        assertEquals(now, post.getCreatedAt());
        
        post.setUpdatedAt(now);
        assertEquals(now, post.getUpdatedAt());
    }
    
    @Test
    public void testPostNetVotesEdgeCases() {
        Post post = new Post();
        post.setUpvotes(0);
        post.setDownvotes(0);
        assertEquals(0, post.getNetVotes());
        
        post.setUpvotes(100);
        post.setDownvotes(0);
        assertEquals(100, post.getNetVotes());
        
        post.setUpvotes(0);
        post.setDownvotes(50);
        assertEquals(-50, post.getNetVotes());
    }
    
    // ========== COMMENT MODEL - ALL GETTERS/SETTERS ==========
    
    @Test
    public void testCommentAllGettersAndSetters() {
        Comment comment = new Comment();
        
        comment.setCommentId("comment123");
        assertEquals("comment123", comment.getCommentId());
        
        comment.setPostId("post123");
        assertEquals("post123", comment.getPostId());
        
        comment.setAuthorId("author123");
        assertEquals("author123", comment.getAuthorId());
        
        comment.setAuthorName("Author Name");
        assertEquals("Author Name", comment.getAuthorName());
        
        comment.setTitle("Comment Title");
        assertEquals("Comment Title", comment.getTitle());
        
        comment.setBody("Comment Body");
        assertEquals("Comment Body", comment.getBody());
        
        comment.setUpvotes(5);
        assertEquals(5, comment.getUpvotes());
        
        comment.setDownvotes(2);
        assertEquals(2, comment.getDownvotes());
        
        Timestamp now = Timestamp.now();
        comment.setCreatedAt(now);
        assertEquals(now, comment.getCreatedAt());
        
        comment.setUpdatedAt(now);
        assertEquals(now, comment.getUpdatedAt());
    }
    
    @Test
    public void testCommentNetVotesEdgeCases() {
        Comment comment = new Comment();
        comment.setUpvotes(0);
        comment.setDownvotes(0);
        assertEquals(0, comment.getNetVotes());
        
        comment.setUpvotes(20);
        comment.setDownvotes(10);
        assertEquals(10, comment.getNetVotes());
    }
    
    @Test
    public void testCommentTitleNullAndEmpty() {
        Comment comment = new Comment();
        comment.setTitle(null);
        assertNull(comment.getTitle());
        
        comment.setTitle("");
        assertEquals("", comment.getTitle());
        
        comment.setTitle("Valid Title");
        assertEquals("Valid Title", comment.getTitle());
    }
    
    // ========== USER MODEL - ALL GETTERS/SETTERS ==========
    
    @Test
    public void testUserAllGettersAndSetters() {
        User user = new User();
        
        user.setUserId("user123");
        assertEquals("user123", user.getUserId());
        
        user.setName("User Name");
        assertEquals("User Name", user.getName());
        
        user.setEmail("user@usc.edu");
        assertEquals("user@usc.edu", user.getEmail());
        
        user.setStudentId("1234567890");
        assertEquals("1234567890", user.getStudentId());
        
        user.setDepartment("Computer Science");
        assertEquals("Computer Science", user.getDepartment());
        
        user.setSchool("Viterbi");
        assertEquals("Viterbi", user.getSchool());
        
        user.setBio("User bio");
        assertEquals("User bio", user.getBio());
        
        Timestamp birthDate = Timestamp.now();
        user.setBirthDate(birthDate);
        assertEquals(birthDate, user.getBirthDate());
        
        user.setReputationScore(50);
        assertEquals(50, user.getReputationScore());
        
        user.setUpvotableStatus(false);
        assertFalse(user.isUpvotableStatus());
        
        user.setUpvotableStatus(true);
        assertTrue(user.isUpvotableStatus());
        
        Timestamp createdAt = Timestamp.now();
        user.setCreatedAt(createdAt);
        assertEquals(createdAt, user.getCreatedAt());
    }
    
    @Test
    public void testUserReputationScoreRange() {
        User user = new User();
        user.setReputationScore(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, user.getReputationScore());
        
        user.setReputationScore(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, user.getReputationScore());
        
        user.setReputationScore(0);
        assertEquals(0, user.getReputationScore());
    }
    
    // ========== PROMPT MODEL - ALL GETTERS/SETTERS ==========
    
    @Test
    public void testPromptAllGettersAndSetters() {
        Prompt prompt = new Prompt();
        
        prompt.setPromptId("prompt123");
        assertEquals("prompt123", prompt.getPromptId());
        
        prompt.setTitle("Prompt Title");
        assertEquals("Prompt Title", prompt.getTitle());
        
        prompt.setText("Prompt Text");
        assertEquals("Prompt Text", prompt.getText());
        
        List<String> tags = Arrays.asList("gpt-4", "claude");
        prompt.setTags(tags);
        assertEquals(tags, prompt.getTags());
        
        prompt.setAuthorId("author123");
        assertEquals("author123", prompt.getAuthorId());
        
        prompt.setAuthorName("Author Name");
        assertEquals("Author Name", prompt.getAuthorName());
        
        Timestamp now = Timestamp.now();
        prompt.setCreatedAt(now);
        assertEquals(now, prompt.getCreatedAt());
        
        prompt.setUpdatedAt(now);
        assertEquals(now, prompt.getUpdatedAt());
    }
    
    @Test
    public void testPromptNullTitle() {
        Prompt prompt = new Prompt();
        prompt.setTitle(null);
        assertNull(prompt.getTitle());
        
        prompt.setTitle("Valid Title");
        assertEquals("Valid Title", prompt.getTitle());
    }
    
    @Test
    public void testPromptEmptyTags() {
        Prompt prompt = new Prompt();
        assertNotNull(prompt.getTags());
        assertTrue(prompt.getTags().isEmpty());
        
        prompt.setTags(Arrays.asList("tag1"));
        assertEquals(1, prompt.getTags().size());
    }
    
    // ========== VOTE MODEL - ALL GETTERS/SETTERS ==========
    
    @Test
    public void testVoteAllGettersAndSetters() {
        Vote vote = new Vote();
        
        vote.setValue(1);
        assertEquals(1, vote.getValue());
        
        vote.setValue(-1);
        assertEquals(-1, vote.getValue());
        
        Timestamp now = Timestamp.now();
        vote.setUpdatedAt(now);
        assertEquals(now, vote.getUpdatedAt());
    }
    
    @Test
    public void testVoteValueRange() {
        Vote vote = new Vote();
        vote.setValue(1);
        assertEquals(1, vote.getValue());
        
        vote.setValue(-1);
        assertEquals(-1, vote.getValue());
    }
}

