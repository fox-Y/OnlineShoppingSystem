package org.example.onlineshoppingsystem.common.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpsert {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    @PositiveOrZero
    private Integer quantity;

    @NotNull @DecimalMin("0.00")
    private BigDecimal retailPrice;

    @NotNull @DecimalMin("0.00")
    private BigDecimal wholesalePrice;
}
