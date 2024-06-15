package com.cineplanetfactory.retocp.domain.request;

import com.cineplanetfactory.retocp.domain.dto.ProductDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSaveReq {
    @JsonProperty("producto")
    private ProductDTO product;

    @JsonProperty("cantidad")
    private Integer quantity;

    @JsonProperty("cliente_nombres")
    private String names;

    @JsonProperty("cliente_apellidos")
    private String lastnames;

    @JsonProperty("cliente_direccion")
    private String address;
}
