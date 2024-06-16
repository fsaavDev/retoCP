package com.cineplanetfactory.retocp.domain.request;

import com.cineplanetfactory.retocp.domain.dto.ProductDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class OrderSaveReq {
    @JsonProperty("codigo_producto")
    @NotNull(message = "El código del producto no puede ser nulo")
    @Positive(message = "El código del producto debe ser un número positivo")
    private Long productCode;

    @JsonProperty("cantidad")
    @NotNull(message = "La cantidad no puede ser nula")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer quantity;

    @JsonProperty("cliente_nombres")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 255, message = "El nombre no puede exceder los 255 caracteres y deben ser al menos 2 caracteres")
    private String names;

    @JsonProperty("cliente_apellidos")
    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(min = 2, max = 255, message = "El apellido no puede exceder los 255 caracteres y deben ser al menos 2 caracteres")
    private String lastnames;

    @JsonProperty("cliente_direccion")
    @NotBlank(message = "La dirección no puede estar vacía")
    @Size(min = 2, max = 255, message = "La dirección no puede exceder los 255 caracteres y deben ser al menos 2 caracteres")
    private String address;
}
