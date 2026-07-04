package com.rishabh.framework.utils;

import com.github.javafaker.Faker;

/**
 * Wraps javafaker so every test that needs a fresh email/name doesn't hit
 * "email already exists" on the signup form when the suite reruns.
 */
public class RandomDataUtil {

    private static final Faker faker = new Faker();

    private RandomDataUtil() {
    }

    public static String uniqueEmail() {
        long stamp = System.currentTimeMillis();
        return "qa_" + stamp + "@mailinator.com";
    }

    public static String fullName() {
        return faker.name().firstName() + " " + faker.name().lastName();
    }

    public static String firstName() {
        return faker.name().firstName();
    }

    public static String lastName() {
        return faker.name().lastName();
    }

    public static String company() {
        return faker.company().name();
    }

    public static String address() {
        return faker.address().streetAddress();
    }

    public static String city() {
        return faker.address().city();
    }

    public static String state() {
        return faker.address().state();
    }

    public static String zipCode() {
        return faker.address().zipCode();
    }

    public static String mobileNumber() {
        return faker.phoneNumber().subscriberNumber(10);
    }

    public static String password() {
        return "Qa@" + faker.number().digits(6);
    }
}
