package com.store.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtils {

    // Mã hóa mật khẩu bằng SHA-256
    public static String encryptPassword(String rawPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(rawPassword.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // So sánh mật khẩu đã mã hóa
    public static boolean verifyPassword(String rawPassword, String encryptedPassword) {
        return encryptPassword(rawPassword).equals(encryptedPassword);
    }
}
