package Grocify.GrocifyNear.dto.RequestDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {
    @NotBlank(message = "Product name is required")
    private String productName;
    @NotNull(message = "Cost is required")
    @Min(value = 0, message = "Cost cannot be negative")
    private Long cost;
}
