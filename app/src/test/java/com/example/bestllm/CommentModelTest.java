package com.example.bestllm;

import com.example.bestllm.models.Comment;
import com.google.firebase.Timestamp;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * White-box test cases for Comment model
 * 
 * These tests verify the internal logic and structure of the Comment model class.
 * Coverage: Constructor, getters, setters, and calculation methods.
 */
public class CommentModelTest {
    
    private Comment comment;
    private static final String COMMENT_ID = "comment123";
    private static final String POST_ID = "post123";
    private static final String AUTHOR_ID = "author123";
    private static final String AUTHOR_NAME = "Test User";
    private static final String TITLE = "Test Comment Title";
    private static final String BODY = "Test comment body content";

    @Before
    public void setUp() {
        comment = new Comment(COMMENT_ID, POST_ID, AUTHOR_ID, AUTHOR_NAME, TITLE, BODY);
    }

    /**
     * White-box Test Case 1: testCommentCreationWithTitle
     * Location: app/src/test/java/com/example/bestllm/CommentModelTest.java
     * Description: Tests that a Comment object can be created with all fields including optional title
     * Rationale: Verifies the constructor properly initializes all fields, especially the optional title field.
     *             Tests branch coverage of constructor with title parameter.
     * Result: PASS - Comment is created with title field set correctly
     */
    @Test
    public void testCommentCreationWithTitle() {
        // Test that comment is created with title
        assertNotNull("Comment should not be null", comment);
        assertEquals("Comment ID should match", COMMENT_ID, comment.getCommentId());
        assertEquals("Post ID should match", POST_ID, comment.getPostId());
        assertEquals("Author ID should match", AUTHOR_ID, comment.getAuthorId());
        assertEquals("Author name should match", AUTHOR_NAME, comment.getAuthorName());
        assertEquals("Title should match", TITLE, comment.getTitle());
        assertEquals("Body should match", BODY, comment.getBody());
        assertEquals("Upvotes should be 0 initially", 0, comment.getUpvotes());
        assertEquals("Downvotes should be 0 initially", 0, comment.getDownvotes());
        assertNotNull("CreatedAt should not be null", comment.getCreatedAt());
        assertNotNull("UpdatedAt should not be null", comment.getUpdatedAt());
    }

    /**
     * White-box Test Case 2: testCommentCreationWithoutTitle
     * Location: app/src/test/java/com/example/bestllm/CommentModelTest.java
     * Description: Tests that a Comment can be created with null title (optional field)
     * Rationale: Verifies the optional title feature works correctly - title can be null.
     *             Tests branch coverage for null title handling.
     * Result: PASS - Comment is created successfully with null title
     */
    @Test
    public void testCommentCreationWithoutTitle() {
        Comment commentNoTitle = new Comment(COMMENT_ID, POST_ID, AUTHOR_ID, AUTHOR_NAME, null, BODY);
        assertNotNull("Comment should not be null", commentNoTitle);
        assertNull("Title should be null", commentNoTitle.getTitle());
        assertEquals("Body should match", BODY, commentNoTitle.getBody());
    }

    /**
     * White-box Test Case 3: testCommentTitleGetterSetter
     * Location: app/src/test/java/com/example/bestllm/CommentModelTest.java
     * Description: Tests title getter and setter methods specifically for optional title field
     * Rationale: Ensures title field can be properly accessed and modified, testing the optional field handling.
     *             Covers getTitle() and setTitle() method paths.
     * Result: PASS - Title getter and setter work correctly
     */
    @Test
    public void testCommentTitleGetterSetter() {
        // Test title getter returns initial value
        assertEquals("Initial title should match", TITLE, comment.getTitle());
        
        // Test title setter with new value
        String newTitle = "Updated Title";
        comment.setTitle(newTitle);
        assertEquals("Title should be updated", newTitle, comment.getTitle());
        
        // Test title setter with null (optional field)
        comment.setTitle(null);
        assertNull("Title should be null after setting to null", comment.getTitle());
        
        // Test title setter with empty string
        comment.setTitle("");
        assertEquals("Title should be empty string", "", comment.getTitle());
    }

    /**
     * White-box Test Case 4: testGetNetVotes
     * Location: app/src/test/java/com/example/bestllm/CommentModelTest.java
     * Description: Tests the getNetVotes() method calculates correct net votes (upvotes - downvotes)
     * Rationale: Verifies the vote calculation logic works correctly for different vote scenarios.
     *             Tests all branches: positive net, negative net, zero net, and edge cases.
     * Result: PASS - Net votes calculated correctly for all scenarios
     */
    @Test
    public void testGetNetVotes() {
        // Test with equal upvotes and downvotes
        comment.setUpvotes(5);
        comment.setDownvotes(5);
        assertEquals("Net votes should be 0", 0, comment.getNetVotes());
        
        // Test with more upvotes (positive net)
        comment.setUpvotes(10);
        comment.setDownvotes(3);
        assertEquals("Net votes should be 7", 7, comment.getNetVotes());
        
        // Test with more downvotes (negative net)
        comment.setUpvotes(2);
        comment.setDownvotes(8);
        assertEquals("Net votes should be -6", -6, comment.getNetVotes());
        
        // Test with zero votes
        comment.setUpvotes(0);
        comment.setDownvotes(0);
        assertEquals("Net votes should be 0", 0, comment.getNetVotes());
    }

    /**
     * White-box Test Case 5: testCommentDefaultConstructor
     * Location: app/src/test/java/com/example/bestllm/CommentModelTest.java
     * Description: Tests that the default constructor creates an empty Comment object and fields can be set
     * Rationale: Verifies Firestore can deserialize Comment objects using default constructor.
     *             Tests that all fields start as null and can be set via setters.
     * Result: PASS - Default constructor creates valid Comment object
     */
    @Test
    public void testCommentDefaultConstructor() {
        Comment emptyComment = new Comment();
        assertNotNull("Comment should not be null", emptyComment);
        assertNull("Comment ID should be null initially", emptyComment.getCommentId());
        assertNull("Title should be null initially", emptyComment.getTitle());
        assertNull("Body should be null initially", emptyComment.getBody());
        
        // Test that we can set values after creation
        emptyComment.setCommentId("newId");
        emptyComment.setTitle("New Title");
        emptyComment.setBody("New Body");
        assertEquals("Comment ID should be set", "newId", emptyComment.getCommentId());
        assertEquals("Title should be set", "New Title", emptyComment.getTitle());
        assertEquals("Body should be set", "New Body", emptyComment.getBody());
    }
}

