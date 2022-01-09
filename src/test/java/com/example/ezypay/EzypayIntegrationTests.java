package com.example.ezypay;

import com.example.ezypay.constant.Constants;
import com.example.ezypay.model.SubscriptionRequest;
import com.example.ezypay.model.common.Amount;
import com.example.ezypay.util.Json;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
class EzypayIntegrationTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    void setup() throws Exception {
        mvc = webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @DisplayName("Subscription request payload must be validated")
    void validateSubscriptionRequestPayload() throws Exception{

        SubscriptionRequest subscriptionRequest = createDailySubscriptionRequest();
        subscriptionRequest.setSubscriptionType("RANDOM");
        System.out.println(Json.toString(subscriptionRequest));
        mvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.toString(subscriptionRequest))
                        .with(httpBasic("admin","password")))
                .andExpect(status().isBadRequest());

        //test with invalid start date
        subscriptionRequest = createDailySubscriptionRequest();
        subscriptionRequest.setStartDate("01/13/2021");
        mvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.toString(subscriptionRequest))
                        .with(httpBasic("admin","password")))
                .andExpect(status().isBadRequest());

        //test with invalid end date
        subscriptionRequest = createDailySubscriptionRequest();
        subscriptionRequest.setEndDate("01/13/2021");
        mvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.toString(subscriptionRequest))
                        .with(httpBasic("admin","password")))
                .andExpect(status().isBadRequest());

        //test with invalid currency
        subscriptionRequest = createDailySubscriptionRequest();
        subscriptionRequest.getAmount().setCurrency("YEN");
        mvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.toString(subscriptionRequest))
                        .with(httpBasic("admin","password")))
                .andExpect(status().isBadRequest());

        //test with invalid subCurrency
        subscriptionRequest = createDailySubscriptionRequest();
        subscriptionRequest.getAmount().setSubCurrency("RUPIAH");
        mvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.toString(subscriptionRequest))
                        .with(httpBasic("admin","password")))
                .andExpect(status().isBadRequest());


        //test with more than 90 days date range
        subscriptionRequest = createDailySubscriptionRequest();
        subscriptionRequest.setStartDate("01/02/2022");
        subscriptionRequest.setEndDate("01/02/2023");
        mvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.toString(subscriptionRequest))
                        .with(httpBasic("admin","password")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Check daily subscription invoice dates")
    void checkDailySubscriptionInvoices() throws Exception{
        SubscriptionRequest subscriptionRequest = createDailySubscriptionRequest();
        mvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.toString(subscriptionRequest))
                        .with(httpBasic("admin","password")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.invoiceDates", hasSize(3)))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.invoiceDates",
                                Matchers.containsInAnyOrder("04/02/2022", "05/02/2022", "06/02/2022")));
    }

    @Test
    @DisplayName("Check weekly subscription invoice dates")
    void checkWeeklySubscriptionInvoices() throws Exception{
        SubscriptionRequest subscriptionRequest = createWeeklySubscriptionRequest();
        mvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.toString(subscriptionRequest))
                        .with(httpBasic("admin","password")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.invoiceDates", hasSize(5)))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.invoiceDates",
                                Matchers.containsInAnyOrder("08/02/2022", "15/02/2022", "22/02/2022", "01/03/2022", "08/03/2022")));
    }

    @Test
    @DisplayName("Check monthly subscription invoice dates")
    void checkMonthlySubscriptionInvoices() throws Exception{
        SubscriptionRequest subscriptionRequest = createMonthlySubscriptionRequest();
        mvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.toString(subscriptionRequest))
                        .with(httpBasic("admin","password")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.invoiceDates", hasSize(3)))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.invoiceDates",
                                Matchers.containsInAnyOrder("20/02/2022", "20/03/2022", "20/04/2022")));

    }

    private SubscriptionRequest createDailySubscriptionRequest(){
        Amount amount = new Amount();
        amount.setValue(1000);
        SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
        subscriptionRequest.setSubscriptionType(Constants.SubscriptionType.DAILY.name());
        subscriptionRequest.setAmount(amount);
        subscriptionRequest.setStartDate("04/02/2022");
        subscriptionRequest.setEndDate("06/02/2022");
        return subscriptionRequest;
    }

    private SubscriptionRequest createWeeklySubscriptionRequest(){
        Amount amount = new Amount();
        amount.setValue(1000);
        SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
        subscriptionRequest.setSubscriptionType(Constants.SubscriptionType.WEEKLY.name());
        subscriptionRequest.setAmount(amount);
        subscriptionRequest.setStartDate("04/02/2022");
        subscriptionRequest.setEndDate("04/03/2022");
        return subscriptionRequest;
    }

    private SubscriptionRequest createMonthlySubscriptionRequest(){
        Amount amount = new Amount();
        amount.setValue(1000);
        SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
        subscriptionRequest.setSubscriptionType(Constants.SubscriptionType.MONTHLY.name());
        subscriptionRequest.setAmount(amount);
        subscriptionRequest.setStartDate("04/02/2022");
        subscriptionRequest.setEndDate("04/04/2022");
        return subscriptionRequest;
    }

}
