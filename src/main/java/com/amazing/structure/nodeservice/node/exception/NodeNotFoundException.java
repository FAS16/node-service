package com.amazing.structure.nodeservice.node.exception;

public class NodeNotFoundException extends RuntimeException{

    public NodeNotFoundException(Long nodeId) {
        super(String.format("Node with id '%s' not found", nodeId));
    }
}