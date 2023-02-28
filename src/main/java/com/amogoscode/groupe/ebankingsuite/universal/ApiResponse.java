package com.amogoscode.groupe.ebankingsuite.universal;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class ApiResponse {

    private String message;
    private Object data;

    public ApiResponse(String message, String data) {
        this.message = message;
        this.data = data;
    }

    public ApiResponse(String message) {
        this.message = message;
    }
}
