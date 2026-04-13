package com.fedex.cdc.demo.consumer.api;

public record OrderResponse(
        String orderId,
        String sku,
        int quantity,
        String status,
        String reservationId
) {
}

