package com.appschef.intern.minimarket.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ApplicationProperties {
    @Value("${transactionPoint}")
    private Integer transactionPoint;
}
