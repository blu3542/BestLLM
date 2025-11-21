package com.example.bestllm;

import com.example.bestllm.models.Post;
import com.google.firebase.Timestamp;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * White-box test cases for Post model
 * 
 * Coverage: Constructor, getters, setters, utility methods, edge cases
 */
public class PostModelWhiteBoxTest {
    
    private Post post;
    private static final String POST_ID = "post123";
    private static final String TITLE = "Test Post Title";
    private static final String BODY = "Test post body content";
    private static final String AUTHOR_ID = "author123";
    private static final String AUTHOR_NAME = "Test Author";
    private static final List<String> TAGS = Arrays.asList("tag1", "tag2", "tag3");

    @Before
    public void setUp() {
        post = new Post(POST_ID, TITLE, BODY, TAGS, AUTHOR_ID, AUTHOR_NAME);
    }

    /**
     * White-box Test Case 1: testPostCreationWithAllFields
     * Location: app/src/test/java/com/example/bestllm/PostModelWhiteBoxTest.java
     * Description: Tests Post constructor initializes all fields correctly
     * Rationale: Verifies constructor logic sets all fields including tags list handling
     * Result: PASS - All fields initialized correctly
     */
    @Test
    public void testPostCreationWithAllFields() {
        assertNotNull("Post should not be null", post);
        assertEquals("Post ID should match", POST_ID, post.getPostId());
        assertEquals("Title should match", TITLE, post.getTitle());
        assertEquals("Body should match", BODY, post.getBody());
        assertEquals("Author ID should match", AUTHOR_ID, post.getAuthorId());
        assertEquals("Author name should match", AUTHOR_NAME, post.getAuthorName());
        assertEquals("Tags should match", TAGS, post.getTags());
        assertEquals("Upvotes should be 0", 0, post.getUpvotes());
        assertEquals("Downvotes should be 0", 0, post.getDownvotes());
        assertEquals("Comment count should be 0", 0, post.getCommentCount());
        assertNotNull("CreatedAt should not be null", post.getCreatedAt());
        assertNotNull("UpdatedAt should not be null", post.getUpdatedAt());
    }

    /**
     * White-box Test Case 2: testPostCreationWithNullTags
     * Location: app/src/test/java/com/example/bestllm/PostModelWhiteBoxTest.java
     * Description: Tests Post constructor handles null tags by creating empty list
     * Rationale: Verifies branch coverage for null tag handling in constructor
     * Result: PASS - Null tags converted to empty list
     */
    @Test
    public void testPostCreationWithNullTags() {
        Post postNoTags = new Post(POST_ID, TITLE, BODY, null, AUTHOR_ID, AUTHOR_NAME);
        assertNotNull("Tags should not be null", postNoTags.getTags());
        assertTrue("Tags should be empty list", postNoTags.getTags().isEmpty());
    }

    /**
     * White-box Test Case 3: testPostDefaultConstructor
     * Location: app/src/test/java/com/example/bestllm/PostModelWhiteBoxTest.java
     * Description: Tests default constructor creates Post with empty tags list
     * Rationale: Verifies Firestore deserialization support
     * Result: PASS - Default constructor initializes empty tags list
     */
    @Test
    public void testPostDefaultConstructor() {
        Post emptyPost = new Post();
        assertNotNull("Post should not be null", emptyPost);
        assertNotNull("Tags should not be null", emptyPost.getTags());
        assertTrue("Tags should be empty", emptyPost.getTags().isEmpty());
        
        // Test setters work after default construction
        emptyPost.setPostId("newId");
        emptyPost.setTitle("New Title");
        emptyPost.setTags(Arrays.asList("tag1"));
        assertEquals("Post ID should be set", "newId", emptyPost.getPostId());
        assertEquals("Title should be set", "New Title", emptyPost.getTitle());
        assertEquals("Tags should be set", 1, emptyPost.getTags().size());
    }

    /**
     * White-box Test Case 4: testGetNetVotes
     * Location: app/src/test/java/com/example/bestllm/PostModelWhiteBoxTest.java
     * Description: Tests getNetVotes() calculates correctly for all vote scenarios
     * Rationale: Verifies vote calculation logic branches (positive, negative, zero)
     * Result: PASS - Net votes calculated correctly
     */
    @Test
    public void testGetNetVotes() {
        // Test positive net votes
        post.setUpvotes(10);
        post.setDownvotes(3);
        assertEquals("Net votes should be 7", 7, post.getNetVotes());
        
        // Test negative net votes
        post.setUpvotes(2);
        post.setDownvotes(8);
        assertEquals("Net votes should be -6", -6, post.getNetVotes());
        
        // Test zero net votes
        post.setUpvotes(5);
        post.setDownvotes(5);
        assertEquals("Net votes should be 0", 0, post.getNetVotes());
        
        // Test both zero
        post.setUpvotes(0);
        post.setDownvotes(0);
        assertEquals("Net votes should be 0", 0, post.getNetVotes());
    }

    /**
     * White-box Test Case 5: testPostGettersAndSetters
     * Location: app/src/test/java/com/example/bestllm/PostModelWhiteBoxTest.java
     * Description: Tests all getter and setter methods for Post fields
     * Rationale: Ensures all fields can be accessed and modified correctly
     * Result: PASS - All getters and setters work correctly
     */
    @Test
    public void testPostGettersAndSetters() {
        // Test title
        String newTitle = "Updated Title";
        post.setTitle(newTitle);
        assertEquals("Title should be updated", newTitle, post.getTitle());
        
        // Test body
        String newBody = "Updated body";
        post.setBody(newBody);
        assertEquals("Body should be updated", newBody, post.getBody());
        
        // Test tags
        List<String> newTags = Arrays.asList("newTag1", "newTag2");
        post.setTags(newTags);
        assertEquals("Tags should be updated", newTags, post.getTags());
        
        // Test comment count
        post.setCommentCount(5);
        assertEquals("Comment count should be 5", 5, post.getCommentCount());
        
        // Test timestamps
        Timestamp newTimestamp = Timestamp.now();
        post.setCreatedAt(newTimestamp);
        post.setUpdatedAt(newTimestamp);
        assertEquals("CreatedAt should be updated", newTimestamp, post.getCreatedAt());
        assertEquals("UpdatedAt should be updated", newTimestamp, post.getUpdatedAt());
    }
}

