package com.appschef.intern.minimarket.dto.response.members;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoUploadResponse {
    @JsonProperty("fileName")
    private String fileName;

    @JsonProperty("size")
    private Long size;

    @JsonProperty("download_url")
    private String downloadUrl;

}
