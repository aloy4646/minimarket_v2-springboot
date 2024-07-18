package com.appschef.intern.minimarket.dto.request.stock;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStockRequest {
    @NotBlank(message = "type must be provided")
    @Size(max = 15, message = "type cannot exceed 100 character")
    @JsonProperty("type")
    private String type;

    @NotNull(message = "quantity must be provided")
    @JsonProperty("quantity")
    private Integer quantity;
}
