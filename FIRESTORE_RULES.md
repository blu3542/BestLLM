# Firestore Security Rules - Saved Prompts Fix

## Problem
You're getting `PERMISSION_DENIED` errors when trying to save/unsave prompts or view saved prompts. This is because Firestore security rules don't allow access to the `users/{userId}/savedPrompts` subcollection.

## Solution

You need to update your Firestore security rules in the Firebase Console. Here's how:

### Step 1: Access Firebase Console
1. Go to https://console.firebase.google.com/
2. Select your project: `bestllm-3ad45`
3. Click on **Firestore Database** in the left sidebar
4. Click on the **Rules** tab at the top

### Step 2: Update the Rules

Add or update your rules to include permissions for the `savedPrompts` subcollection. Here's the complete ruleset:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    
    // Users collection - users can read any user, but only update their own
    match /users/{userId} {
      allow read: if request.auth != null;
      allow create: if request.auth != null && request.auth.uid == userId;
      allow update: if request.auth != null && request.auth.uid == userId;
      allow delete: if request.auth != null && request.auth.uid == userId;
      
      // Saved Prompts subcollection - users can only access their own saved prompts
      match /savedPrompts/{promptId} {
        allow read: if request.auth != null && request.auth.uid == userId;
        allow create: if request.auth != null && request.auth.uid == userId;
        allow delete: if request.auth != null && request.auth.uid == userId;
        allow update: if false; // No updates needed, only create/delete
      }
    }
    
    // Posts collection
    match /posts/{postId} {
      allow read: if request.auth != null;
      allow create: if request.auth != null;
      allow update: if request.auth != null && request.auth.uid == resource.data.authorId;
      allow delete: if request.auth != null && request.auth.uid == resource.data.authorId;
      
      // Comments subcollection
      match /comments/{commentId} {
        allow read: if request.auth != null;
        allow create: if request.auth != null;
        allow update: if request.auth != null && request.auth.uid == resource.data.authorId;
        allow delete: if request.auth != null && request.auth.uid == resource.data.authorId;
      }
      
      // Votes subcollection
      match /votes/{voteId} {
        allow read: if request.auth != null;
        allow create: if request.auth != null;
        allow update: if request.auth != null && request.auth.uid == resource.data.userId;
        allow delete: if request.auth != null && request.auth.uid == resource.data.userId;
      }
    }
    
    // Prompts collection
    match /prompts/{promptId} {
      allow read: if request.auth != null;
      allow create: if request.auth != null;
      allow update: if request.auth != null && request.auth.uid == resource.data.authorId;
      allow delete: if request.auth != null && request.auth.uid == resource.data.authorId;
    }
  }
}
```

### Step 3: Publish the Rules
1. After updating the rules, click **Publish** button
2. Wait for confirmation that rules are published
3. Test your app again - the permission error should be gone!

## Key Points

- **`savedPrompts/{promptId}`**: This subcollection allows users to read, create, and delete their own saved prompts
- **`request.auth.uid == userId`**: Ensures users can only access their own saved prompts
- **`request.auth != null`**: Ensures user is logged in

## Testing

After updating the rules:
1. Try saving a prompt - should work without permission error
2. Try unsaving a prompt - should work
3. Try viewing saved prompts filter - should work
4. Try accessing another user's saved prompts - should be blocked (this is correct behavior)

## Troubleshooting

If you still get errors:
1. Make sure you're logged in (check Firebase Auth)
2. Verify the rules were published successfully
3. Check Firebase Console logs for more detailed error messages
4. Make sure your Firebase project ID matches: `bestllm-3ad45`

