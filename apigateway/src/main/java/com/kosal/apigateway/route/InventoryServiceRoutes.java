package com.kosal.apigateway.route;

import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;

@Configuration
public class InventoryServiceRoutes {

    @Bean
    public RouterFunction<ServerResponse> inventoryRoutes() {
        return GatewayRouterFunctions.route("inventory-service")
                .route(RequestPredicates.path("/api/v1/inventory/venue/{venueId}"),
                        HandlerFunctions.http())
                .route(RequestPredicates.path("/api/v1/inventory/event/{eventId}"),
                        HandlerFunctions.http())
                .before(BeforeFilterFunctions.uri("http://localhost:8080"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> inventoryServiceApiDoc() {
        return GatewayRouterFunctions.route("inventory-service-api-docs")
                .route(RequestPredicates.path("/docs/inventoryservice/v3/api-docs"), HandlerFunctions.http())
                .before(BeforeFilterFunctions.uri("http://localhost:8080"))
                .before(BeforeFilterFunctions.setPath("/v3/api-docs"))
                .build();
    }
}
