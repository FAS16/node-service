package com.amazing.structure.nodeservice.node;

import com.amazing.structure.nodeservice.node.exception.InvalidNodeException;
import com.amazing.structure.nodeservice.node.exception.NodeIsAncestorException;
import com.amazing.structure.nodeservice.node.exception.SelfParentingException;
import com.amazing.structure.nodeservice.node.exception.NodeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NodeService {

    Logger logger = LoggerFactory.getLogger(NodeService.class);
    private final NodeRepository nodeRepository;

    @Transactional
    public Node createNode(Node node) {

        Optional<Node> existingNode = nodeRepository.findById(node.getId());
        if(existingNode.isPresent()) {
            throw new InvalidNodeException(node.getId(), "Node already exists");
        }

        Optional<Node> parent = nodeRepository.findById(node.getParentId());
        if(parent.isEmpty()) {
            throw new InvalidNodeException(node.getParentId(), "No such node for parenting");
        }

        Optional<Node> root = nodeRepository.findById(node.getRootId());
        if(root.isEmpty() && node.getParentId() > 0) {
            throw new InvalidNodeException(node.getRootId(), "Invalid root");
        }

        // Create node entry
        node.setHeight(0L);
        nodeRepository.save(node);

        // Create node relations
        nodeRepository.createNodeRelations(node.getId(), node.getParentId());

        // Set height
        node.setHeight(nodeRepository.getHeight(node.getId()));
        return nodeRepository.save(node);
    }
    public Page<Node> getDescendants(Long nodeId, Pageable pageable) {
        Node existingNode = getNodeById(nodeId);
        Page<Node> descendants = nodeRepository.findDescendants(existingNode.getId(), pageable);
        return descendants;
    }

    @Transactional
    public void changeParent(Long nodeId, Long newParentId) {

        if(nodeId.longValue() == newParentId.longValue()) {
            throw new SelfParentingException();
        }

        // Get nodes from database and check for existence
        Node node = getNodeById(nodeId);
        Node newParent = getNodeById(newParentId);

        // Moving a node to a descendant will disconnect the involved nodes from the three
        if(isDescendantOf(nodeId, newParentId)) {
            throw new NodeIsAncestorException(nodeId, newParentId);
        }

        // Delete all node_relations (paths) that end at descendants of the node and that begin at ancestors of the node.
        nodeRepository.disconnectSubtree(node.getId());

        // Insert new node_relations (paths) for new ancestors and current descendants of the node.
        nodeRepository.insertSubtree(node.getId(), newParent.getId());

        // Update node with new attributes and save
        long oldParentId = node.getParentId();
        node.setParentId(newParentId);
        node.setHeight(nodeRepository.getHeight(node.getId()));
        nodeRepository.save(node);

        logger.info(String.format("Changed parent of node with id '%s'. Old parent id: '%s'. New parent id: '%s'", nodeId, oldParentId, newParentId));
    }

    public Node getNodeById(Long nodeId) {
        return nodeRepository.findById(nodeId).orElseThrow(() -> new NodeNotFoundException(nodeId));
    }

    public boolean isDescendantOf(Long ancestorId, Long descendantId) {
        return nodeRepository.isDescendantOf(ancestorId, descendantId);
    }
}
