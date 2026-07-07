package KTB4_gourmet_Week9.Assignment.dto;

import KTB4_gourmet_Week9.Assignment.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {

    private UserResponseDto user;

    public static LoginResponseDto of(User user) {
        return new LoginResponseDto(
                new UserResponseDto(user)
        );
    }
}