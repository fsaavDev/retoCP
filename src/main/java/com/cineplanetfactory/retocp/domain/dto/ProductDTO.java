package com.cineplanetfactory.retocp.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    @JsonProperty("codigo")
    private Long code;

    @JsonProperty("nombre")
    private String name;

    @JsonProperty("precio")
    private BigDecimal price;
}
