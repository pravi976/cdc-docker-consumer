package com.fedex.cdc.demo.consumer.client.dto;

public record InventoryReservationResponse(
        String reservationId,
        String sku,
        int reservedQty,
        String reservationStatus,
        String reasonCode
) {
}

