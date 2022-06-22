package com.amazing.structure.nodeservice.node.dto;
import lombok.Builder;
import lombok.Data;


@Data
@Builder(toBuilder = true)
public class NodeDTO {

    private Long id;
    private Long parentId;
    private Long rootId;
    private Long height;

}
