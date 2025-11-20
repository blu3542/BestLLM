# Black-box Test Case Documentation
## User Profile and Password Reset Functionality

---

## Test Case 1: testForgotPasswordButtonVisibility

### i. Location
- **Folder:** `app/src/androidTest/java/com/example/bestllm/`
- **File:** `UserProfileAndPasswordBlackBoxTest.java`
- **Test Method:** `testForgotPasswordButtonVisibility()`

### ii. Description and Execution

**Description:**
Tests that the "Forgot Password?" link is visible and clickable on the login screen.

**How to Execute:**
1. Open Android Studio
2. Navigate to `app/src/androidTest/java/com/example/bestllm/UserProfileAndPasswordBlackBoxTest.java`
3. Right-click on the `testForgotPasswordButtonVisibility()` method
4. Select "Run 'testForgotPasswordButtonVisibility()'"
5. Run on an Android emulator or physical device
6. The test will launch LoginActivity and verify the "Forgot Password?" link exists and is clickable

### iii. Rationale

**Why this input:**
- Input: Launch LoginActivity without any user interaction
- This tests the initial state of the login screen to ensure critical password reset functionality is discoverable

**How the test case was generated:**
- **Testing Technique:** Boundary value analysis and accessibility testing
- **Reasoning:** Users who have forgotten their password need to immediately see how to recover their account. This is a critical user flow entry point, so we test the initial state (boundary condition) where the user first sees the login screen
- **Input Selection:** No input required - we test visibility at app launch, which is when users most need to find password reset functionality

**Bug Discovery:**
No bugs were uncovered. The "Forgot Password?" link is consistently visible and clickable on the login screen.

---

## Test Case 2: testForgotPasswordDialogOpens

### i. Location
- **Folder:** `app/src/androidTest/java/com/example/bestllm/`
- **File:** `UserProfileAndPasswordBlackBoxTest.java`
- **Test Method:** `testForgotPasswordDialogOpens()`

### ii. Description and Execution

**Description:**
Tests that clicking "Forgot Password?" opens a dialog with the correct title ("Reset Password"), message ("Enter your USC email to receive a password reset link"), and action buttons ("Send Reset Link" and "Cancel").

**How to Execute:**
1. Open Android Studio
2. Navigate to `app/src/androidTest/java/com/example/bestllm/UserProfileAndPasswordBlackBoxTest.java`
3. Right-click on the `testForgotPasswordDialogOpens()` method
4. Select "Run 'testForgotPasswordDialogOpens()'"
5. Run on an Android emulator or physical device
6. The test will click the "Forgot Password?" link and verify the dialog appears with correct content

### iii. Rationale

**Why this input:**
- Input: Single click on the "Forgot Password?" link
- This tests the complete password reset dialog UI to ensure users receive clear instructions and options

**How the test case was generated:**
- **Testing Technique:** State transition testing and use case testing
- **Reasoning:** After clicking "Forgot Password?", the app should transition to a new state (dialog displayed) with all necessary information for the user to proceed. This tests the critical workflow transition from login to password reset initiation
- **Input Selection:** A single click is the natural user action, representing the most common path users take when they need to reset their password

**Bug Discovery:**
No bugs were uncovered. The dialog appears correctly with all required elements (title, message, email input field, and action buttons).

---

## Test Case 3: testForgotPasswordDialogCancellation

### i. Location
- **Folder:** `app/src/androidTest/java/com/example/bestllm/`
- **File:** `UserProfileAndPasswordBlackBoxTest.java`
- **Test Method:** `testForgotPasswordDialogCancellation()`

### ii. Description and Execution

**Description:**
Tests that clicking the "Cancel" button in the password reset dialog closes the dialog and returns the user to the login screen without performing any password reset actions.

**How to Execute:**
1. Open Android Studio
2. Navigate to `app/src/androidTest/java/com/example/bestllm/UserProfileAndPasswordBlackBoxTest.java`
3. Right-click on the `testForgotPasswordDialogCancellation()` method
4. Select "Run 'testForgotPasswordDialogCancellation()'"
5. Run on an Android emulator or physical device
6. The test will open the password reset dialog and click Cancel, verifying return to login screen

### iii. Rationale

**Why this input:**
- Input: Open dialog, then click "Cancel" button
- This tests the negative/cancellation flow to ensure users can exit the password reset process without consequences

**How the test case was generated:**
- **Testing Technique:** Negative testing and error prevention testing
- **Reasoning:** Users often open dialogs accidentally or change their mind. They must be able to cancel operations safely. This tests the "escape hatch" functionality
- **Input Selection:** The Cancel action represents user indecision or accidental clicks - a common real-world scenario that must be handled gracefully without side effects

**Bug Discovery:**
No bugs were uncovered. The Cancel button correctly dismisses the dialog and returns to the login screen without triggering any password reset operations.

---

## Test Case 4: testEditProfileActivityLaunchFromSetupMode

### i. Location
- **Folder:** `app/src/androidTest/java/com/example/bestllm/`
- **File:** `UserProfileAndPasswordBlackBoxTest.java`
- **Test Method:** `testEditProfileActivityLaunchFromSetupMode()`

### ii. Description and Execution

**Description:**
Tests that EditProfileActivity can be launched in setup mode (after new user registration) and displays all required profile fields (name, email, student ID, bio, birth date) along with the "Skip for Now" button that allows users to defer profile completion.

**How to Execute:**
1. Open Android Studio
2. Navigate to `app/src/androidTest/java/com/example/bestllm/UserProfileAndPasswordBlackBoxTest.java`
3. Right-click on the `testEditProfileActivityLaunchFromSetupMode()` method
4. Select "Run 'testEditProfileActivityLaunchFromSetupMode()'"
5. Run on an Android emulator or physical device
6. The test will launch EditProfileActivity with SETUP_MODE=true intent extra and verify UI elements

### iii. Rationale

**Why this input:**
- Input: Launch EditProfileActivity with `SETUP_MODE=true` intent extra
- This tests the new user onboarding flow where profile setup is optional

**How the test case was generated:**
- **Testing Technique:** Decision table testing and state-based testing
- **Reasoning:** The application has two distinct modes for the profile editor: setup mode (for new users) and edit mode (for existing users). These modes have different UI elements and constraints. We chose the SETUP_MODE=true input to test the new user path specifically
- **Input Selection:** The SETUP_MODE boolean flag is a decision point in the application that causes different UI behavior. Testing both states (true/false) ensures both code paths work correctly. This test covers the "true" path

**Bug Discovery:**
No bugs were uncovered. Setup mode correctly displays all profile fields with the email and student ID fields disabled (non-editable), and the "Skip for Now" button is visible, allowing new users to defer profile completion.

---

## Test Case 5: testEditProfileFieldsAreEditable

### i. Location
- **Folder:** `app/src/androidTest/java/com/example/bestllm/`
- **File:** `UserProfileAndPasswordBlackBoxTest.java`
- **Test Method:** `testEditProfileFieldsAreEditable()`

### ii. Description and Execution

**Description:**
Tests that profile fields accept user input correctly. Specifically: editable fields (name and bio) allow text input and display entered text, while protected fields (email and student ID) remain disabled to prevent unauthorized changes.

**How to Execute:**
1. Open Android Studio
2. Navigate to `app/src/androidTest/java/com/example/bestllm/UserProfileAndPasswordBlackBoxTest.java`
3. Right-click on the `testEditProfileFieldsAreEditable()` method
4. Select "Run 'testEditProfileFieldsAreEditable()'"
5. Run on an Android emulator or physical device
6. Note: This test may require Firebase Authentication to be configured
7. The test will enter text into name and bio fields and verify protected fields are disabled

### iii. Rationale

**Why this input:**
- Input: Type "Test User Name" into name field and "This is my test bio" into bio field
- These are realistic, typical user inputs that represent common profile updates

**How the test case was generated:**
- **Testing Technique:** Input domain testing and equivalence partitioning
- **Reasoning:** Profile fields have different security constraints - some can be edited (name, bio) while others cannot (email, student ID). We need to verify both the positive case (editable fields accept input) and the security constraint (protected fields reject input)
- **Input Selection:**
  - "Test User Name" - represents a typical 3-word name with spaces and mixed case, covering the normal equivalence class for names
  - "This is my test bio" - represents a short sentence bio, covering the normal equivalence class for bio text
  - These inputs are in the valid range but test realistic user behavior rather than edge cases

**Bug Discovery:**
No bugs were uncovered. The test confirms:
- Name and bio fields correctly accept and display user input
- Email and student ID fields are properly disabled, preventing unauthorized changes
- This security constraint protects user identity information from accidental or malicious modification
