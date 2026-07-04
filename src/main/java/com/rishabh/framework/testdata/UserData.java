package com.rishabh.framework.testdata;

import com.rishabh.framework.utils.RandomDataUtil;

/**
 * Builder for the signup account-info form. Every field has a sane random
 * default so a test can just do UserData.builder().build() and get something
 * that'll pass validation, then override only what it actually cares about.
 */
public class UserData {

    private final String name;
    private final String email;
    private final String password;
    private final boolean male;
    private final String birthDay;
    private final String birthMonth;
    private final String birthYear;
    private final boolean subscribeNewsletter;
    private final String firstName;
    private final String lastName;
    private final String company;
    private final String address;
    private final String address2;
    private final String country;
    private final String state;
    private final String city;
    private final String zipcode;
    private final String mobileNumber;

    private UserData(Builder b) {
        this.name = b.name;
        this.email = b.email;
        this.password = b.password;
        this.male = b.male;
        this.birthDay = b.birthDay;
        this.birthMonth = b.birthMonth;
        this.birthYear = b.birthYear;
        this.subscribeNewsletter = b.subscribeNewsletter;
        this.firstName = b.firstName;
        this.lastName = b.lastName;
        this.company = b.company;
        this.address = b.address;
        this.address2 = b.address2;
        this.country = b.country;
        this.state = b.state;
        this.city = b.city;
        this.zipcode = b.zipcode;
        this.mobileNumber = b.mobileNumber;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name = RandomDataUtil.fullName();
        private String email = RandomDataUtil.uniqueEmail();
        private String password = RandomDataUtil.password();
        private boolean male = true;
        private String birthDay = "10";
        private String birthMonth = "May";
        private String birthYear = "1995";
        private boolean subscribeNewsletter = true;
        private String firstName = RandomDataUtil.firstName();
        private String lastName = RandomDataUtil.lastName();
        private String company = RandomDataUtil.company();
        private String address = RandomDataUtil.address();
        private String address2 = "";
        private String country = "India";
        private String state = RandomDataUtil.state();
        private String city = RandomDataUtil.city();
        private String zipcode = RandomDataUtil.zipCode();
        private String mobileNumber = RandomDataUtil.mobileNumber();

        public Builder name(String v) { this.name = v; return this; }
        public Builder email(String v) { this.email = v; return this; }
        public Builder password(String v) { this.password = v; return this; }
        public Builder male(boolean v) { this.male = v; return this; }
        public Builder birthDay(String v) { this.birthDay = v; return this; }
        public Builder birthMonth(String v) { this.birthMonth = v; return this; }
        public Builder birthYear(String v) { this.birthYear = v; return this; }
        public Builder subscribeNewsletter(boolean v) { this.subscribeNewsletter = v; return this; }
        public Builder firstName(String v) { this.firstName = v; return this; }
        public Builder lastName(String v) { this.lastName = v; return this; }
        public Builder company(String v) { this.company = v; return this; }
        public Builder address(String v) { this.address = v; return this; }
        public Builder address2(String v) { this.address2 = v; return this; }
        public Builder country(String v) { this.country = v; return this; }
        public Builder state(String v) { this.state = v; return this; }
        public Builder city(String v) { this.city = v; return this; }
        public Builder zipcode(String v) { this.zipcode = v; return this; }
        public Builder mobileNumber(String v) { this.mobileNumber = v; return this; }

        public UserData build() {
            return new UserData(this);
        }
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public boolean isMale() { return male; }
    public String getBirthDay() { return birthDay; }
    public String getBirthMonth() { return birthMonth; }
    public String getBirthYear() { return birthYear; }
    public boolean isSubscribeNewsletter() { return subscribeNewsletter; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getCompany() { return company; }
    public String getAddress() { return address; }
    public String getAddress2() { return address2; }
    public String getCountry() { return country; }
    public String getState() { return state; }
    public String getCity() { return city; }
    public String getZipcode() { return zipcode; }
    public String getMobileNumber() { return mobileNumber; }
}
