package com.fedex.cdc.demo.consumer.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateOrderRequest(
        @NotBlank String orderId,
        @NotBlank String sku,
        @Min(1) int quantity,
        @NotBlank String shipNode,
        @NotBlank String priority
) {
}

