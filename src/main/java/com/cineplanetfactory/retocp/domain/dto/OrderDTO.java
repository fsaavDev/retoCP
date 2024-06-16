package com.cineplanetfactory.retocp.domain.dto;

import com.cineplanetfactory.retocp.domain.model.Order;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDTO {
    @JsonProperty("nro_pedido")
    private Long orderNumber;

    @JsonProperty("codigo_producto")
    private Long productCode;

    @JsonProperty("cantidad")
    private Integer quantity;

    @JsonProperty("cliente")
    private CustomerDTO customer;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class CustomerDTO {

        @JsonProperty("nombres")
        private String names;

        @JsonProperty("apellidos")
        private String lastnames;

        @JsonProperty("direccion")
        private String address;
    }
}
