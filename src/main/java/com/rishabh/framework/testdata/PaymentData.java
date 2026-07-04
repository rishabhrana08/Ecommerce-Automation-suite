package com.rishabh.framework.testdata;

import com.github.javafaker.Faker;

/**
 * Dummy card data - this site never touches a real payment gateway, it just
 * validates that the fields aren't empty, so faker-generated values are fine.
 */
public class PaymentData {

    private final String nameOnCard;
    private final String cardNumber;
    private final String cvc;
    private final String expiryMonth;
    private final String expiryYear;

    private PaymentData(Builder b) {
        this.nameOnCard = b.nameOnCard;
        this.cardNumber = b.cardNumber;
        this.cvc = b.cvc;
        this.expiryMonth = b.expiryMonth;
        this.expiryYear = b.expiryYear;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private static final Faker faker = new Faker();

        private String nameOnCard = faker.name().fullName();
        private String cardNumber = faker.finance().creditCard().replaceAll("-", "");
        private String cvc = String.valueOf(faker.number().numberBetween(100, 999));
        private String expiryMonth = "12";
        private String expiryYear = "2028";

        public Builder nameOnCard(String v) { this.nameOnCard = v; return this; }
        public Builder cardNumber(String v) { this.cardNumber = v; return this; }
        public Builder cvc(String v) { this.cvc = v; return this; }
        public Builder expiryMonth(String v) { this.expiryMonth = v; return this; }
        public Builder expiryYear(String v) { this.expiryYear = v; return this; }

        public PaymentData build() {
            return new PaymentData(this);
        }
    }

    public String getNameOnCard() { return nameOnCard; }
    public String getCardNumber() { return cardNumber; }
    public String getCvc() { return cvc; }
    public String getExpiryMonth() { return expiryMonth; }
    public String getExpiryYear() { return expiryYear; }
}
