package com.example.jaggia.wheresmystuff9.model.user_system;

public class EmailHandler {
    private static final int VALIDEMAILLENGTH = 4;
    public static boolean validateEmailFormat(String email) {
        return null != email && email.length() >= VALIDEMAILLENGTH && email.contains("@") && email.contains(".");
    }
}
