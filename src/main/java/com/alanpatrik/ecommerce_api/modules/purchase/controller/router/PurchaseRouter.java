package com.alanpatrik.ecommerce_api.modules.purchase.controller.router;

import com.alanpatrik.ecommerce_api.modules.purchase.Purchase;
import com.alanpatrik.ecommerce_api.modules.purchase.controller.hander.PurchaseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class PurchaseRouter {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/purchase",
                    produces = {APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanMethod = "create",
                    operation = @Operation(
                            operationId = "create",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "SUCCESSFUL OPERATION",
                                            content = {
                                                    @Content(schema = @Schema(
                                                            implementation = Purchase.class
                                                    ))
                                            }
                                    ),

                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "BAD REQUEST"
                                    )
                            },
                            requestBody = @RequestBody(
                                    content = {
                                            @Content(schema = @Schema(
                                                    implementation = Purchase.class
                                            ))
                                    }
                            )
                    )
            ),

            @RouterOperation(
                    path = "/api/v1/purchase/{id}",
                    produces = {APPLICATION_JSON_VALUE},
                    method = RequestMethod.DELETE,
                    beanMethod = "delete",
                    operation = @Operation(
                            operationId = "delete",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "204",
                                            description = "NO CONTENT"
                                    ),

                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "NOT FOUND"
                                    ),

                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "BAD REQUEST"
                                    )
                            },

                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "id")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> router3(PurchaseHandler handler) {
        return route()
                .path("/api/v1/purchase", b1 -> b1
                        .nest(accept(APPLICATION_JSON), b2 -> b2
                                .GET("", handler::getAll)
                                .POST("", handler::create)
                                .DELETE("/{id}", handler::delete)))
                .build();
    }
}
