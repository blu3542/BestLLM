package com.example.bestllm;

import com.example.bestllm.models.Comment;
import com.example.bestllm.models.Post;
import com.example.bestllm.models.Prompt;
import com.example.bestllm.models.User;
import com.example.bestllm.models.Vote;
import com.google.firebase.Timestamp;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Comprehensive White-box Test Suite - Models
 * 
 * This test file covers all model classes with critical functionality tests
 * 
 * Test Cases 1-5: Post Model
 * Test Cases 6-10: Comment Model  
 * Test Cases 11-15: User Model
 * Test Cases 16-20: Prompt Model
 * Test Cases 21-25: Vote Model
 */
public class ComprehensiveModelWhiteBoxTest {
    
    // Test Case 1: Post creation with all fields
    @Test
    public void testPostCreationWithAllFields() {
        Post post = new Post("post1", "Title", "Body", Arrays.asList("tag1", "tag2"), "author1", "Author Name");
        assertEquals("post1", post.getPostId());
        assertEquals("Title", post.getTitle());
        assertEquals("Body", post.getBody());
        assertEquals(2, post.getTags().size());
        assertEquals(0, post.getUpvotes());
        assertEquals(0, post.getDownvotes());
        assertNotNull(post.getCreatedAt());
    }
    
    // Test Case 2: Post net votes calculation
    @Test
    public void testPostNetVotesCalculation() {
        Post post = new Post("post1", "Title", "Body", null, "author1", "Author");
        post.setUpvotes(10);
        post.setDownvotes(3);
        assertEquals(7, post.getNetVotes());
    }
    
    // Test Case 3: Post with null tags
    @Test
    public void testPostWithNullTags() {
        Post post = new Post("post1", "Title", "Body", null, "author1", "Author");
        assertNotNull(post.getTags());
        assertTrue(post.getTags().isEmpty());
    }
    
    // Test Case 4: Post default constructor
    @Test
    public void testPostDefaultConstructor() {
        Post post = new Post();
        assertNotNull(post);
        assertNotNull(post.getTags());
        post.setPostId("newId");
        assertEquals("newId", post.getPostId());
    }
    
    // Test Case 5: Post tag manipulation
    @Test
    public void testPostTagManipulation() {
        Post post = new Post();
        List<String> tags = Arrays.asList("tag1", "tag2", "tag3");
        post.setTags(tags);
        assertEquals(3, post.getTags().size());
        assertEquals("tag1", post.getTags().get(0));
    }
    
    // Test Case 6: Comment creation with title
    @Test
    public void testCommentCreationWithTitle() {
        Comment comment = new Comment("comment1", "post1", "author1", "Author", "Title", "Body");
        assertEquals("comment1", comment.getCommentId());
        assertEquals("Title", comment.getTitle());
        assertEquals("Body", comment.getBody());
        assertEquals(0, comment.getUpvotes());
    }
    
    // Test Case 7: Comment creation without title (optional)
    @Test
    public void testCommentCreationWithoutTitle() {
        Comment comment = new Comment("comment1", "post1", "author1", "Author", null, "Body");
        assertNull(comment.getTitle());
        assertEquals("Body", comment.getBody());
    }
    
    // Test Case 8: Comment net votes calculation
    @Test
    public void testCommentNetVotesCalculation() {
        Comment comment = new Comment("comment1", "post1", "author1", "Author", null, "Body");
        comment.setUpvotes(5);
        comment.setDownvotes(2);
        assertEquals(3, comment.getNetVotes());
    }
    
    // Test Case 9: Comment title getter/setter
    @Test
    public void testCommentTitleGetterSetter() {
        Comment comment = new Comment("comment1", "post1", "author1", "Author", null, "Body");
        comment.setTitle("New Title");
        assertEquals("New Title", comment.getTitle());
        comment.setTitle(null);
        assertNull(comment.getTitle());
    }
    
    // Test Case 10: Comment default constructor
    @Test
    public void testCommentDefaultConstructor() {
        Comment comment = new Comment();
        assertNotNull(comment);
        comment.setCommentId("newId");
        comment.setBody("New Body");
        assertEquals("newId", comment.getCommentId());
        assertEquals("New Body", comment.getBody());
    }
    
    // Test Case 11: User creation with required fields
    @Test
    public void testUserCreationWithRequiredFields() {
        User user = new User("user1", "John Doe", "john@usc.edu");
        assertEquals("user1", user.getUserId());
        assertEquals("John Doe", user.getName());
        assertEquals("john@usc.edu", user.getEmail());
        assertEquals("", user.getBio());
        assertEquals(0, user.getReputationScore());
        assertTrue(user.isUpvotableStatus());
    }
    
    // Test Case 12: User student ID
    @Test
    public void testUserStudentId() {
        User user = new User("user1", "John", "john@usc.edu");
        user.setStudentId("1234567890");
        assertEquals("1234567890", user.getStudentId());
    }
    
    // Test Case 13: User profile fields
    @Test
    public void testUserProfileFields() {
        User user = new User("user1", "John", "john@usc.edu");
        user.setDepartment("Computer Science");
        user.setSchool("Viterbi");
        user.setBio("Test bio");
        assertEquals("Computer Science", user.getDepartment());
        assertEquals("Viterbi", user.getSchool());
        assertEquals("Test bio", user.getBio());
    }
    
    // Test Case 14: User reputation score
    @Test
    public void testUserReputationScore() {
        User user = new User("user1", "John", "john@usc.edu");
        user.setReputationScore(100);
        assertEquals(100, user.getReputationScore());
        user.setReputationScore(-10);
        assertEquals(-10, user.getReputationScore());
    }
    
    // Test Case 15: User default constructor
    @Test
    public void testUserDefaultConstructor() {
        User user = new User();
        assertNotNull(user);
        user.setUserId("newId");
        user.setName("New Name");
        assertEquals("newId", user.getUserId());
        assertEquals("New Name", user.getName());
    }
    
    // Test Case 16: Prompt creation
    @Test
    public void testPromptCreation() {
        Prompt prompt = new Prompt("prompt1", "Title", "Text", Arrays.asList("tag1"), "author1", "Author");
        assertEquals("prompt1", prompt.getPromptId());
        assertEquals("Title", prompt.getTitle());
        assertEquals("Text", prompt.getText());
        assertEquals(1, prompt.getTags().size());
    }
    
    // Test Case 17: Prompt with null title
    @Test
    public void testPromptWithNullTitle() {
        Prompt prompt = new Prompt("prompt1", null, "Text", null, "author1", "Author");
        assertNull(prompt.getTitle());
        assertNotNull(prompt.getTags());
    }
    
    // Test Case 18: Prompt tag handling
    @Test
    public void testPromptTagHandling() {
        Prompt prompt = new Prompt("prompt1", "Title", "Text", null, "author1", "Author");
        assertNotNull(prompt.getTags());
        List<String> tags = Arrays.asList("gpt-4", "claude");
        prompt.setTags(tags);
        assertEquals(2, prompt.getTags().size());
    }
    
    // Test Case 19: Prompt default constructor
    @Test
    public void testPromptDefaultConstructor() {
        Prompt prompt = new Prompt();
        assertNotNull(prompt);
        prompt.setPromptId("newId");
        prompt.setText("New Text");
        assertEquals("newId", prompt.getPromptId());
        assertEquals("New Text", prompt.getText());
    }
    
    // Test Case 20: Prompt getter/setter methods
    @Test
    public void testPromptGetterSetter() {
        Prompt prompt = new Prompt();
        prompt.setTitle("New Title");
        prompt.setAuthorName("New Author");
        assertEquals("New Title", prompt.getTitle());
        assertEquals("New Author", prompt.getAuthorName());
    }
    
    // Test Case 21: Vote creation
    @Test
    public void testVoteCreation() {
        Vote vote = new Vote(1);
        assertEquals(1, vote.getValue());
        assertNotNull(vote.getUpdatedAt());
    }
    
    // Test Case 22: Vote with downvote value
    @Test
    public void testVoteDownvote() {
        Vote vote = new Vote(-1);
        assertEquals(-1, vote.getValue());
        assertNotNull(vote.getUpdatedAt());
    }
    
    // Test Case 23: Vote default constructor
    @Test
    public void testVoteDefaultConstructor() {
        Vote vote = new Vote();
        assertNotNull(vote);
        vote.setValue(1);
        assertEquals(1, vote.getValue());
    }
    
    // Test Case 24: Vote getter/setter
    @Test
    public void testVoteGetterSetter() {
        Vote vote = new Vote(1);
        vote.setValue(-1);
        assertEquals(-1, vote.getValue());
        vote.setValue(1);
        assertEquals(1, vote.getValue());
    }
    
    // Test Case 25: Vote timestamp
    @Test
    public void testVoteTimestamp() {
        Vote vote = new Vote(1);
        assertNotNull(vote.getUpdatedAt());
        Timestamp newTime = Timestamp.now();
        vote.setUpdatedAt(newTime);
        assertEquals(newTime, vote.getUpdatedAt());
    }
}

