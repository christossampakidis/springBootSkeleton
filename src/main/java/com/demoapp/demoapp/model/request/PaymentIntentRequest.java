package com.demoapp.demoapp.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentIntentRequest {
    private String email;
    private Long amount;
}
