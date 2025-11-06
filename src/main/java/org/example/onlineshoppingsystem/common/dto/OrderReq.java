package org.example.onlineshoppingsystem.common.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderReq {

    @NotNull
    @Size(min = 1)
    @Valid
    private List<Item> order;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        @NotNull
        private Long productId;

        @NotNull
        @Positive
        private Integer quantity;
    }
}
