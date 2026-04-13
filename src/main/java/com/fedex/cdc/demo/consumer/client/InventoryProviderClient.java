package com.fedex.cdc.demo.consumer.client;

import com.fedex.cdc.demo.consumer.client.dto.InventoryAvailabilityResponse;
import com.fedex.cdc.demo.consumer.client.dto.InventoryReservationRequest;
import com.fedex.cdc.demo.consumer.client.dto.InventoryReservationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "supply-inventory-provider",
        url = "${provider.base-url}",
        path = "/api/v1/inventory"
)
public interface InventoryProviderClient {
    @GetMapping("/{sku}")
    InventoryAvailabilityResponse getAvailability(@PathVariable("sku") String sku);

    @PostMapping("/reservations")
    InventoryReservationResponse reserve(@RequestBody InventoryReservationRequest request);
}
