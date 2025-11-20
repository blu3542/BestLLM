package com.example.bestllm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.example.bestllm.models.Prompt;
import com.google.firebase.Timestamp;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * White-box tests for the Prompt model.
 *
 * Feature coverage:
 *  - Default constructor behavior
 *  - Parameterized constructor behavior
 *  - Tag list initialization
 *  - Getters and setters
 */
public class PromptModelWhiteBoxTest {

    /**
     * White-box Test Case 1: Default constructor initializes tags and timestamps.
     */
    @Test
    public void testDefaultConstructorInitializesTagsAndTimestamps() {
        Prompt prompt = new Prompt();

        assertNotNull("Tags list should be initialized", prompt.getTags());
        assertTrue("Default tags list should be empty", prompt.getTags().isEmpty());
        assertNotNull("createdAt timestamp should be initialized", prompt.getCreatedAt());
        assertNotNull("updatedAt timestamp should be initialized", prompt.getUpdatedAt());
    }

    /**
     * White-box Test Case 2: Parameterized constructor populates all fields.
     */
    @Test
    public void testParameterizedConstructorPopulatesFields() {
        List<String> tags = Arrays.asList("gpt-4", "openai");
        Prompt prompt = new Prompt(
                "prompt123",
                "Test Title",
                "Test prompt body",
                tags,
                "user123",
                "Test User"
        );

        assertEquals("Prompt ID should match", "prompt123", prompt.getPromptId());
        assertEquals("Title should match", "Test Title", prompt.getTitle());
        assertEquals("Text should match", "Test prompt body", prompt.getText());
        assertEquals("Author ID should match", "user123", prompt.getAuthorId());
        assertEquals("Author name should match", "Test User", prompt.getAuthorName());
        assertEquals("Tags should match", tags, prompt.getTags());
        assertNotNull("createdAt should be set", prompt.getCreatedAt());
        assertNotNull("updatedAt should be set", prompt.getUpdatedAt());
    }

    /**
     * White-box Test Case 3: Passing null tags yields an empty list.
     */
    @Test
    public void testParameterizedConstructorReplacesNullTagsWithEmptyList() {
        Prompt prompt = new Prompt(
                "prompt456",
                "No Tag Title",
                "No tag prompt",
                null,
                "user456",
                "Another User"
        );

        assertNotNull("Tags list should not be null when constructed with null",
                prompt.getTags());
        assertTrue("Tags list should be empty when constructed with null",
                prompt.getTags().isEmpty());
    }

    /**
     * White-box Test Case 4: Setters correctly update fields.
     */
    @Test
    public void testSettersUpdatePromptFields() {
        Prompt prompt = new Prompt();
        Timestamp now = Timestamp.now();

        prompt.setPromptId("id789");
        prompt.setTitle("Updated Title");
        prompt.setText("Updated text body");
        prompt.setTags(Collections.singletonList("claude-3"));
        prompt.setAuthorId("user789");
        prompt.setAuthorName("Updated User");
        prompt.setCreatedAt(now);
        prompt.setUpdatedAt(now);

        assertEquals("ID should be updated", "id789", prompt.getPromptId());
        assertEquals("Title should be updated", "Updated Title", prompt.getTitle());
        assertEquals("Text should be updated", "Updated text body", prompt.getText());
        assertEquals("Tags should be updated",
                Collections.singletonList("claude-3"), prompt.getTags());
        assertEquals("Author ID should be updated", "user789", prompt.getAuthorId());
        assertEquals("Author name should be updated", "Updated User", prompt.getAuthorName());
        assertEquals("createdAt should be updated", now, prompt.getCreatedAt());
        assertEquals("updatedAt should be updated", now, prompt.getUpdatedAt());
    }
}
