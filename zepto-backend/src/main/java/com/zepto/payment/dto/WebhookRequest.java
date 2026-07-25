package com.zepto.payment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebhookRequest {

    private String event;

    private String payload;

}