package com.lupenmar.unicorntask.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;

    @Schema(example = "Toyota")
    @NotNull(message = "Brand cannot be null")
    private String brand;

    @Schema(example = "New")
    @NotNull(message = "Model cannot be null")
    private String model;

    @Schema(example = "2021")
    @NotNull(message = "Year cannot be null")
    private int year;

    @Schema(example = "Black")
    private String color;

    @Schema(example = "60000")
    @NotNull(message = "Mileage cannot be null")
    private int mileage;

    @Schema(hidden = true)
    private boolean rented;

    @ManyToOne
    @JoinColumn(name = "rented_by_id")
    @Schema(hidden = true)
    private Customer rentedBy;

}
