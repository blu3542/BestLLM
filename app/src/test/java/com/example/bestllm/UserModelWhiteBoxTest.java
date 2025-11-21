package com.example.bestllm;

import com.example.bestllm.models.User;
import com.google.firebase.Timestamp;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * White-box test cases for User model
 * 
 * Coverage: Constructor, getters, setters, default values
 */
public class UserModelWhiteBoxTest {
    
    private User user;
    private static final String USER_ID = "user123";
    private static final String NAME = "Test User";
    private static final String EMAIL = "test@usc.edu";

    @Before
    public void setUp() {
        user = new User(USER_ID, NAME, EMAIL);
    }

    /**
     * White-box Test Case 1: testUserCreationWithRequiredFields
     * Location: app/src/test/java/com/example/bestllm/UserModelWhiteBoxTest.java
     * Description: Tests User constructor initializes required fields and defaults
     * Rationale: Verifies constructor sets required fields and initializes defaults correctly
     * Result: PASS - User created with correct defaults
     */
    @Test
    public void testUserCreationWithRequiredFields() {
        assertNotNull("User should not be null", user);
        assertEquals("User ID should match", USER_ID, user.getUserId());
        assertEquals("Name should match", NAME, user.getName());
        assertEquals("Email should match", EMAIL, user.getEmail());
        assertEquals("Bio should be empty string", "", user.getBio());
        assertEquals("Reputation score should be 0", 0, user.getReputationScore());
        assertTrue("Upvotable status should be true", user.isUpvotableStatus());
        assertNotNull("CreatedAt should not be null", user.getCreatedAt());
    }

    /**
     * White-box Test Case 2: testUserDefaultConstructor
     * Location: app/src/test/java/com/example/bestllm/UserModelWhiteBoxTest.java
     * Description: Tests default constructor creates empty User object
     * Rationale: Verifies Firestore deserialization support
     * Result: PASS - Default constructor creates valid User
     */
    @Test
    public void testUserDefaultConstructor() {
        User emptyUser = new User();
        assertNotNull("User should not be null", emptyUser);
        assertNull("User ID should be null", emptyUser.getUserId());
        assertNull("Name should be null", emptyUser.getName());
        assertNull("Email should be null", emptyUser.getEmail());
    }

    /**
     * White-box Test Case 3: testUserStudentIdGetterSetter
     * Location: app/src/test/java/com/example/bestllm/UserModelWhiteBoxTest.java
     * Description: Tests Student ID getter and setter methods
     * Rationale: Verifies Student ID field can be set and retrieved
     * Result: PASS - Student ID getter/setter work correctly
     */
    @Test
    public void testUserStudentIdGetterSetter() {
        String studentId = "1234567890";
        user.setStudentId(studentId);
        assertEquals("Student ID should match", studentId, user.getStudentId());
        
        // Test null
        user.setStudentId(null);
        assertNull("Student ID should be null", user.getStudentId());
    }

    /**
     * White-box Test Case 4: testUserProfileFields
     * Location: app/src/test/java/com/example/bestllm/UserModelWhiteBoxTest.java
     * Description: Tests profile-related fields (department, school, bio)
     * Rationale: Verifies all profile fields can be set and retrieved
     * Result: PASS - Profile fields work correctly
     */
    @Test
    public void testUserProfileFields() {
        // Test department
        String department = "Computer Science";
        user.setDepartment(department);
        assertEquals("Department should match", department, user.getDepartment());
        
        // Test school
        String school = "Viterbi";
        user.setSchool(school);
        assertEquals("School should match", school, user.getSchool());
        
        // Test bio
        String bio = "Test bio";
        user.setBio(bio);
        assertEquals("Bio should match", bio, user.getBio());
    }

    /**
     * White-box Test Case 5: testUserReputationAndStatus
     * Location: app/src/test/java/com/example/bestllm/UserModelWhiteBoxTest.java
     * Description: Tests reputation score and upvotable status fields
     * Rationale: Verifies reputation and status fields work correctly
     * Result: PASS - Reputation and status fields work correctly
     */
    @Test
    public void testUserReputationAndStatus() {
        // Test reputation score
        user.setReputationScore(100);
        assertEquals("Reputation score should be 100", 100, user.getReputationScore());
        
        // Test negative reputation
        user.setReputationScore(-10);
        assertEquals("Reputation score should be -10", -10, user.getReputationScore());
        
        // Test upvotable status
        user.setUpvotableStatus(false);
        assertFalse("Upvotable status should be false", user.isUpvotableStatus());
        
        user.setUpvotableStatus(true);
        assertTrue("Upvotable status should be true", user.isUpvotableStatus());
    }
}

