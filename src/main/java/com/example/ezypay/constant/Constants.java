package com.example.ezypay.constant;

public class Constants {
    public static String TRANSACTION_RECORD_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String ROLE_LIBRARIAN = "ROLE_LIBRARIAN";
    public static String ROLE_MEMBER = "ROLE_MEMBER";
    public static long INVOICE_MAX_DATE_RANGE = 90;

    public enum Currency {
        MYR("MYR");

        String type;

        Currency(String type){
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    public enum SubCurrency {
        CENT("CENT");

        String type;

        SubCurrency(String type){
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    public enum SubscriptionType {
        DAILY,
        MONTHLY,
        WEEKLY
    }
}
