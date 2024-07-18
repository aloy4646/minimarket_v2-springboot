package com.appschef.intern.minimarket.dto.response.members;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDetailResponse {
    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("member_number")
    private String memberNumber;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("address")
    private String address;

    @JsonProperty("point")
    private Long point;
}
