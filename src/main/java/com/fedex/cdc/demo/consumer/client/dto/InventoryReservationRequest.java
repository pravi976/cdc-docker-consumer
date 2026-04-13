package com.fedex.cdc.demo.consumer.client.dto;

public record InventoryReservationRequest(
        String reservationId,
        String sku,
        int requestedQty,
        String orderId,
        String shipNode,
        String priority
) {
}

