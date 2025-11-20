package com.example.bestllm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.example.bestllm.models.Vote;
import com.google.firebase.Timestamp;

import org.junit.Test;

/**
 * White-box tests for the Vote model.
 *
 * Feature coverage:
 *  - Default and parameterized constructors
 *  - Value and timestamp behavior
 */
public class VoteModelWhiteBoxTest {

    /**
     * White-box Test Case 1: Default constructor leaves value at 0 and timestamp null.
     */
    @Test
    public void testDefaultConstructorHasZeroValueAndNullTimestamp() {
        Vote vote = new Vote();

        assertEquals("Default value should be 0", 0, vote.getValue());
        assertNull("Default timestamp should be null", vote.getUpdatedAt());
    }

    /**
     * White-box Test Case 2: Parameterized constructor sets value and timestamp.
     */
    @Test
    public void testParameterizedConstructorSetsValueAndTimestamp() {
        Vote vote = new Vote(1);

        assertEquals("Value should be set from constructor", 1, vote.getValue());
        assertNotNull("Timestamp should be set in constructor", vote.getUpdatedAt());
    }

    /**
     * White-box Test Case 3: Setters update vote fields.
     */
    @Test
    public void testSettersUpdateVoteFields() {
        Vote vote = new Vote(1);
        Timestamp now = Timestamp.now();

        vote.setValue(-1);
        vote.setUpdatedAt(now);

        assertEquals("Updated value should be -1", -1, vote.getValue());
        assertEquals("Updated timestamp should match", now, vote.getUpdatedAt());
    }
}
