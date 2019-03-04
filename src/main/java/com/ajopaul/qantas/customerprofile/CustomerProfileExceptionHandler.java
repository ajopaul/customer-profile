package com.ajopaul.qantas.customerprofile;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Optional;

@ControllerAdvice
public class CustomerProfileExceptionHandler extends ResponseEntityExceptionHandler {


    public static final String SOMETHING_WENT_WRONG = "Something went wrong";

    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<?> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e, WebRequest request) {
        String error = e.getName() +
                " should be of type " +
                Optional.ofNullable(e.getRequiredType()).map(Class::getSimpleName).orElse("");

        return new ResponseEntity<>(ResponseData.error(HttpStatus.BAD_REQUEST, SOMETHING_WENT_WRONG, error)
                , new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException e, HttpHeaders headers
            , HttpStatus status, WebRequest request) {

        String error = "Invalid URL " + e.getHttpMethod() + " " + e.getRequestURL();
        return new ResponseEntity<>(ResponseData.error(HttpStatus.NOT_FOUND, SOMETHING_WENT_WRONG, error)
                , new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported( HttpRequestMethodNotSupportedException e
            ,  HttpHeaders headers,  HttpStatus status,  WebRequest request) {

        StringBuilder builder = new StringBuilder();
        builder.append(e.getMethod());
        builder.append(" method Not supported. Supported methods are ");
        Optional.ofNullable(e.getSupportedHttpMethods()).map(t -> builder.append(t).append(" "));

        return new ResponseEntity<>(ResponseData.error(HttpStatus.METHOD_NOT_ALLOWED, SOMETHING_WENT_WRONG, builder.toString())
                , new HttpHeaders(),HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = ex.getMessage();
        if (message != null) {
            message = message.substring(0, message.indexOf(";"));
        }
        StringBuilder builder = new StringBuilder()
                .append(message)
                .append("\n");

        return new ResponseEntity<>(ResponseData.error(HttpStatus.BAD_REQUEST, SOMETHING_WENT_WRONG, builder.toString())
                , new HttpHeaders(),HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append("Content type: ")
                .append("\'")
                .append(request.getHeader("Content-Type"))
                .append("\'")
                .append(" Not supported. Supported content type: \'application/json\'");

        return new ResponseEntity<>(ResponseData.error(HttpStatus.BAD_REQUEST, SOMETHING_WENT_WRONG, builder.toString())
                , new HttpHeaders(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll( Exception e,  WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String errMsg = e.getMessage();

        return new ResponseEntity<>(ResponseData.error(status, SOMETHING_WENT_WRONG, errMsg), new HttpHeaders(), status);
    }

}
