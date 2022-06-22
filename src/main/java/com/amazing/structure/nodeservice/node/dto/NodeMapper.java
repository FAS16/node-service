package com.amazing.structure.nodeservice.node.dto;

import com.amazing.structure.nodeservice.node.Node;
import com.amazing.structure.nodeservice.node.dto.CreateNodeDTO;
import com.amazing.structure.nodeservice.node.dto.NodeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NodeMapper {

    public NodeDTO toDTO(Node node) {
        return NodeDTO.builder()
                .id(node.getId())
                .parentId(node.getParentId())
                .rootId(node.getRootId())
                .height(node.getHeight())
                .build();
    }

    public Node toModel(CreateNodeDTO dto) {
        return Node.builder()
                .id(dto.getId())
                .parentId(dto.getParentId())
                .rootId(dto.getRootId())
                .build();
    }

    public Page<NodeDTO> toDTO(Page<Node> nodes) {
        return nodes.map(this::toDTO);
    }
}
