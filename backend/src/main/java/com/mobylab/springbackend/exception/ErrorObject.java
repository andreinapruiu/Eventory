package com.mobylab.springbackend.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorObject {
    @JsonProperty("error_code")
    private Integer statusCode;
    @JsonProperty("error_message")
    private String message;
    @JsonProperty("errors")
    private Map<String, String> errors;
    @JsonProperty("error_timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public Integer getStatusCode() {
        return statusCode;
    }

    public ErrorObject setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ErrorObject setMessage(String message) {
        this.message = message;
        return this;
    }
    
    public Map<String, String> getErrors() {
        return errors;
    }

    public ErrorObject setErrors(Map<String, String> errors) {
        this.errors = errors;
        return this;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public ErrorObject setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
