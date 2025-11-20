package com.example.bestllm;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * White-box tests for prompt tag parsing logic.
 *
 * This mirrors the algorithm in CreatePromptActivity.parseTags, but is tested
 * in isolation from Android dependencies.
 *
 * Feature coverage:
 *  - Comma-separated parsing
 *  - Trimming whitespace
 *  - Removing empty entries
 *  - Removing duplicates
 */
public class PromptTagParsingWhiteBoxTest {

    /**
     * Helper method that mirrors CreatePromptActivity.parseTags(String raw).
     */
    private List<String> parseTags(String raw) {
        if (raw.isEmpty()) return new ArrayList<>();
        List<String> out = new ArrayList<>();
        for (String s : Arrays.asList(raw.split(","))) {
            String t = s.trim();
            if (!t.isEmpty() && !out.contains(t)) out.add(t);
        }
        return out;
    }

    /**
     * White-box Test Case 1:
     * Splits comma-separated tags, trims whitespace, and removes duplicates.
     */
    @Test
    public void testParseTagsSplitsAndTrimsAndRemovesDuplicates() {
        String input = " gpt-4 , claude-3, gpt-4 , llama-3 ";
        List<String> tags = parseTags(input);

        assertEquals("Should have 3 unique tags", 3, tags.size());
        assertTrue("Should contain gpt-4", tags.contains("gpt-4"));
        assertTrue("Should contain claude-3", tags.contains("claude-3"));
        assertTrue("Should contain llama-3", tags.contains("llama-3"));
    }

    /**
     * White-box Test Case 2:
     * Empty input string returns an empty list.
     */
    @Test
    public void testParseTagsEmptyStringReturnsEmptyList() {
        List<String> tags = parseTags("");

        assertNotNull("Tags list should not be null", tags);
        assertTrue("Tags list should be empty for empty input", tags.isEmpty());
    }

    /**
     * White-box Test Case 3:
     * Ignores extra commas and empty segments.
     */
    @Test
    public void testParseTagsIgnoresEmptySegmentsAndExtraCommas() {
        String input = ",, gpt-4 ,, , claude-3 , , ";
        List<String> tags = parseTags(input);

        assertEquals("Should only keep non-empty tags", 2, tags.size());
        assertTrue(tags.contains("gpt-4"));
        assertTrue(tags.contains("claude-3"));
    }
}
