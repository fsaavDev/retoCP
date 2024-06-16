package com.cineplanetfactory.retocp.domain.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

/**
 * @author fsaav
 * Tabla Productos para retoCP
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "PRODUCTOS")
public class Product {
    /**
     * Identificador único del producto.
     */
    @Id
    @Column(name = "codigo",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code;

    /**
     * Nombre del producto.
     */
    @Column(name = "nombre", nullable = false)
    private String name;

    /**
     * Precio del producto.
     * BigDecimal para precision de decimales, por buena practica le asigno a precios, costos y totales.
     * Otros casos utilizo Double para velocidad de calculos
     */
    @Column(name = "precio", precision = 6, scale = 2, nullable = false)
    private BigDecimal price;
}
