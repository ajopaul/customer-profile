package com.ajopaul.qantas.customerprofile;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ResponseData {
    public Object data;
    public Error error;

    public static ResponseData success(Object data){
        return ResponseData.builder().data(data).build();
    }

    public static ResponseData error(HttpStatus status, String shortMessage, String detailMessage){
        return ResponseData.builder()
                .error(Error.builder().title(shortMessage)
                        .detail(detailMessage)
                        .status(status.toString())
                        .build())
                .build();
    }

    @Builder
    @Data
    public static class Error {
        private String title;
        private String status;
        private String detail;
    }
}
