package com.amazing.structure.nodeservice.node;

import com.amazing.structure.nodeservice.node.dto.ChangeParentDTO;
import com.amazing.structure.nodeservice.node.dto.CreateNodeDTO;
import com.amazing.structure.nodeservice.node.dto.NodeDTO;
import com.amazing.structure.nodeservice.node.dto.NodeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/node")
@RequiredArgsConstructor
public class NodeController  {

    private final NodeService nodeService;
    private final NodeMapper nodeMapper;

    @GetMapping(path = "/{nodeId}")
    public NodeDTO getNode(@PathVariable Long nodeId) {
        return nodeMapper.toDTO(nodeService.getNodeById(nodeId));
    }

    @GetMapping(path = "/{nodeId}/descendants")
    public Page<NodeDTO> getDescendants(@PathVariable Long nodeId, Pageable pageable) {
        return nodeMapper.toDTO(nodeService.getDescendants(nodeId, pageable));
    }

    @PutMapping(path = "/{nodeId}")
    public void changeParent(@PathVariable Long nodeId, @Valid @RequestBody ChangeParentDTO dto) {
        nodeService.changeParent(nodeId, dto.getParentId());
    }

    @PostMapping()
    public NodeDTO createNode(@RequestBody CreateNodeDTO dto) {
        return nodeMapper.toDTO(nodeService.createNode(nodeMapper.toModel(dto)));
    }
}