package com.example.bestllm;

import com.example.bestllm.models.User;
import com.example.bestllm.utils.Validators;
import com.google.firebase.Timestamp;

import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * White-box test cases for User Profile Setup, Update, and Password Reset functionality
 *
 * These tests verify the internal logic of profile management and password reset features including
 * User model setters/getters, validation logic, and data transformation methods.
 * Coverage: User model methods, profile validation, date handling, and field constraints.
 */
public class UserProfileAndPasswordWhiteBoxTest {

    private User testUser;
    private SimpleDateFormat dateFormat;

    @Before
    public void setUp() {
        // Create a test user with complete profile data
        testUser = new User("user123", "John Doe", "john@usc.edu");
        testUser.setStudentId("1234567890");
        testUser.setBio("Computer Science student at USC");

        Calendar birthDate = Calendar.getInstance();
        birthDate.set(2000, Calendar.JANUARY, 15);
        testUser.setBirthDate(new Timestamp(birthDate.getTime()));

        testUser.setReputationScore(100);
        testUser.setUpvotableStatus(true);
        testUser.setCreatedAt(Timestamp.now());

        dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
    }

    /**
     * White-box Test Case 1: testUserProfileFieldSettersAndGetters
     * Location: app/src/test/java/com/example/bestllm/UserProfileAndPasswordWhiteBoxTest.java
     * Description: Tests User model's setter and getter methods for profile fields
     * Rationale: Verifies that all profile data fields (name, bio, email, studentId, birthDate)
     *            can be correctly set and retrieved. Tests the fundamental data access methods
     *            used throughout the profile management features.
     * Result: PASS - All setters and getters work correctly
     */
    @Test
    public void testUserProfileFieldSettersAndGetters() {
        // Test userId
        assertEquals("UserId should match", "user123", testUser.getUserId());
        testUser.setUserId("newUser456");
        assertEquals("Updated userId should match", "newUser456", testUser.getUserId());

        // Test name
        assertEquals("Name should match", "John Doe", testUser.getName());
        testUser.setName("Jane Smith");
        assertEquals("Updated name should match", "Jane Smith", testUser.getName());

        // Test email
        assertEquals("Email should match", "john@usc.edu", testUser.getEmail());
        testUser.setEmail("jane@usc.edu");
        assertEquals("Updated email should match", "jane@usc.edu", testUser.getEmail());

        // Test studentId
        assertEquals("StudentId should match", "1234567890", testUser.getStudentId());
        testUser.setStudentId("9876543210");
        assertEquals("Updated studentId should match", "9876543210", testUser.getStudentId());

        // Test bio
        assertEquals("Bio should match", "Computer Science student at USC", testUser.getBio());
        testUser.setBio("Updated bio with new information");
        assertEquals("Updated bio should match", "Updated bio with new information", testUser.getBio());

        // Test reputationScore
        assertEquals("ReputationScore should match", 100, testUser.getReputationScore());
        testUser.setReputationScore(150);
        assertEquals("Updated reputationScore should match", 150, testUser.getReputationScore());

        // Test upvotableStatus
        assertTrue("UpvotableStatus should be true", testUser.isUpvotableStatus());
        testUser.setUpvotableStatus(false);
        assertFalse("Updated upvotableStatus should be false", testUser.isUpvotableStatus());
    }

    /**
     * White-box Test Case 2: testBirthDateTimestampConversion
     * Location: app/src/test/java/com/example/bestllm/UserProfileAndPasswordWhiteBoxTest.java
     * Description: Tests the birthDate Timestamp conversion logic used in EditProfileActivity
     * Rationale: Verifies that birthDate can be correctly converted between Date, Calendar, and Timestamp.
     *            This is critical for the date picker functionality and Firestore storage.
     *            Tests the internal date handling logic that ensures data consistency.
     * Result: PASS - Date conversion works correctly in all directions
     */
    @Test
    public void testBirthDateTimestampConversion() {
        // Test initial birthDate
        assertNotNull("BirthDate should not be null", testUser.getBirthDate());
        Date birthDate = testUser.getBirthDate().toDate();

        // Verify date conversion to Calendar
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(birthDate);
        assertEquals("Year should be 2000", 2000, calendar.get(Calendar.YEAR));
        assertEquals("Month should be January", Calendar.JANUARY, calendar.get(Calendar.MONTH));
        assertEquals("Day should be 15", 15, calendar.get(Calendar.DAY_OF_MONTH));

        // Test setting new birthDate from Calendar
        Calendar newBirthDate = Calendar.getInstance();
        newBirthDate.set(1995, Calendar.JUNE, 20);
        testUser.setBirthDate(new Timestamp(newBirthDate.getTime()));

        Date updatedDate = testUser.getBirthDate().toDate();
        Calendar updatedCalendar = Calendar.getInstance();
        updatedCalendar.setTime(updatedDate);

        assertEquals("Updated year should be 1995", 1995, updatedCalendar.get(Calendar.YEAR));
        assertEquals("Updated month should be June", Calendar.JUNE, updatedCalendar.get(Calendar.MONTH));
        assertEquals("Updated day should be 20", 20, updatedCalendar.get(Calendar.DAY_OF_MONTH));

        // Test null birthDate handling
        testUser.setBirthDate(null);
        assertNull("BirthDate should be null", testUser.getBirthDate());

        // Test formatting date for display
        newBirthDate.set(2001, Calendar.MARCH, 10);
        String formattedDate = dateFormat.format(newBirthDate.getTime());
        assertEquals("Date should be formatted as MM/dd/yyyy", "03/10/2001", formattedDate);
    }

    /**
     * White-box Test Case 3: testNameValidationLogic
     * Location: app/src/test/java/com/example/bestllm/UserProfileAndPasswordWhiteBoxTest.java
     * Description: Tests the name validation logic used in profile update
     * Rationale: Verifies that Validators.isValidName() correctly validates names according to business rules.
     *            Tests minimum length requirement (2 characters), empty/null handling, and whitespace.
     *            This validation prevents invalid profile updates and maintains data quality.
     * Result: PASS - Name validation correctly accepts/rejects all test cases
     */
    @Test
    public void testNameValidationLogic() {
        // Test valid names
        assertTrue("Valid name should pass", Validators.isValidName("John Doe"));
        assertTrue("Two character name should pass", Validators.isValidName("Jo"));
        assertTrue("Name with middle initial should pass", Validators.isValidName("John M. Doe"));
        assertTrue("Name with special characters should pass", Validators.isValidName("O'Brien"));
        assertTrue("Name with hyphen should pass", Validators.isValidName("Mary-Jane"));

        // Test invalid names
        assertFalse("Empty name should fail", Validators.isValidName(""));
        assertFalse("Null name should fail", Validators.isValidName(null));
        assertFalse("Single character should fail", Validators.isValidName("J"));
        assertFalse("Only whitespace should fail", Validators.isValidName("   "));
        assertFalse("Tab character should fail", Validators.isValidName("\t"));

        // Test edge cases
        assertTrue("Name with leading/trailing spaces (after trim) should pass",
                   Validators.isValidName("  John  "));
        assertTrue("Very long name should pass",
                   Validators.isValidName("Christopher Alexander Montgomery III"));
        assertTrue("Name with numbers should pass", Validators.isValidName("John2"));
    }

    /**
     * White-box Test Case 4: testEmailValidationForPasswordReset
     * Location: app/src/test/java/com/example/bestllm/UserProfileAndPasswordWhiteBoxTest.java
     * Description: Tests USC email validation logic used in password reset functionality
     * Rationale: Verifies that Validators.isValidUSCEmail() correctly validates USC email addresses.
     *            Tests @usc.edu domain requirement, email format validation, and edge cases.
     *            Critical for password reset security - ensures only USC users can reset passwords.
     * Result: PASS - Email validation correctly accepts valid USC emails and rejects invalid ones
     */
    @Test
    public void testEmailValidationForPasswordReset() {
        // Test valid USC emails
        assertTrue("Valid USC email should pass", Validators.isValidUSCEmail("john@usc.edu"));
        assertTrue("USC email with numbers should pass", Validators.isValidUSCEmail("john123@usc.edu"));
        assertTrue("USC email with dots should pass", Validators.isValidUSCEmail("john.doe@usc.edu"));
        assertTrue("USC email with hyphens should pass", Validators.isValidUSCEmail("john-doe@usc.edu"));
        assertTrue("USC email with underscores should pass", Validators.isValidUSCEmail("john_doe@usc.edu"));

        // Test invalid emails
        assertFalse("Empty email should fail", Validators.isValidUSCEmail(""));
        assertFalse("Null email should fail", Validators.isValidUSCEmail(null));
        assertFalse("Non-USC email should fail", Validators.isValidUSCEmail("john@gmail.com"));
        assertFalse("Wrong USC domain should fail", Validators.isValidUSCEmail("john@uscedu.com"));
        assertFalse("Missing @ symbol should fail", Validators.isValidUSCEmail("johnusc.edu"));
        assertFalse("Missing domain should fail", Validators.isValidUSCEmail("john@"));
        assertFalse("Missing local part should fail", Validators.isValidUSCEmail("@usc.edu"));

        // Test edge cases
        assertFalse("Space in email should fail", Validators.isValidUSCEmail("john doe@usc.edu"));
        assertFalse("Multiple @ symbols should fail", Validators.isValidUSCEmail("john@@usc.edu"));
        assertTrue("Case insensitive check - uppercase should pass",
                   Validators.isValidUSCEmail("JOHN@USC.EDU"));
        assertTrue("Case insensitive check - mixed case should pass",
                   Validators.isValidUSCEmail("JoHn@UsC.eDu"));
    }

    /**
     * White-box Test Case 5: testProfileDataNullHandling
     * Location: app/src/test/java/com/example/bestllm/UserProfileAndPasswordWhiteBoxTest.java
     * Description: Tests null handling for optional profile fields (bio, birthDate)
     * Rationale: Verifies that User model correctly handles null values for optional fields.
     *            Tests that mandatory fields (userId, name, email) can be set while optional fields
     *            (bio, birthDate) remain null without causing errors. This is important for profile
     *            setup where users can skip optional fields.
     * Result: PASS - Null handling works correctly for all optional fields
     */
    @Test
    public void testProfileDataNullHandling() {
        // Create user with only required fields
        User minimalUser = new User("user789", "Jane Doe", "jane@usc.edu");

        // Verify required fields are set
        assertNotNull("UserId should not be null", minimalUser.getUserId());
        assertNotNull("Name should not be null", minimalUser.getName());
        assertNotNull("Email should not be null", minimalUser.getEmail());
        assertEquals("UserId should match", "user789", minimalUser.getUserId());
        assertEquals("Name should match", "Jane Doe", minimalUser.getName());
        assertEquals("Email should match", "jane@usc.edu", minimalUser.getEmail());

        // Verify optional fields can be null (not set in constructor)
        // Note: Default constructor sets bio to "" and createdAt to Timestamp.now()
        assertNotNull("Bio should be empty string by default", minimalUser.getBio());
        assertEquals("Bio should be empty string", "", minimalUser.getBio());

        // birthDate is not set in constructor, so it should be null
        assertNull("BirthDate should be null when not set", minimalUser.getBirthDate());
        assertNull("StudentId should be null when not set", minimalUser.getStudentId());

        // Test setting optional fields to null explicitly
        testUser.setBio(null);
        testUser.setBirthDate(null);
        testUser.setStudentId(null);

        assertNull("Bio should be null after setting to null", testUser.getBio());
        assertNull("BirthDate should be null after setting to null", testUser.getBirthDate());
        assertNull("StudentId should be null after setting to null", testUser.getStudentId());

        // Test setting optional fields from null to values
        minimalUser.setBio("New bio added later");
        minimalUser.setStudentId("1112223333");
        Calendar newDate = Calendar.getInstance();
        newDate.set(1998, Calendar.DECEMBER, 25);
        minimalUser.setBirthDate(new Timestamp(newDate.getTime()));

        assertEquals("Bio should be updated", "New bio added later", minimalUser.getBio());
        assertEquals("StudentId should be updated", "1112223333", minimalUser.getStudentId());
        assertNotNull("BirthDate should be set", minimalUser.getBirthDate());

        // Verify required fields still work with null optional fields
        User nullOptionalUser = new User("user999", "Test User", "test@usc.edu");
        nullOptionalUser.setBio(null);
        nullOptionalUser.setBirthDate(null);

        assertEquals("Name should still work", "Test User", nullOptionalUser.getName());
        assertEquals("Email should still work", "test@usc.edu", nullOptionalUser.getEmail());
    }
}
