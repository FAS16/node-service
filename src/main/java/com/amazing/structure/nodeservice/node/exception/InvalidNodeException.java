package com.amazing.structure.nodeservice.node.exception;

public class InvalidNodeException extends RuntimeException {

    public InvalidNodeException(Long nodeId, String message) {
        super(String.format(message + ". id: '%s'", nodeId));
    }
}
