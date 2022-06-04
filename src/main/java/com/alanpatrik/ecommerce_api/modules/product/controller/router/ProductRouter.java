package com.alanpatrik.ecommerce_api.modules.product.controller.router;

import com.alanpatrik.ecommerce_api.modules.product.Product;
import com.alanpatrik.ecommerce_api.modules.product.controller.hander.ProductHandler;
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
public class ProductRouter {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/products",
                    produces = {APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    beanMethod = "getAll",
                    operation = @Operation(
                            operationId = "getAll",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "SUCCESSFUL OPERATION",
                                            content = {
                                                    @Content(schema = @Schema(
                                                            implementation = Product.class
                                                    ))
                                            }
                                    )
                            }
                    )
            ),

            @RouterOperation(
                    path = "/api/v1/products/{id}",
                    produces = {APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    beanMethod = "getById",
                    operation = @Operation(
                            operationId = "getById",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "SUCCESSFUL OPERATION",
                                            content = {
                                                    @Content(schema = @Schema(
                                                            implementation = Product.class
                                                    ))
                                            }
                                    ),

                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "NOT FOUND"
                                    )
                            },
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "id")
                            }
                    )
            ),

            @RouterOperation(
                    path = "/api/v1/products",
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
                                                            implementation = Product.class
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
                                                    implementation = Product.class
                                            ))
                                    }
                            )
                    )
            ),

            @RouterOperation(
                    path = "/api/v1/products/{id}",
                    produces = {APPLICATION_JSON_VALUE},
                    method = RequestMethod.PUT,
                    beanMethod = "update",
                    operation = @Operation(
                            operationId = "update",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "SUCCESSFUL OPERATION",
                                            content = {
                                                    @Content(schema = @Schema(
                                                            implementation = Product.class
                                                    ))
                                            }
                                    ),

                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "BAD REQUEST"
                                    ),

                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "BAD REQUEST"
                                    )
                            },

                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "id")
                            },

                            requestBody = @RequestBody(
                                    content = {
                                            @Content(schema = @Schema(
                                                    implementation = Product.class
                                            ))
                                    }
                            )
                    )
            ),

            @RouterOperation(
                    path = "/api/v1/products/{id}",
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
                                            responseCode = "400",
                                            description = "BAD REQUEST"
                                    ),

                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "BAD REQUEST"
                                    )
                            },

                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "id")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> router2(ProductHandler handler) {
        return route()
                .path("/api/v1/products", b1 -> b1
                        .nest(accept(APPLICATION_JSON), b2 -> b2
                                .GET("", handler::getAll)
                                .GET("/{id}", handler::getById)
                                .POST("", handler::create)
                                .PUT("/{id}", handler::update)
                                .PATCH("/update/storage/{id}", handler::updateStorage)
                                .DELETE("/{id}", handler::delete)))
                .build();
    }
}
