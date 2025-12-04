# How to Run the App

This section assumes the project is already set up in Android Studio with a device/emulator selected.

---

## 1. Launching the App

1. In Android Studio, make sure the **`app`** run configuration is selected.
2. Click the **Run ▶** button in the top toolbar.
3. Once the build and install finish, the app will automatically open on your selected device/emulator.
4. You should see either a **login screen** or a **home/posts screen**, depending on whether a user is already logged in.

---

## 2. Registration and Login

### 2.1 Register a New USC User

1. If you see a **login screen**, tap the **Register** button/link to go to the registration screen.
2. On the registration screen, fill in:
   - **Name** – your full name.
   - **Student ID** – exactly **10 digits** (e.g., `1234567890`).
   - **Email** – must end with **`@usc.edu`**.
   - **Password** – at least the required minimum length.
   - **Confirm Password** – must match the password.
3. Tap the **Register** button.
4. If any field is invalid (non-USC email, wrong ID length, short password, passwords don’t match), read the error message shown and correct the input.
5. On success, you will typically be automatically logged in or redirected to the login screen.

### 2.2 Log In

1. On the **login screen**, enter your **USC email** and **password**.
2. Tap the **Login** button.
3. If login succeeds, you will be taken to the **home/posts** screen.

---

## 3. Profile Management

### 3.1 Initial Profile Setup

1. After registering/logging in for the first time, you may be taken to a **profile setup** screen.
2. Fill in the required profile fields, such as:
   - **Affiliation** – department and school (e.g., “Computer Science, Viterbi”).
   - **Birth date**.
   - **Bio** – a short description about yourself.
3. Tap **Save** or **Done** to complete your profile.
4. Your **name, email, and student ID** are fixed and cannot be changed later.

### 3.2 Editing Profile Information

1. From any main screen, navigate to your **Profile** (for example via a profile icon, navigation drawer, or menu option labeled “Profile” or “Settings”).
2. On the profile screen, locate editable fields (e.g., **birth date**, **bio**, possibly some affiliation fields if allowed).
3. Update the fields you want to change.
4. Tap **Save** or **Update Profile**.
5. Verify that your changes are reflected on the profile screen.

### 3.3 Resetting Password

1. Go to the **Login** screen if you are logged out, or to a **Profile/Account** section if there is a “Reset Password” option there.
2. Tap **“Forgot Password”** or **“Reset Password”**.
3. Follow the on-screen instructions (usually entering your USC email and confirming).
4. Check your email (if the app uses email reset) or follow the app’s flow to complete the password reset.
5. Log back in with your new password.

---

## 4. Posts – Creating, Viewing, Editing, Deleting

### 4.1 Viewing Posts (Home Feed)

1. After login, you should be on the **Home / Posts** screen.
2. Scroll through the **list of posts**, which show:
   - **Title**
   - **LLM tag** (e.g., `gpt-4`)
   - **Author name**
   - A snippet of the **body**
   - **Vote counts** (upvotes/downvotes)
3. Tap any post to open the **Post Details** screen, where you can see the full content and comments.

### 4.2 Creating a New Post

1. On the **Home / Posts** screen, tap the **“New Post”** button or **Floating Action Button (FAB)** (often a `+` icon).
2. In the **Create Post** screen:
   - Enter a **Title** (required).
   - Enter the **Body** of your post (required).
   - Enter a **Tag** specifying the LLM being discussed (required, e.g., `gpt-4`, `claude-3`).
3. Tap the **Create** or **Publish** button.
4. If any required field is missing, read and fix the error messages (for example, “Title is required”).

5. After successful creation, you should be taken back to a list where your post now appears.

### 4.3 Editing an Existing Post

1. On the **Home / Posts** or **Post Details** screen, locate a post that **you authored**.
2. Tap it to open **Post Details**.
3. Look for an **Edit** option (usually an edit icon or menu item).
4. Modify the **title**, **body**, or **tag** as allowed.
5. Tap **Save** or **Update Post** to submit your changes.
6. Verify your updated post appears correctly in the list and details view.

### 4.4 Deleting a Post (if enabled)

1. Open one of **your own posts** in the **Post Details** screen.
2. Look for a **Delete** button or menu item for that post.
3. Tap **Delete** and confirm in any confirmation dialog.
4. Return to the posts list and confirm that the deleted post is no longer visible.

---

## 5. Comments – Adding, Editing, Deleting

### 5.1 Adding a Comment to a Post

1. From the **Home / Posts** screen, tap a post to open **Post Details**.
2. Scroll down to the **Comments** section.
3. Tap **Add Comment** (or equivalent button).
4. Fill in:
   - Optional **Comment Title** (if the UI supports it).
   - Required **Comment Body**.
5. Tap **Post Comment** or **Submit**.
6. Your new comment should appear in the comments list with your name and timestamp.

### 5.2 Editing Your Comment

1. In the **Comments** section of a post, find a comment that **you authored**.
2. Tap the **Edit** icon or menu next to your comment.
3. Update the comment’s **title** (if any) and/or **body**.
4. Tap **Save** or **Update Comment**.
5. Confirm that the changes appear correctly in the comments list.

### 5.3 Deleting Your Comment

1. Locate your own comment in the comments list for a post.
2. Tap the **Delete** option (icon, trash can, or menu).
3. Confirm deletion when prompted.
4. Ensure that the comment is removed from the comments list.

---

## 6. Voting – Upvoting and Downvoting Posts & Comments

### 6.1 Voting on Posts

1. On the **Home / Posts** screen or **Post Details** screen, find the **upvote** and **downvote** controls for a post (arrows, thumbs, or similar icons).
2. Tap the **Upvote** control to upvote the post:
   - The **upvote count** should increase by 1 (or your previous downvote removed and upvote applied).
3. Tap the **Downvote** control to downvote the post:
   - The **downvote count** should update accordingly.
4. To **change or cancel** your vote:
   - Tap the opposite vote button to switch.
   - Tap the same button again (if the app supports toggling) to reset back to neutral.

### 6.2 Voting on Comments

1. On the **Post Details** screen, scroll to the **Comments** section.
2. Each comment should have its own **upvote** and **downvote** buttons next to it.
3. Use them the same way:
   - Tap **Upvote** to upvote a comment.
   - Tap **Downvote** to downvote a comment.
   - Change/cancel your vote by tapping again or tapping the opposite control, depending on how the UI behaves.

---

## 7. Viewing Trending (Top-k) Posts

1. On the **Home / Posts** screen, look for a **Trending** tab, button, or menu option (e.g., “Trending” or “Top Posts”).
2. Tap **Trending** to switch to the **top-k posts** view.
3. The app will display posts ranked by **number of upvotes** (or by a custom ranking algorithm if implemented).
4. Scroll through the trending list to explore the most popular posts.

---

## 8. Prompt Sharing – Creating, Viewing, Editing, Deleting Prompts

### 8.1 Opening the Prompt Sharing Section

1. From the **Home / Posts** screen, open the **app menu** (toolbar icon or overflow menu).
2. Tap the menu option labeled **Prompt Sharing** (often shown as “Prompt_Sharing” or similar).
3. You will be navigated to the **Prompts List** screen showing all prompts shared by users.

### 8.2 Viewing Existing Prompts

1. On the **Prompts List** screen, scroll to see all prompts.
2. Each prompt typically shows:
   - **Title**
   - Brief **description** (if available)
   - **LLM tag(s)**
   - Author and timestamp  
   - A **bookmark icon** indicating whether the prompt is saved (outlined = not saved, filled = saved)
3. Tap any prompt to open a detailed view (if your UI has a detail screen for prompts).

### 8.3 Creating a New Prompt

1. On the **Prompts List** screen, tap the **“New Prompt”** button or FAB.
2. In the **Create Prompt** screen, fill in:
   - **Title** (required).
   - **Prompt text** (the actual prompt you would give to an LLM, required).
   - Optional **description** (short explanation of what the prompt does).
   - At least one **Tag** specifying the LLM (required, e.g., `gpt-4`).
3. Tap **Create** or **Submit**.
4. If any required information is missing, read and correct the errors (e.g., “Title is required”, “Prompt text is required”, “At least one tag is required”).
5. After success, return to the **Prompts List** and verify your prompt appears.

### 8.4 Editing Your Prompt

1. In the **Prompts List**, find a prompt that **you created**.
2. Tap the prompt or an **Edit** icon associated with it.
3. In the **Edit Prompt** screen, modify fields such as title, prompt text, description, or tags as allowed.
4. Tap **Save** or **Update Prompt**.
5. Confirm that the updated prompt appears correctly in the list.

### 8.5 Deleting Your Prompt

1. Locate your prompt in the **Prompts List**.
2. Tap the **Delete** icon or menu option for that prompt.
3. Confirm deletion in the dialog if one appears.
4. Verify that the prompt is removed from the list.

### 8.6 Saving / Bookmarking Prompts

1. On the **Prompts List** screen, locate the **bookmark icon** on a prompt card.
2. Tap the **bookmark icon**:
   - If the prompt was **not saved**, it will be saved to your account and the icon should change to the **filled** state.
   - If the prompt was **already saved**, tapping again will **unsave** it and the icon should return to the **outlined** state.
3. Saved prompts are stored per user, so they persist across sessions and devices as long as you log in with the same account.

---

## 9. Searching Posts

### 9.1 Selecting Search Mode

1. On the **Home / Posts** screen, find:
   - The **search text field**.
   - The **search mode spinner/dropdown** (e.g., “Full text”, “Tag”, “Author”, “Title”).
2. Tap the **search mode** control to open its options.
3. Choose one of the modes:
   - **Tag** – search by LLM tag.
   - **Author** – search by post author name.
   - **Title** – search by words in the title.
   - **Full text** – search in both title and body.

### 9.2 Running a Search

1. After selecting a search mode, tap the **search text field**.
2. Type a **keyword or tag** based on the mode selected:
   - Tag mode: e.g., `gpt-4`.
   - Author mode: e.g., `John Doe`.
   - Title mode: e.g., `comparison`.
   - Full text: any word you expect in title or body.
3. Tap the **Search icon** or button (often a magnifying glass).
4. The posts list will update to show **only the posts** that match your query under the chosen mode.
5. To clear the search, remove the text and search again or use a **clear**/reset option if provided.

---

## 10. Searching and Filtering Prompts (Tags + Saved)

### 10.1 Searching Prompts by Tag

1. Navigate to the **Prompt Sharing** section (see 8.1).
2. On the **Prompts List** screen, look for a **search bar**, **filter field**, or **tag selector**.
3. In that search field, type an **LLM tag** (e.g., `claude-3`, `gpt-4`) or choose it from a dropdown if available.
4. Trigger the search or filter (by pressing enter, search button, or filter button, depending on the UI).
5. The list of prompts will update to show only those prompts associated with the selected tag.

### 10.2 Viewing Only Saved Prompts

1. While on the **Prompts List** screen, look for a **Saved** filter (such as a toggle, chip, or menu option).
2. Enable the **Saved** filter:
   - The list will update to show **only the prompts you have bookmarked**.
3. Disable the **Saved** filter to return to viewing **all prompts**.
4. You can combine this with other filters/search (depending on your UI) to quickly find saved prompts for a particular tag or use case.

---
