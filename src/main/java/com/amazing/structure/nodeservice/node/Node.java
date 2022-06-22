package com.amazing.structure.nodeservice.node;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "node")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Node {

    @Id
    @Column(name = "id")
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "root_id", nullable = false)
    private Long rootId;

    @Column(name = "height", nullable = false)
    private Long height;
}
