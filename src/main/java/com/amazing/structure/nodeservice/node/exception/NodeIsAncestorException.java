package com.amazing.structure.nodeservice.node.exception;

public class NodeIsAncestorException extends RuntimeException{
        public NodeIsAncestorException(Long ancestorId, Long descendantId) {
        super(String.format("Parent change not allowed. Node with id '%s' is ancestor of node with id '%s'", ancestorId, descendantId));
    }

}
