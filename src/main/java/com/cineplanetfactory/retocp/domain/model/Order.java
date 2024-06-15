package com.cineplanetfactory.retocp.domain.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;

/**
 * @author fsaav
 * Tabla Pedidos para retoCP
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PEDIDOS")
public class Order {
    /**
     * Identificador único del Pedido.
     */
    @Id
    @Column(name = "nro_pedido",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderNumber;

    /**
     * Relacion muchos a uno de Pedido con Producto.
     * Un producto puede ser referenciado en multiples Pedidos a traves de la columna codigo_producto
     */
    @ManyToOne
    @JoinColumn(name = "codigo_producto",nullable = false, foreignKey = @ForeignKey(name = "fk_pedido_producto"))
    private Product product;

    /**
     * Cantidad de Productos del Pedido.
     */
    @Column(name = "cantidad", nullable = false)
    private Integer quantity;

    /**
     * Objeto Cliente del Pedido.
     * Se le hace Override a los atributos para darles un nombre de columnba personalizado
     */
    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "names", column = @Column(name = "cliente_nombres", nullable = false)),
            @AttributeOverride(name = "lastnames", column = @Column(name = "cliente_apellidos", nullable = false)),
            @AttributeOverride(name = "address", column = @Column(name = "cliente_direccion", nullable = false)),
    })
    private Customer customer;

    /**
     * Clase Cliente de tipo Embeddable.
     * Esta clase asignara las columnas complementarias de la clase Pedido
     */
    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class Customer {
        /**
         * Nombres del cliente del Pedido.
         */
        private String names;
        /**
         * Apellidos del cliente del Pedido.
         */
        private String lastnames;
        /**
         * Direccion del cliente del Pedido.
         */
        private String address;
    }

}
