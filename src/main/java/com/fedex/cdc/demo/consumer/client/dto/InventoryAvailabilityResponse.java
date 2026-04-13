package com.fedex.cdc.demo.consumer.client.dto;

public record InventoryAvailabilityResponse(
        String sku,
        int availableQty,
        String warehouseCode,
        String status
) {
}

