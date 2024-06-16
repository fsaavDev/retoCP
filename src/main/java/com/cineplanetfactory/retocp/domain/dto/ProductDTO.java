package com.cineplanetfactory.retocp.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductDTO {

    @JsonProperty("codigo")
    private Long code;

    @JsonProperty("nombre")
    private String name;

    @JsonProperty("precio")
    private BigDecimal price;
}
