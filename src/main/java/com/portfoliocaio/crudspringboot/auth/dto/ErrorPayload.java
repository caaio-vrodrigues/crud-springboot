package com.portfoliocaio.crudspringboot.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ErrorPayload implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String timestamp;
    private final int status;
    private final String message;
    private final String path;

    private ErrorPayload(
    	String timestamp, int status, String message, String path) 
    {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.path = path;
    }

    public static ErrorPayload of(int status, String message, String path) {
        return new ErrorPayload(Instant.now().toString(), status, message, path);
    }

    public String getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public String getPath() { return path; }
}
