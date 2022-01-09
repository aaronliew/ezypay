package com.example.ezypay.service;

import com.example.ezypay.constant.Constants;
import com.example.ezypay.model.SubscriptionRequest;
import com.example.ezypay.model.SubscriptionResponse;
import com.example.ezypay.model.common.Amount;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService{

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");

    @Override
    public SubscriptionResponse createSubscription(SubscriptionRequest subscriptionRequest) {
        String subscriptionType = subscriptionRequest.getSubscriptionType();
        String startDate = subscriptionRequest.getStartDate();
        String endDate = subscriptionRequest.getEndDate();
        long amountValue = subscriptionRequest.getAmount().getValue();
        Amount amount = new Amount();
        amount.setValue(amountValue);
        List<String> invoiceDates = createInvoiceDatesBasedOnSubscriptionType(subscriptionType, startDate, endDate);

        SubscriptionResponse subscriptionResponse = new SubscriptionResponse();
        subscriptionResponse.setSubscriptionType(subscriptionRequest.getSubscriptionType());
        subscriptionResponse.setInvoiceDates(invoiceDates);
        subscriptionResponse.setAmount(amount);
        return subscriptionResponse;
    }


    private List<String> createInvoiceDatesBasedOnSubscriptionType(String subscriptionType, String fromDate, String toDate){
        LocalDate fromLocalDate = LocalDate.parse(fromDate, formatter);
        LocalDate toLocalDate = LocalDate.parse(toDate, formatter);

        List<String> invoiceDates = new ArrayList<>();
        for (LocalDate date = fromLocalDate; !date.isAfter(toLocalDate); date = date.plusDays(1)) {
            // Do your job here with `date`.
            if (subscriptionType.equals(Constants.SubscriptionType.DAILY.name())){
                handleDailySubscription(invoiceDates, date);
            } else if (subscriptionType.equals(Constants.SubscriptionType.WEEKLY.name())){
                handleWeeklySubscription(invoiceDates, date, toLocalDate);
            } else {
                handleMonthlySubscription(invoiceDates, date, toLocalDate);
            }
        }

        return invoiceDates;
    }

    private void handleMonthlySubscription(List<String> invoiceDates, LocalDate date, LocalDate finalLocalDate){
        if (date.getDayOfMonth() == 20){
            invoiceDates.add(date.format(formatter));
        } else if (date.compareTo(finalLocalDate) == 0){
            if (finalLocalDate.getDayOfMonth() < 20) {
                invoiceDates.add(date.withDayOfMonth(20).format(formatter));
            } else {
                invoiceDates.add(date.withDayOfMonth(20).plusMonths(1).format(formatter));
            }
        }
    }

    private void handleWeeklySubscription(List<String> invoiceDates, LocalDate date, LocalDate finalLocalDate){
        if (date.getDayOfWeek().equals(DayOfWeek.TUESDAY)){
            invoiceDates.add(date.format(formatter));
        } else if (date.compareTo(finalLocalDate) == 0){
            DayOfWeek dow = DayOfWeek.of( 2 );
            if (finalLocalDate.getDayOfWeek().getValue() > DayOfWeek.TUESDAY.getValue()) {
                invoiceDates.add(date.with(dow).plusWeeks(1).format(formatter));
            } else {

                invoiceDates.add(date.with(dow).format(formatter));
            }
        }
    }

    private void handleDailySubscription(List<String> invoiceDates, LocalDate date){
        invoiceDates.add(date.format(formatter));
    }
}
