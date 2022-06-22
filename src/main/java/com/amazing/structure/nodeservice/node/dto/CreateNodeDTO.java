package com.amazing.structure.nodeservice.node.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder(toBuilder = true)
public class CreateNodeDTO {

    @NotNull
    private Long id;
    @NotNull
    private Long parentId;
    @NotNull
    private Long rootId;
}
