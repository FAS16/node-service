package com.amazing.structure.nodeservice.rest.exception;

import com.amazing.structure.nodeservice.node.NodeService;
import com.amazing.structure.nodeservice.node.exception.InvalidNodeException;
import com.amazing.structure.nodeservice.node.exception.NodeIsAncestorException;
import com.amazing.structure.nodeservice.node.exception.SelfParentingException;
import com.amazing.structure.nodeservice.node.exception.NodeNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionMapper {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(NodeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNodeNotFoundException(NodeNotFoundException e) {
        return logAndGetResponse(HttpStatus.NOT_FOUND.value(), e);
    }

    @ExceptionHandler(SelfParentingException.class)
    public ResponseEntity<ErrorResponse> handleSelfParentingException(SelfParentingException e) {
        return logAndGetResponse(HttpStatus.BAD_REQUEST.value(), e);
    }

    @ExceptionHandler(NodeIsAncestorException.class)
    public ResponseEntity<ErrorResponse> handleNodeIsAncestorException(NodeIsAncestorException e) {
        return logAndGetResponse(HttpStatus.BAD_REQUEST.value(), e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return logAndGetResponse(HttpStatus.BAD_REQUEST.value(), e);
    }

    @ExceptionHandler(InvalidNodeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidNodeException(InvalidNodeException e) {
        return logAndGetResponse(HttpStatus.BAD_REQUEST.value(), e);
    }

    private ResponseEntity<ErrorResponse> logAndGetResponse(int errorCode, Exception e) {
        ErrorResponse response = new ErrorResponse(errorCode, e.getMessage());
        logger.error(e.getMessage(), e);
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode));
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class ErrorResponse {
        private Integer errorCode;
        private String message;
    }
}
