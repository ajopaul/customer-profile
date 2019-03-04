package com.ajopaul.qantas.customerprofile;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ResponseData {
    public Object data;
    public Error error;

    private ResponseData(Object data) {
        this.data = data;
    }

    private ResponseData(HttpStatus status, String shortMessage, String detailMessage){
        this.error = Error.builder().title(shortMessage)
                .detail(detailMessage)
                .status(status.toString())
                .build();

    }

    public static ResponseData success(Object data){
        return new ResponseData(data);
    }

    public static ResponseData error(HttpStatus status, String shortMessage, String detailMessage){
        return new ResponseData(status, shortMessage, detailMessage);
    }

    @Builder
    @Data
    public static class Error {
        private String title;
        private String status;
        private String detail;
    }
}
