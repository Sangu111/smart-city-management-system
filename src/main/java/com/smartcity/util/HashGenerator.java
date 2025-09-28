package com.smartcity.util;

import com.smartcity.dao.PasswordUtil;

/**
 * Utility class for generating password hashes
 * Usage: java HashGenerator <password>
 */
public class HashGenerator {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HashGenerator <password>");
            return;
        }
        
        String password = args[0];
        String hash = PasswordUtil.hashPassword(password);
        System.out.println("Password hash: " + hash);
    }
}