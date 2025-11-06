package org.example.onlineshoppingsystem.common.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailRes {

    private Long id;
    private String name;
    private String description;
    private Integer quantity;               // 用户视图下为 null
    private BigDecimal retailPrice;
    private BigDecimal wholesalePrice;      // 用户视图下为 null

    /** 用户视图：不暴露库存与进价 */
    public static ProductDetailRes user(Long id, String name, String description, BigDecimal retailPrice) {
        return new ProductDetailRes(id, name, description, null, retailPrice, null);
    }

    /** 管理员视图：完整字段 */
    public static ProductDetailRes admin(Long id, String name, String description,
                                         Integer quantity, BigDecimal retailPrice, BigDecimal wholesalePrice) {
        return new ProductDetailRes(id, name, description, quantity, retailPrice, wholesalePrice);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdminUpsert {
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
}
