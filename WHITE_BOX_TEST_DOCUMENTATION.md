# White-box Test Case Documentation
## User Profile and Password Reset Functionality

---

## Test Case 1: testUserProfileFieldSettersAndGetters

### i. Location
- **Folder:** `app/src/test/java/com/example/bestllm/`
- **File:** `UserProfileAndPasswordWhiteBoxTest.java`
- **Test Method:** `testUserProfileFieldSettersAndGetters()`

### ii. Description and Execution

**Description:**
Tests the User model's setter and getter methods for all profile fields including userId, name, email, studentId, bio, reputationScore, and upvotableStatus. Verifies that data can be correctly stored and retrieved from the User object.

**How to Execute:**
1. Open Android Studio
2. Navigate to `app/src/test/java/com/example/bestllm/UserProfileAndPasswordWhiteBoxTest.java`
3. Right-click on the `testUserProfileFieldSettersAndGetters()` method
4. Select "Run 'testUserProfileFieldSettersAndGetters()'"
5. This is a JUnit test (not instrumented), so it runs on the local JVM - no emulator or device needed
6. Test results appear instantly in the Run panel

**Code Coverage:**
- **Statement Coverage:** 100% of all setter and getter methods in User model
- **Branch Coverage:** N/A (simple accessor methods have no conditional branches)
- **Path Coverage:** All setter→getter paths tested for each field

### iii. Result

**Test Result:** ✅ **PASS**

The test successfully verifies:
- All getter methods return the correct values that were set
- All setter methods correctly update field values
- Field values persist accurately in the User object without data loss or corruption
- No unexpected type conversions or data transformations occur

**Specific Verifications:**
- userId: "user123" → "newUser456" ✓
- name: "John Doe" → "Jane Smith" ✓
- email: "john@usc.edu" → "jane@usc.edu" ✓
- studentId: "1234567890" → "9876543210" ✓
- bio: "Computer Science student at USC" → "Updated bio with new information" ✓
- reputationScore: 100 → 150 ✓
- upvotableStatus: true → false ✓

### iv. Bug Discovery

**Bugs Found:** None

No bugs were discovered. All accessor methods work correctly and maintain data integrity. The User model properly encapsulates all profile data with functioning getters and setters that are used throughout the application for profile management.

---

## Test Case 2: testBirthDateTimestampConversion

### i. Location
- **Folder:** `app/src/test/java/com/example/bestllm/`
- **File:** `UserProfileAndPasswordWhiteBoxTest.java`
- **Test Method:** `testBirthDateTimestampConversion()`

### ii. Description and Execution

**Description:**
Tests the birthDate Timestamp conversion logic used in EditProfileActivity. Verifies bidirectional conversion between Date, Calendar, and Firebase Timestamp objects, ensuring date values are preserved during storage and retrieval from Firestore. Also tests null handling and date formatting.

**How to Execute:**
1. Open Android Studio
2. Navigate to `app/src/test/java/com/example/bestllm/UserProfileAndPasswordWhiteBoxTest.java`
3. Right-click on the `testBirthDateTimestampConversion()` method
4. Select "Run 'testBirthDateTimestampConversion()'"
5. Runs as JUnit test on local JVM (no emulator needed)

**Code Coverage:**
- **Statement Coverage:** 100% of date conversion logic paths
- **Branch Coverage:** Both null and non-null birthDate branches covered
- **Path Coverage:** All conversion directions tested (Timestamp→Date→Calendar and reverse)

### iii. Result

**Test Result:** ✅ **PASS**

The test successfully verifies:
- Timestamp to Date conversion preserves date values (January 15, 2000 remains accurate)
- Date to Calendar conversion correctly extracts year, month, and day components
- Calendar to Timestamp conversion works bidirectionally (June 20, 1995 converted and verified)
- Setting birthDate to null is handled without errors
- Date formatting produces correct MM/dd/yyyy output ("03/10/2001")

**Specific Verifications:**
- Initial date (2000-01-15): Year=2000, Month=JANUARY, Day=15 ✓
- Updated date (1995-06-20): Year=1995, Month=JUNE, Day=20 ✓
- Null birthDate handling: No exceptions or errors ✓
- Date formatting: "03/10/2001" matches expected format ✓

### iv. Bug Discovery

**Bugs Found:** None

No bugs were discovered. The date conversion logic is robust and maintains data accuracy through all transformations. This is critical for the date picker functionality in EditProfileActivity and ensures birthdates are correctly stored in and retrieved from Firestore.

---

## Test Case 3: testNameValidationLogic

### i. Location
- **Folder:** `app/src/test/java/com/example/bestllm/`
- **File:** `UserProfileAndPasswordWhiteBoxTest.java`
- **Test Method:** `testNameValidationLogic()`

### ii. Description and Execution

**Description:**
Tests the `Validators.isValidName()` method which validates user names according to business rules. Verifies minimum length requirements (2 characters), proper handling of null/empty inputs, whitespace validation, and acceptance of special characters commonly found in names.

**How to Execute:**
1. Open Android Studio
2. Navigate to `app/src/test/java/com/example/bestllm/UserProfileAndPasswordWhiteBoxTest.java`
3. Right-click on the `testNameValidationLogic()` method
4. Select "Run 'testNameValidationLogic()'"
5. Runs as JUnit test on local JVM

**Code Coverage:**
- **Statement Coverage:** 100% of `Validators.isValidName()` method
- **Branch Coverage:** All conditional branches (null check, length check, whitespace check)
- **Decision Coverage:** All valid/invalid scenarios tested

### iii. Result

**Test Result:** ✅ **PASS**

The test successfully verifies:

**Valid Names (Accepted):**
- "John Doe" - standard two-word name ✓
- "Jo" - minimum length (2 characters) ✓
- "John M. Doe" - name with middle initial ✓
- "O'Brien" - name with apostrophe ✓
- "Mary-Jane" - name with hyphen ✓
- "  John  " - name with leading/trailing spaces (trimmed) ✓
- "Christopher Alexander Montgomery III" - very long name ✓
- "John2" - name with numbers ✓

**Invalid Names (Rejected):**
- "" (empty string) ✓
- null ✓
- "J" (single character, below minimum) ✓
- "   " (whitespace only) ✓
- "\t" (tab character) ✓

### iv. Bug Discovery

**Bugs Found:** None

No bugs were discovered. The validation logic correctly enforces the minimum length requirement (2 characters) while being permissive enough to accept legitimate names with special characters (hyphens, apostrophes), numbers, and multiple words. This prevents invalid profile updates while accommodating diverse naming conventions.

---

## Test Case 4: testEmailValidationForPasswordReset

### i. Location
- **Folder:** `app/src/test/java/com/example/bestllm/`
- **File:** `UserProfileAndPasswordWhiteBoxTest.java`
- **Test Method:** `testEmailValidationForPasswordReset()`

### ii. Description and Execution

**Description:**
Tests the `Validators.isValidUSCEmail()` method which validates that email addresses belong to the USC domain (@usc.edu). This validation is critical for password reset security, ensuring only USC users can reset passwords. Tests email format validation, domain verification, case sensitivity, and edge cases.

**How to Execute:**
1. Open Android Studio
2. Navigate to `app/src/test/java/com/example/bestllm/UserProfileAndPasswordWhiteBoxTest.java`
3. Right-click on the `testEmailValidationForPasswordReset()` method
4. Select "Run 'testEmailValidationForPasswordReset()'"
5. Runs as JUnit test on local JVM

**Code Coverage:**
- **Statement Coverage:** 100% of `Validators.isValidUSCEmail()` method
- **Branch Coverage:** All conditional paths (null, empty, format, domain checks)
- **Boundary Testing:** Edge cases including missing components, multiple @, spaces

### iii. Result

**Test Result:** ✅ **PASS**

The test successfully verifies:

**Valid USC Emails (Accepted):**
- "john@usc.edu" - standard format ✓
- "john123@usc.edu" - with numbers ✓
- "john.doe@usc.edu" - with dots ✓
- "john-doe@usc.edu" - with hyphens ✓
- "john_doe@usc.edu" - with underscores ✓
- "JOHN@USC.EDU" - uppercase (case-insensitive) ✓
- "JoHn@UsC.eDu" - mixed case ✓

**Invalid Emails (Rejected):**
- "" (empty string) ✓
- null ✓
- "john@gmail.com" - wrong domain ✓
- "john@uscedu.com" - wrong USC domain ✓
- "johnusc.edu" - missing @ symbol ✓
- "john@" - missing domain ✓
- "@usc.edu" - missing local part ✓
- "john doe@usc.edu" - space in email ✓
- "john@@usc.edu" - multiple @ symbols ✓

### iv. Bug Discovery

**Bugs Found:** None

No bugs were discovered. The email validation provides proper security for password reset functionality by ensuring only users with valid USC email addresses can initiate password resets. The case-insensitive matching is user-friendly while maintaining security requirements.

---

## Test Case 5: testProfileDataNullHandling

### i. Location
- **Folder:** `app/src/test/java/com/example/bestllm/`
- **File:** `UserProfileAndPasswordWhiteBoxTest.java`
- **Test Method:** `testProfileDataNullHandling()`

### ii. Description and Execution

**Description:**
Tests null handling for optional profile fields (bio, birthDate, studentId) in the User model. Verifies that required fields (userId, name, email) work correctly while optional fields can be null without causing errors. This is important for profile setup where users can skip optional fields.

**How to Execute:**
1. Open Android Studio
2. Navigate to `app/src/test/java/com/example/bestllm/UserProfileAndPasswordWhiteBoxTest.java`
3. Right-click on the `testProfileDataNullHandling()` method
4. Select "Run 'testProfileDataNullHandling()'"
5. Runs as JUnit test on local JVM

**Code Coverage:**
- **Statement Coverage:** 100% of User constructor and setter logic for optional fields
- **Branch Coverage:** All null/non-null branches for optional fields
- **Path Coverage:** All combinations of required + optional field states

### iii. Result

**Test Result:** ✅ **PASS**

The test successfully verifies:

**Required Fields (Always Set):**
- userId: "user789" correctly set ✓
- name: "Jane Doe" correctly set ✓
- email: "jane@usc.edu" correctly set ✓

**Optional Fields (Can Be Null):**
- Bio defaults to empty string ("") ✓
- birthDate can be null ✓
- studentId can be null ✓

**Null Operations:**
- Setting optional fields to null explicitly works ✓
- Setting optional fields from null to values works ✓
- Required fields work independently of optional field state ✓

**Specific Scenarios Tested:**
1. Minimal user with only required fields: No errors ✓
2. Setting optional fields to null: bio=null, birthDate=null, studentId=null ✓
3. Setting optional fields from null to values: All transitions work ✓
4. User with null optional fields: name and email still accessible ✓

### iv. Bug Discovery

**Bugs Found:** None

No bugs were discovered. The User model correctly handles optional fields with null values, allowing users to skip profile fields during setup while maintaining data integrity for required fields. This design supports the "Skip for Now" functionality in profile setup and prevents null pointer exceptions when optional data is not provided.
