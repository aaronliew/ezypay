package com.example.ezypay.controller;

import com.example.ezypay.constant.Constants;
import com.example.ezypay.exception.InvalidArgumentException;
import com.example.ezypay.exception.InvalidDateRangeException;
import com.example.ezypay.model.SubscriptionRequest;
import com.example.ezypay.model.SubscriptionResponse;
import com.example.ezypay.service.SubscriptionService;
import com.example.ezypay.util.DateUtil;
import com.example.ezypay.util.Json;
import com.example.ezypay.util.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("subscriptions")
@Slf4j
public class SubscriptionController {


    SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService){
        this.subscriptionService = subscriptionService;
    }


    @PostMapping("")
    public SubscriptionResponse createSubscription(@RequestBody SubscriptionRequest subscriptionRequest){
        try {
            validateSubscriptionRequest(subscriptionRequest);
            log.info("Processing create subscription request, request = {}", Json.toString(subscriptionRequest));
            SubscriptionResponse subscriptionResponse =  subscriptionService.createSubscription(subscriptionRequest);
            log.info("Successfully processing request, response = {}", Json.toString(subscriptionResponse));
            return subscriptionService.createSubscription(subscriptionRequest);
        } catch (InvalidArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Argument is invalid");
        } catch (InvalidDateRangeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date range is invalid");
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid page number or size");
        }
    }

    public void validateSubscriptionRequest(SubscriptionRequest subscriptionRequest) throws InvalidArgumentException {
        if (ValidationUtil.isStringEmpty(subscriptionRequest.getSubscriptionType())){
            throw new InvalidArgumentException();
        }

        if (!Constants.SubscriptionType.isSubscriptionTypeValid(subscriptionRequest.getSubscriptionType())){
            throw new InvalidArgumentException();
        }

        if (ValidationUtil.isStringEmpty(subscriptionRequest.getStartDate())){
            throw new InvalidArgumentException();
        }

        if (ValidationUtil.isStringEmpty(subscriptionRequest.getEndDate())){
            throw new InvalidArgumentException();
        }

        if (DateUtil.calculateDurationBetweenDates(subscriptionRequest.getStartDate(), subscriptionRequest.getEndDate()) > Constants.INVOICE_MAX_DATE_RANGE){
            throw new InvalidDateRangeException();
        }

        if (subscriptionRequest.getAmount() == null ||
                ValidationUtil.isStringEmpty(subscriptionRequest.getAmount().getCurrency())){
            throw new InvalidArgumentException();
        }

        if (ValidationUtil.isLongValueEmpty(subscriptionRequest.getAmount().getValue())){
            throw new InvalidArgumentException();
        }

        if (!Constants.Currency.isCurrencyTypeValid(subscriptionRequest.getAmount().getCurrency())){
            throw new InvalidArgumentException();
        }

        if (!Constants.SubCurrency.isSubCurrencyTypeValid(subscriptionRequest.getAmount().getSubCurrency())){
            throw new InvalidArgumentException();
        }
    }
}
