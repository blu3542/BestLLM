package com.example.bestllm;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.bestllm.utils.SessionManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * White-box test cases for SessionManager
 * 
 * Coverage: Session creation, login status, data retrieval, logout
 */
@RunWith(MockitoJUnitRunner.class)
public class SessionManagerWhiteBoxTest {
    
    @Mock
    private Context mockContext;
    
    @Mock
    private SharedPreferences mockPrefs;
    
    @Mock
    private SharedPreferences.Editor mockEditor;
    
    private SessionManager sessionManager;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockContext.getSharedPreferences(anyString(), eq(Context.MODE_PRIVATE))).thenReturn(mockPrefs);
        when(mockPrefs.edit()).thenReturn(mockEditor);
        when(mockEditor.putString(anyString(), anyString())).thenReturn(mockEditor);
        when(mockEditor.putBoolean(anyString(), anyBoolean())).thenReturn(mockEditor);
        
        sessionManager = new SessionManager(mockContext);
    }

    /**
     * White-box Test Case 1: testCreateSession
     * Location: app/src/test/java/com/example/bestllm/SessionManagerWhiteBoxTest.java
     * Description: Tests createSession stores all user data correctly
     * Rationale: Verifies session creation logic stores userId, name, email, and login status
     * Result: PASS - Session created with all fields stored
     */
    @Test
    public void testCreateSession() {
        String userId = "user123";
        String name = "Test User";
        String email = "test@usc.edu";
        
        sessionManager.createSession(userId, name, email);
        
        verify(mockEditor).putString("userId", userId);
        verify(mockEditor).putString("userName", name);
        verify(mockEditor).putString("userEmail", email);
        verify(mockEditor).putBoolean("isLoggedIn", true);
        verify(mockEditor).apply();
    }

    /**
     * White-box Test Case 2: testIsLoggedIn
     * Location: app/src/test/java/com/example/bestllm/SessionManagerWhiteBoxTest.java
     * Description: Tests isLoggedIn() retrieves login status correctly
     * Rationale: Verifies login status retrieval logic
     * Result: PASS - Login status retrieved correctly
     */
    @Test
    public void testIsLoggedIn() {
        // Test logged in
        when(mockPrefs.getBoolean("isLoggedIn", false)).thenReturn(true);
        assertTrue("Should be logged in", sessionManager.isLoggedIn());
        
        // Test not logged in
        when(mockPrefs.getBoolean("isLoggedIn", false)).thenReturn(false);
        assertFalse("Should not be logged in", sessionManager.isLoggedIn());
    }

    /**
     * White-box Test Case 3: testGetUserId
     * Location: app/src/test/java/com/example/bestllm/SessionManagerWhiteBoxTest.java
     * Description: Tests getUserId() retrieves user ID correctly
     * Rationale: Verifies user ID retrieval logic
     * Result: PASS - User ID retrieved correctly
     */
    @Test
    public void testGetUserId() {
        String userId = "user123";
        when(mockPrefs.getString("userId", null)).thenReturn(userId);
        assertEquals("User ID should match", userId, sessionManager.getUserId());
        
        // Test null
        when(mockPrefs.getString("userId", null)).thenReturn(null);
        assertNull("User ID should be null", sessionManager.getUserId());
    }

    /**
     * White-box Test Case 4: testGetUserNameAndEmail
     * Location: app/src/test/java/com/example/bestllm/SessionManagerWhiteBoxTest.java
     * Description: Tests getUserName() and getUserEmail() retrieve data correctly
     * Rationale: Verifies name and email retrieval logic
     * Result: PASS - Name and email retrieved correctly
     */
    @Test
    public void testGetUserNameAndEmail() {
        String name = "Test User";
        String email = "test@usc.edu";
        
        when(mockPrefs.getString("userName", null)).thenReturn(name);
        when(mockPrefs.getString("userEmail", null)).thenReturn(email);
        
        assertEquals("Name should match", name, sessionManager.getUserName());
        assertEquals("Email should match", email, sessionManager.getUserEmail());
    }

    /**
     * White-box Test Case 5: testLogout
     * Location: app/src/test/java/com/example/bestllm/SessionManagerWhiteBoxTest.java
     * Description: Tests logout() clears all session data
     * Rationale: Verifies logout logic clears SharedPreferences
     * Result: PASS - Logout clears session data
     */
    @Test
    public void testLogout() {
        sessionManager.logout();
        verify(mockEditor).clear();
        verify(mockEditor).apply();
    }
}

