package com.ajopaul.qantas.customerprofile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ResponseData<T> {
    @JsonIgnore
    public static final String SOMETHING_WENT_WRONG = "Something went wrong";
    private T data;
    private Error error;

    @JsonIgnore
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
