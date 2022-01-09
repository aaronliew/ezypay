package com.example.ezypay.service;

import com.example.ezypay.model.SubscriptionRequest;
import com.example.ezypay.model.SubscriptionResponse;

public interface SubscriptionService {

    SubscriptionResponse createSubscription(SubscriptionRequest subscriptionRequest);

}
