package com.ajopaul.qantas.customerprofile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ResponseData<T> {
    @JsonIgnore
    public static final String SOMETHING_WENT_WRONG = "Something went wrong";
    @ApiModelProperty(notes = "Contains Data from the response")
    private T data;
    @ApiModelProperty(notes = "Contains details of Error")
    private Error error;

    @JsonIgnore
    public static ResponseData<?> error(HttpStatus status, String shortMessage, String detailMessage){
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
        @ApiModelProperty(notes = "HTTP Status Code")
        private String status;
        @ApiModelProperty(notes = "Short title of the error.")
        private String title;
        @ApiModelProperty(notes = "Detail message for the error")
        private String detail;
    }
}
