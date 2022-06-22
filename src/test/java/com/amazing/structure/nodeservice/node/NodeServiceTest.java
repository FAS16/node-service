package com.amazing.structure.nodeservice.node;

import com.amazing.structure.nodeservice.node.exception.InvalidNodeException;
import com.amazing.structure.nodeservice.node.exception.NodeIsAncestorException;
import com.amazing.structure.nodeservice.node.exception.NodeNotFoundException;
import com.amazing.structure.nodeservice.node.exception.SelfParentingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class NodeServiceTest {

    @Autowired
    private NodeService nodeService;

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:Create_root.sql")
    void getDescendants() {
        // given
        long rootId = 1L;
        ArrayList<Node> nodes = new ArrayList<>();
        nodes.add(new Node(2L, 1L, 1L, 1L));
        nodes.add(new Node(3L, 2L, 1L, 2L));
        nodes.add(new Node(4L, 1L, 1L, 1L));
        nodes.add(new Node(5L, 4L, 1L, 2L));
        nodes.add(new Node(6L, 4L, 1L, 2L));
        nodes.add(new Node(7L, 6L, 1L, 3L));
        nodes.forEach((node) -> nodeService.createNode(node));

        // when
        List<Node> expectedDescendants = nodeService.getDescendants(rootId, Pageable.unpaged()).getContent();

        // then
        assertThat(expectedDescendants).isEqualTo(nodes);
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:Create_root.sql")
    void changeParent() {
        // given
        ArrayList<Node> nodes = new ArrayList<>();
        nodes.add(new Node(2L, 1L, 1L, 1L));
        nodes.add(new Node(3L, 2L, 1L, 2L));
        nodes.add(new Node(4L, 1L, 1L, 1L));
        nodes.add(new Node(5L, 4L, 1L, 2L));
        nodes.add(new Node(6L, 4L, 1L, 2L));
        nodes.add(new Node(7L, 6L, 1L, 3L));
        nodes.forEach((node) -> nodeService.createNode(node));

        // when
        nodeService.changeParent(6L, 3L);

        // then
        List<Node> descendantsOfNewParent = nodeService.getDescendants(3L, Pageable.unpaged()).getContent();
        List<Node> descendantsOfOldParent = nodeService.getDescendants(4L, Pageable.unpaged()).getContent();

        assertThat(descendantsOfOldParent).isEqualTo(nodes.stream().filter(n -> n.getId() == 5).collect(Collectors.toList()));
        assertThat(descendantsOfNewParent.stream()
                .filter(n -> n.getId() == 6L || n.getId() == 7L)
                .count()).isEqualTo(2);

    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:Create_root.sql")
    void createNode() {

        // given
        long rootId = 1L;
        Node node = new Node(2L, rootId, rootId, 1L);

        // when
        nodeService.createNode(node);

        // then
        Node newChild = nodeService.getNodeById(node.getId());
        assertThat(newChild).isEqualTo(node);

    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:Create_root.sql")
    void isDescendantOf() {
        // given
        long rootId = 1L;
        Node firstChild = new Node(2L, rootId, rootId, 1L);
        Node secondChild = new Node(3L, rootId, rootId, 1L);
        nodeService.createNode(firstChild);
        nodeService.createNode(secondChild);

        // when
        boolean isDescendant1 = nodeService.isDescendantOf(rootId, 2L);
        boolean isDescendant2 = nodeService.isDescendantOf(rootId, 3L);
        boolean isNotDescendant1 = nodeService.isDescendantOf(2L, rootId);
        boolean isNotDescendant2 = nodeService.isDescendantOf(3L, rootId);
        boolean isNotDescendant3 = nodeService.isDescendantOf(2L, 3L);
        boolean isNotDescendant4 = nodeService.isDescendantOf(3L, 2L);

        // then
        assertThat(isDescendant1).isTrue();
        assertThat(isDescendant2).isTrue();
        assertThat(isNotDescendant1).isFalse();
        assertThat(isNotDescendant2).isFalse();
        assertThat(isNotDescendant3).isFalse();
        assertThat(isNotDescendant4).isFalse();

    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:Create_root.sql")
    public void changeParentToDescendant() {
        ArrayList<Node> nodes = new ArrayList<>();
        nodes.add(new Node(2L, 1L, 1L, 1L));
        nodes.add(new Node(3L, 2L, 1L, 2L));
        nodes.add(new Node(4L, 1L, 1L, 1L));
        nodes.add(new Node(5L, 4L, 1L, 2L));
        nodes.add(new Node(6L, 4L, 1L, 2L));
        nodes.add(new Node(7L, 6L, 1L, 3L));
        nodes.forEach((node) -> nodeService.createNode(node));

        assertThrows(NodeIsAncestorException.class, () -> nodeService.changeParent(4L, 7L));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:Create_root.sql")
    public void changeParentToNonExistingNode() {
        nodeService.createNode(new Node(2L, 1L, 1L, 1L));
        assertThrows(NodeNotFoundException.class, () -> {
            nodeService.changeParent(2L, 4L);
        });
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:Create_root.sql")
    public void changeParentToSelf() {
        assertThrows(SelfParentingException.class, () -> {
            nodeService.changeParent(2L, 2L);
        });
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:Create_root.sql")
    public void createNodeWithNonExistingRoot() {
        assertThrows(InvalidNodeException.class, () -> {
            nodeService.createNode(new Node(2L, 1L, 66L, 1L));
        });
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:Create_root.sql")
    public void createNodeWithNonExistingParent() {
        assertThrows(InvalidNodeException.class, () -> {
            nodeService.createNode(new Node(2L, 66L, 1L, 1L));
        });
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:Create_root.sql")
    public void createNodeWithAlreadyExistingNode() {
        assertThrows(InvalidNodeException.class, () -> {
            nodeService.createNode(new Node(1L, null, 1L, 0L));
        });
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:Create_root.sql")
    public void createNodeWithNonRoot() {
        nodeService.createNode(new Node(2L, 1L, 1L, 1L));
        assertThrows(InvalidNodeException.class, () -> {
            nodeService.createNode(new Node(1L, 2L, 2L, 0L));
        });
    }
}