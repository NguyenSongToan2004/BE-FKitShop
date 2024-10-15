package com.group4.FKitShop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseObjectRegis {
    private int status;
    private String message;
    private Object data;
}