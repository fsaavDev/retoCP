package com.cineplanetfactory.retocp.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductSaveReq {
    @JsonProperty("codigo")
    private Long code;

    @JsonProperty("nombre")
    private String name;

    @JsonProperty("precio")
    private BigDecimal price;
}
