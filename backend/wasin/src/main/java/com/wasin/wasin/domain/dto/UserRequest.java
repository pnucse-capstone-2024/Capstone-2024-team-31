package com.wasin.wasin.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserRequest {

    private static final String emailPattern = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
    private static final String passwordPattern = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!~`<>,./?;:'\"\\[\\]{}\\\\()|_-])\\S*$";

    public record SignUpDTO (

            @NotEmpty(message = "역할은 비어있으면 안됩니다.")
            String role,

            @NotEmpty(message = "이름은 비어있으면 안됩니다.")
            @Size(min = 2, max = 10, message = "이름은 2에서 10자 이내여야 합니다.")
            String username,

            @NotEmpty(message = "이메일은 비어있으면 안됩니다.")
            @Size(max = 255, message = "이메일은 255자 이내여야 합니다.")
            @Pattern(regexp = emailPattern, message = "이메일 형식으로 작성해주세요")
            String email,

            @NotEmpty(message = "패스워드는 비어있으면 안됩니다.")
            @Size(min = 8, max = 20, message = "패스워드는 8에서 20자 이내여야 합니다.")
            @Pattern(regexp = passwordPattern, message = "영문, 숫자, 특수문자가 포함되어야하고 공백이 포함될 수 없습니다.")
            String password,

            @NotEmpty(message = "패스워드2는 비어있으면 안됩니다.")
            @Size(min = 8, max = 20, message = "패스워드는 8에서 20자 이내여야 합니다.")
            @Pattern(regexp = passwordPattern, message = "영문, 숫자, 특수문자가 포함되어야하고 공백이 포함될 수 없습니다.")
            String password2
    ) {
    }

    public record LoginDTO(
            @NotEmpty(message = "이메일은 비어있으면 안됩니다.")
            @Size(max = 255, message = "이메일은 255자 이내여야 합니다.")
            @Pattern(regexp = emailPattern, message = "이메일 형식으로 작성해주세요")
            String email,

            @NotEmpty(message = "패스워드는 비어있으면 안됩니다.")
            @Size(min = 8, max = 20, message = "8에서 20자 이내여야 합니다.")
            @Pattern(regexp = passwordPattern, message = "영문, 숫자, 특수문자가 포함되어야하고 공백이 포함될 수 없습니다.")
            String password,

            @NotEmpty(message = "FCM 토큰은 비어있으면 안됩니다.")
            @Size(max = 255, message = "100자 이내여야 합니다.")
            String fcmToken
    ) {
    }

    public record EmailDTO(
            @NotEmpty(message = "이메일은 비어있으면 안됩니다.")
            @Size(max = 255, message = "이메일은 255자 이내여야 합니다.")
            @Pattern(regexp = emailPattern, message = "이메일 형식으로 작성해주세요")
            String email
    ) {
    }

    public record EmailCheckDTO(
            @NotEmpty(message = "이메일은 비어있으면 안됩니다.")
            @Size(max = 255, message = "이메일은 255자 이내여야 합니다.")
            @Pattern(regexp = emailPattern, message = "이메일 형식으로 작성해주세요")
            String email,

            @NotEmpty(message = "이메일 코드는 비어있으면 안됩니다.")
            String code
    ) {
    }

    public record ReissueDTO(
            @NotEmpty(message = "refresh 토큰은 비어있으면 안됩니다.")
            String refreshToken
    ) {
    }

    public record LockDTO(
            @NotEmpty(message = "잠금해제 비밀번호는 비어있으면 안됩니다.")
            @Pattern(regexp = "\\d{4}", message = "잠금해제 비밀번호는 4자리 숫자로 구성되어야 합니다.")
            String password
    ) {
    }

    public record LockConfirmDTO(
            @NotEmpty(message = "잠금해제 비밀번호는 비어있으면 안됩니다.")
            @Pattern(regexp = "\\d{4}", message = "잠금해제 비밀번호는 4자리 숫자로 구성되어야 합니다.")
            String password
    ) {
    }
}
