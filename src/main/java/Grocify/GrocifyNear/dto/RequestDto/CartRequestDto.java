package Grocify.GrocifyNear.dto.RequestDto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartRequestDto {
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
    @Min(value = 0, message = "Price cannot be negative")
    private double price;
}
