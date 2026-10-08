package com.vehiclerental;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String password = "jashu1234";

        String encodedPassword = encoder.encode(password);

        System.out.println(encodedPassword);
    }
}
