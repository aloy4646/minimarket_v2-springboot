package com.appschef.intern.minimarket.dto.request.members;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopMemberRequest {
    @NotBlank(message = "member_number must be provided")
    @Size(max = 7, message = "member_number cannot exceed 7 characters")
    @JsonProperty("member_number")
    private String memberNumber;
}
