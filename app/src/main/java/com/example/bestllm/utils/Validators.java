package com.example.bestllm.utils;

import android.util.Patterns;

public class Validators {

    public static boolean isValidUSCEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.toLowerCase().endsWith("@usc.edu") &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.length() >= 2;
    }
}