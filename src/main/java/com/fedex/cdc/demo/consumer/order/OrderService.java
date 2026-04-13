package com.fedex.cdc.demo.consumer.order;

import com.fedex.cdc.demo.consumer.api.CreateOrderRequest;
import com.fedex.cdc.demo.consumer.api.OrderResponse;
import com.fedex.cdc.demo.consumer.client.InventoryProviderClient;
import com.fedex.cdc.demo.consumer.client.dto.InventoryAvailabilityResponse;
import com.fedex.cdc.demo.consumer.client.dto.InventoryReservationRequest;
import com.fedex.cdc.demo.consumer.client.dto.InventoryReservationResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {
    private final InventoryProviderClient inventoryProviderClient;
    private final OrderRepository orderRepository;

    public OrderService(InventoryProviderClient inventoryProviderClient, OrderRepository orderRepository) {
        this.inventoryProviderClient = inventoryProviderClient;
        this.orderRepository = orderRepository;
    }

    public OrderResponse createOrder(CreateOrderRequest request) {
        InventoryAvailabilityResponse availability = inventoryProviderClient.getAvailability(request.sku());
        if (availability.availableQty() < request.quantity()) {
            throw new IllegalStateException("Insufficient inventory for sku=" + request.sku());
        }

        String reservationId = "RES-" + UUID.nameUUIDFromBytes((request.orderId() + request.sku()).getBytes())
                .toString().substring(0, 12);
        InventoryReservationResponse reservation = inventoryProviderClient.reserve(new InventoryReservationRequest(
                reservationId,
                request.sku(),
                request.quantity(),
                request.orderId(),
                request.shipNode(),
                request.priority()
        ));

        OrderEntity entity = new OrderEntity();
        entity.setOrderId(request.orderId());
        entity.setSku(request.sku());
        entity.setQuantity(request.quantity());
        entity.setShipNode(request.shipNode());
        entity.setPriority(request.priority());
        entity.setStatus("CONFIRMED");
        orderRepository.save(entity);

        return new OrderResponse(
                entity.getOrderId(),
                entity.getSku(),
                entity.getQuantity(),
                entity.getStatus(),
                reservation.reservationId()
        );
    }
}

