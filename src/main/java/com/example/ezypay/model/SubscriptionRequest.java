package com.example.ezypay.model;


import com.example.ezypay.model.common.Amount;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SubscriptionRequest {
    private Amount amount;
    private String subscriptionType;
    private String startDate;
    private String endDate;
}
