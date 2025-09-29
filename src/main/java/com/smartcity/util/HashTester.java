package com.smartcity.util;

import com.smartcity.dao.PasswordUtil;

public class HashTester {
    public static void main(String[] args) {
        System.out.println("Testing password hashes:");
        System.out.println("admin123 hash: " + PasswordUtil.hashPassword("admin123"));
        System.out.println("test hash: " + PasswordUtil.hashPassword("test"));
    }
}