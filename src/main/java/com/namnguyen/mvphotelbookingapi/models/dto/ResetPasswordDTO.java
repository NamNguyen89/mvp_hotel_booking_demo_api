package com.namnguyen.mvphotelbookingapi.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResetPasswordDTO {

    @NotBlank(message = "Password cannot be empty")
    private String oldPassword;

    @NotBlank(message = "Password cannot be empty")
    private String newPassword;

    @NotBlank(message = "Password cannot be empty")
    private String confirmNewPassword;

}
