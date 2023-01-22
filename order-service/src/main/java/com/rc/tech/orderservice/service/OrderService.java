package com.rc.tech.orderservice.service;

import com.rc.tech.orderservice.dto.InventoryResponse;
import com.rc.tech.orderservice.dto.OrderLineItemsDto;
import com.rc.tech.orderservice.dto.OrderRequest;
import com.rc.tech.orderservice.model.Order;
import com.rc.tech.orderservice.model.OrderLineItems;
import com.rc.tech.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItemsList =  orderRequest.getOrderLineItemDtoList()
                .stream().map(orderLineItemsDto -> mapToOrderLineItem(orderLineItemsDto))
                .toList();

        order.setOrderLineItemsList(orderLineItemsList);

        final List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCodes", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean allProductsInStock = false;
        if (inventoryResponses != null) {
            allProductsInStock = Arrays.stream(inventoryResponses)
                    .allMatch(response -> response.isInStock());
        }

        if(allProductsInStock) {
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product is not in stock, Please try again later");
        }


        log.info("Order Saved Successfully {} ", order.getOrderNumber());
    }

    private OrderLineItems mapToOrderLineItem(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
