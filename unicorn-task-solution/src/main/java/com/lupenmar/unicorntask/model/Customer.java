package com.lupenmar.unicorntask.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;

    @NotNull(message = "Name cannot be null")
    @Schema(example = "Chipi chapa")
    private String name;

    @Column(unique = true)
    @NotNull(message = "Email cannot be null")
    @Schema(example = "chipi@gmail.com")
    private String email;

    @NotNull(message = "Phone number cannot be null")
    @Schema(example = "111-222-333")
    private String phoneNumber;

    @OneToMany(mappedBy = "rentedBy", cascade = CascadeType.ALL)
    @JsonIgnore
    @Schema(hidden = true)
    private List<Car> rentedCars;

    public Customer(Long id, String name, String email, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
