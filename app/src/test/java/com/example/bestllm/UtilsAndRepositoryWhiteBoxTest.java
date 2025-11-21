package com.example.bestllm;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.bestllm.utils.SessionManager;
import com.example.bestllm.utils.Validators;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Additional White-box Tests for Utils and Repository Validation Logic
 * 
 * Tests 1-10: Comprehensive Validators coverage
 * Tests 11-15: SessionManager with mocking
 */
@RunWith(MockitoJUnitRunner.class)
public class UtilsAndRepositoryWhiteBoxTest {
    
    @Mock
    private Context mockContext;
    
    @Mock
    private SharedPreferences mockPrefs;
    
    @Mock
    private SharedPreferences.Editor mockEditor;
    
    private SessionManager sessionManager;

    @Before
    public void setUp() {
        when(mockContext.getSharedPreferences(anyString(), anyInt())).thenReturn(mockPrefs);
        when(mockPrefs.edit()).thenReturn(mockEditor);
        when(mockEditor.putString(anyString(), anyString())).thenReturn(mockEditor);
        when(mockEditor.putBoolean(anyString(), anyBoolean())).thenReturn(mockEditor);
        sessionManager = new SessionManager(mockContext);
    }

    // ========== COMPREHENSIVE VALIDATORS TESTS (1-10) ==========
    
    @Test
    public void test1_ValidatorsUSCEmailValidCases() {
        assertTrue(Validators.isValidUSCEmail("test@usc.edu"));
        assertTrue(Validators.isValidUSCEmail("student123@usc.edu"));
        assertTrue(Validators.isValidUSCEmail("john.doe@usc.edu"));
        assertTrue(Validators.isValidUSCEmail("test_user@usc.edu"));
    }
    
    @Test
    public void test2_ValidatorsUSCEmailInvalidCases() {
        assertFalse(Validators.isValidUSCEmail("test@gmail.com"));
        assertFalse(Validators.isValidUSCEmail("test@usc.com"));
        assertFalse(Validators.isValidUSCEmail("test@usc"));
        assertFalse(Validators.isValidUSCEmail("@usc.edu"));
    }
    
    @Test
    public void test3_ValidatorsUSCEmailNullAndEmpty() {
        assertFalse(Validators.isValidUSCEmail(null));
        assertFalse(Validators.isValidUSCEmail(""));
        assertFalse(Validators.isValidUSCEmail("   "));
    }
    
    @Test
    public void test4_ValidatorsPasswordValidCases() {
        assertTrue(Validators.isValidPassword("123456"));
        assertTrue(Validators.isValidPassword("password"));
        assertTrue(Validators.isValidPassword("verylongpassword123456"));
        assertTrue(Validators.isValidPassword("abc123"));
    }
    
    @Test
    public void test5_ValidatorsPasswordInvalidCases() {
        assertFalse(Validators.isValidPassword("12345"));
        assertFalse(Validators.isValidPassword("pass"));
        assertFalse(Validators.isValidPassword(null));
        assertFalse(Validators.isValidPassword(""));
    }
    
    @Test
    public void test6_ValidatorsStudentIdValidCases() {
        assertTrue(Validators.isValidStudentId("1234567890"));
        assertTrue(Validators.isValidStudentId("0000000000"));
        assertTrue(Validators.isValidStudentId("9999999999"));
    }
    
    @Test
    public void test7_ValidatorsStudentIdInvalidCases() {
        assertFalse(Validators.isValidStudentId("12345"));
        assertFalse(Validators.isValidStudentId("123456789"));
        assertFalse(Validators.isValidStudentId("12345678901"));
        assertFalse(Validators.isValidStudentId("abcdefghij"));
        assertFalse(Validators.isValidStudentId("12345abcde"));
    }
    
    @Test
    public void test8_ValidatorsStudentIdNullAndEmpty() {
        assertFalse(Validators.isValidStudentId(null));
        assertFalse(Validators.isValidStudentId(""));
    }
    
    @Test
    public void test9_ValidatorsNameValidCases() {
        assertTrue(Validators.isValidName("John Doe"));
        assertTrue(Validators.isValidName("AB"));
        assertTrue(Validators.isValidName("Mary Jane Watson"));
        assertTrue(Validators.isValidName("O'Brien"));
    }
    
    @Test
    public void test10_ValidatorsNameInvalidCases() {
        assertFalse(Validators.isValidName("A"));
        assertFalse(Validators.isValidName(null));
        assertFalse(Validators.isValidName(""));
        assertFalse(Validators.isValidName("   "));
        assertFalse(Validators.isValidName("\t"));
    }

    // ========== SESSION MANAGER TESTS (11-15) ==========
    
    @Test
    public void test11_SessionManagerCreateSession() {
        sessionManager.createSession("user123", "John Doe", "john@usc.edu");
        verify(mockEditor).putString("userId", "user123");
        verify(mockEditor).putString("userName", "John Doe");
        verify(mockEditor).putString("userEmail", "john@usc.edu");
        verify(mockEditor).putBoolean("isLoggedIn", true);
        verify(mockEditor).apply();
    }
    
    @Test
    public void test12_SessionManagerIsLoggedIn() {
        when(mockPrefs.getBoolean("isLoggedIn", false)).thenReturn(true);
        assertTrue(sessionManager.isLoggedIn());
        when(mockPrefs.getBoolean("isLoggedIn", false)).thenReturn(false);
        assertFalse(sessionManager.isLoggedIn());
    }
    
    @Test
    public void test13_SessionManagerGetUserId() {
        when(mockPrefs.getString("userId", null)).thenReturn("user123");
        assertEquals("user123", sessionManager.getUserId());
        when(mockPrefs.getString("userId", null)).thenReturn(null);
        assertNull(sessionManager.getUserId());
    }
    
    @Test
    public void test14_SessionManagerGetUserNameAndEmail() {
        when(mockPrefs.getString("userName", null)).thenReturn("John Doe");
        when(mockPrefs.getString("userEmail", null)).thenReturn("john@usc.edu");
        assertEquals("John Doe", sessionManager.getUserName());
        assertEquals("john@usc.edu", sessionManager.getUserEmail());
    }
    
    @Test
    public void test15_SessionManagerLogout() {
        sessionManager.logout();
        verify(mockEditor).clear();
        verify(mockEditor).apply();
    }
}

