package Grocify.GrocifyNear.dto.ResponseDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private String userName;
    private String email;
    private String phoneNo;
    private String gender;
    private String age;
}
