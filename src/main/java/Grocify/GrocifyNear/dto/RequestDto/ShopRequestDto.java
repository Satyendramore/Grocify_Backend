package Grocify.GrocifyNear.dto.RequestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopRequestDto {
    @NotBlank(message = "Shop name is required")
    private String shopName;
    @NotBlank(message = "Address is required")
    private String address;
    @NotNull(message = "Longitude is required")
    private Double longitude;
    @NotNull(message = "Latitude is required")
    private Double latitude;
}
