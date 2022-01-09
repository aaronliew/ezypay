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


    @PostMapping("/create")
    public SubscriptionResponse createSubscription(@RequestBody SubscriptionRequest subscriptionRequest){
        try {
            log.info(Json.toString(subscriptionRequest));
            validateSubscriptionRequest(subscriptionRequest);

            SubscriptionResponse subscriptionResponse =  subscriptionService.createSubscription(subscriptionRequest);
            log.info(Json.toString(subscriptionResponse));
            return subscriptionService.createSubscription(subscriptionRequest);
        } catch (Exception e){
            log.error("error", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid page number or size");
        }
    }

    public void validateSubscriptionRequest(SubscriptionRequest subscriptionRequest) throws InvalidArgumentException {
        if (ValidationUtil.isStringEmpty(subscriptionRequest.getSubscriptionType())){
            throw new InvalidArgumentException();
        }

        if (ValidationUtil.isStringEmpty(subscriptionRequest.getStartDate())){
            throw new InvalidArgumentException();
        }

        if (ValidationUtil.isStringEmpty(subscriptionRequest.getEndDate())){
            throw new InvalidArgumentException();
        }

        if (DateUtil.calculateDurationBetweenDates(subscriptionRequest.getStartDate(), subscriptionRequest.getEndDate()) > Constants.INVOICE_MAX_DATE_RANGE){
            log.info(String.valueOf(DateUtil.calculateDurationBetweenDates(subscriptionRequest.getStartDate(), subscriptionRequest.getEndDate())));
            throw new InvalidDateRangeException();
        }

        if (ValidationUtil.isStringEmpty(subscriptionRequest.getAmount().getCurrency())){
            throw new InvalidArgumentException();
        }

        if (ValidationUtil.isLongValueEmpty(subscriptionRequest.getAmount().getValue())){
            throw new InvalidArgumentException();
        }
    }
}
