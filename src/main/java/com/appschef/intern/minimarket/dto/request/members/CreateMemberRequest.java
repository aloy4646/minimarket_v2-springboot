package com.appschef.intern.minimarket.dto.request.members;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMemberRequest {
    @NotBlank(message = "full_name must be provided")
    @Size(max = 100, message = "full_name cannot exceed 100 characters")
    @JsonProperty("full_name")
    private String fullName;

    @NotBlank(message = "email must be provided")
    @Size(max = 360, message = "email cannot exceed 360 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
    @JsonProperty("email")
    private String email;

    @NotBlank(message = "phone_number must be provided")
    @Size(max = 13, message = "phone_number cannot exceed 13 characters")
    @Pattern(regexp = "^(\\+62|62|0)[0-9]{9,13}$", message = "Invalid phone number format")
    @JsonProperty("phone_number")
    private String phoneNumber;

    @NotBlank(message = "address must be provided")
    @JsonProperty("address")
    private String address;
}
