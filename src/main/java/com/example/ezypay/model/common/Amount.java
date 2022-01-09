package com.example.ezypay.model.common;


import com.example.ezypay.constant.Constants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class Amount {
    long value;
    String currency;
    String subCurrency;

    public Amount(){
        this.currency = Constants.Currency.MYR.getType();
        this.subCurrency = Constants.SubCurrency.CENT.getType();
    }
}
