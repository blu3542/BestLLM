package com.example.bestllm.utils;

import java.util.regex.Pattern;

public class Validators {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "[a-zA-Z0-9+._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
    );

    public static boolean isValidUSCEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.toLowerCase().endsWith("@usc.edu") &&
                EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static boolean isValidStudentId(String studentId) {
        return studentId != null && studentId.matches("\\d{10}");
    }

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.length() >= 2;
    }
}