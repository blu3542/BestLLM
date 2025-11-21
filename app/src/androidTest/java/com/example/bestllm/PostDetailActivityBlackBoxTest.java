package com.example.bestllm;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.bestllm.ui.post.PostDetailActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Black-box test cases for PostDetailActivity using Espresso
 * 
 * Test Case 1: testPostDetailDisplaysContent
 * Location: app/src/androidTest/java/com/example/bestllm/PostDetailActivityBlackBoxTest.java
 * Description: Tests that post detail screen displays title, body, author, and tags
 * Rationale: Verifies post content is properly displayed to users
 * Input: Launch PostDetailActivity with post ID
 * Expected: Post title, body, author name, and tags are visible
 * 
 * How to execute: Run as Android Instrumented Test
 */
@RunWith(AndroidJUnit4.class)
public class PostDetailActivityBlackBoxTest {
    
    private static final String TEST_POST_ID = "test_post_id_123";
    
    @Before
    public void setUp() {
        // Setup if needed
    }

    @Test
    public void testPostDetailDisplaysContent() {
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), 
                PostDetailActivity.class);
        intent.putExtra(PostDetailActivity.EXTRA_POST_ID, TEST_POST_ID);
        
        try (ActivityScenario<PostDetailActivity> scenario = ActivityScenario.launch(intent)) {
            // Verify post title is displayed
            Espresso.onView(ViewMatchers.withId(R.id.textViewTitle))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
            
            // Verify post body is displayed
            Espresso.onView(ViewMatchers.withId(R.id.textViewBody))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
            
            // Verify author is displayed
            Espresso.onView(ViewMatchers.withId(R.id.textViewAuthor))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
            
            // Verify tags container is displayed
            Espresso.onView(ViewMatchers.withId(R.id.chipGroupTags))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        }
    }

    /**
     * Test Case 2: testVoteButtonsDisplayed
     * Location: app/src/androidTest/java/com/example/bestllm/PostDetailActivityBlackBoxTest.java
     * Description: Tests that upvote and downvote buttons are displayed
     * Rationale: Verifies voting functionality is accessible
     * Input: Launch PostDetailActivity
     * Expected: Upvote and downvote buttons are visible
     * 
     * How to execute: Run as Android Instrumented Test
     */
    @Test
    public void testVoteButtonsDisplayed() {
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), 
                PostDetailActivity.class);
        intent.putExtra(PostDetailActivity.EXTRA_POST_ID, TEST_POST_ID);
        
        try (ActivityScenario<PostDetailActivity> scenario = ActivityScenario.launch(intent)) {
            // Verify upvote button exists
            Espresso.onView(ViewMatchers.withId(R.id.btnUpvote))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
            
            // Verify downvote button exists
            Espresso.onView(ViewMatchers.withId(R.id.btnDownvote))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        }
    }

    /**
     * Test Case 3: testCommentInputFieldsDisplayed
     * Location: app/src/androidTest/java/com/example/bestllm/PostDetailActivityBlackBoxTest.java
     * Description: Tests that comment input fields (title and body) are displayed
     * Rationale: Verifies comment creation UI is accessible
     * Input: Launch PostDetailActivity
     * Expected: Comment title field, body field, and submit button are visible
     * 
     * How to execute: Run as Android Instrumented Test
     */
    @Test
    public void testCommentInputFieldsDisplayed() {
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), 
                PostDetailActivity.class);
        intent.putExtra(PostDetailActivity.EXTRA_POST_ID, TEST_POST_ID);
        
        try (ActivityScenario<PostDetailActivity> scenario = ActivityScenario.launch(intent)) {
            // Verify comment title field exists
            Espresso.onView(ViewMatchers.withId(R.id.editCommentTitle))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
            
            // Verify comment body field exists
            Espresso.onView(ViewMatchers.withId(R.id.editCommentBody))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
            
            // Verify submit comment button exists
            Espresso.onView(ViewMatchers.withId(R.id.btnSubmitComment))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        }
    }

    /**
     * Test Case 4: testCommentsListDisplayed
     * Location: app/src/androidTest/java/com/example/bestllm/PostDetailActivityBlackBoxTest.java
     * Description: Tests that comments RecyclerView is displayed
     * Rationale: Verifies comments list UI is present
     * Input: Launch PostDetailActivity
     * Expected: Comments RecyclerView is visible
     * 
     * How to execute: Run as Android Instrumented Test
     */
    @Test
    public void testCommentsListDisplayed() {
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), 
                PostDetailActivity.class);
        intent.putExtra(PostDetailActivity.EXTRA_POST_ID, TEST_POST_ID);
        
        try (ActivityScenario<PostDetailActivity> scenario = ActivityScenario.launch(intent)) {
            // Verify comments RecyclerView exists
            Espresso.onView(ViewMatchers.withId(R.id.recyclerComments))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        }
    }

    /**
     * Test Case 5: testPostVoteCountDisplayed
     * Location: app/src/androidTest/java/com/example/bestllm/PostDetailActivityBlackBoxTest.java
     * Description: Tests that vote count and comment count are displayed
     * Rationale: Verifies post statistics are shown to users
     * Input: Launch PostDetailActivity
     * Expected: Vote count and comment count text views are visible
     * 
     * How to execute: Run as Android Instrumented Test
     */
    @Test
    public void testPostVoteCountDisplayed() {
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), 
                PostDetailActivity.class);
        intent.putExtra(PostDetailActivity.EXTRA_POST_ID, TEST_POST_ID);
        
        try (ActivityScenario<PostDetailActivity> scenario = ActivityScenario.launch(intent)) {
            // Verify vote count is displayed
            Espresso.onView(ViewMatchers.withId(R.id.textViewVotes))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
            
            // Verify comment count is displayed
            Espresso.onView(ViewMatchers.withId(R.id.textViewComments))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        }
    }
}

