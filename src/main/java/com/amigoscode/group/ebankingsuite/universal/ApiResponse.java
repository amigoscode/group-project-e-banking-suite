package com.amigoscode.group.ebankingsuite.universal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ApiResponse {

    private String message;
    private Object data;


    public ApiResponse(String message) {
        this.message = message;
    }
}
