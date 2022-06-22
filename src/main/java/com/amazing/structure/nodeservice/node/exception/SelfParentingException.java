package com.amazing.structure.nodeservice.node.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SelfParentingException extends RuntimeException{

    public SelfParentingException() {
        super("Parent change can only occur for two different nodes");
    }
}
