package com.cineplanetfactory.retocp.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
public class ProductSaveReq {
    @JsonProperty("nombre")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 1, max = 255, message = "El nombre no puede exceder los 255 caracteres y debe tener al menos 1 caracter")
    private String name;

    @JsonProperty("precio")
    @NotNull(message = "El precio no puede ser nulo")
    @Digits(integer = 4, fraction = 2, message = "El precio debe tener un máximo de 4 enteros y 2 decimales")
    @DecimalMin(value = "0.10", inclusive = true, message = "El precio debe ser mayor o igual que 0.10 centimos")
    private BigDecimal price;
}
