package com.demoapp.demoapp.models.requests;

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
