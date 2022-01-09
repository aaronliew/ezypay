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

        public static boolean isCurrencyTypeValid(String type){
            String result = "";
            for (Currency currency : Currency.values()) {
                if (currency.name().equals(type)){
                    result = currency.name();
                    break;
                }
            }
            return result.length() > 0;
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

        public static boolean isSubCurrencyTypeValid(String type){
            String result = "";
            for (SubCurrency subCurrency : SubCurrency.values()) {
                if (subCurrency.name().equals(type)){
                    result = subCurrency.name();
                    break;
                }
            }
            return result.length() > 0;
        }
    }

    public enum SubscriptionType {
        DAILY,
        MONTHLY,
        WEEKLY;

        public static boolean isSubscriptionTypeValid(String type){
            String result = "";
            for (SubscriptionType subscriptionType : SubscriptionType.values()) {
                if (subscriptionType.name().equals(type)){
                    result = subscriptionType.name();
                    break;
                }
            }
            return result.length() > 0;
        }

    }
}
