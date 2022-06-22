package com.amazing.structure.nodeservice.node;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NodeRepository extends JpaRepository<Node, Long> {

    @Query(value = "SELECT n.* FROM node n " +
            "JOIN node_relation nr " +
            "ON (n.id = nr.descendant_id) " +
            "WHERE nr.ancestor_id = ?1 AND n.id != ?1",
            countQuery = "SELECT count(*) FROM node n " +
                    "JOIN node_relation nr " +
                    "ON (n.id = nr.descendant_id) " +
                    "WHERE nr.ancestor_id = ?1 AND n.id != ?1",
            nativeQuery = true)
    Page<Node> findDescendants(Long nodeId, Pageable pageable);

    @Query(value = "SELECT CASE WHEN count(*)>0 THEN 'true' ELSE 'false' END FROM node n " +
            "JOIN node_relation nr " +
            "ON (n.id = nr.descendant_id) " +
            "WHERE nr.ancestor_id = ?1 AND n.id != ?1 AND n.id = ?2",
             nativeQuery = true)
    boolean isDescendantOf(Long ancestorId, Long descendantId);

    @Modifying
    @Query(value = "DELETE a FROM node_relation AS a " +
            "JOIN node_relation AS d ON a.descendant_id = d.descendant_id " +
            "LEFT JOIN node_relation AS n " +
            "ON n.ancestor_id = d.ancestor_id and n.descendant_id = a.ancestor_id " +
            "WHERE d.ancestor_id = ?1 AND n.ancestor_id IS NULL"
            , nativeQuery = true)
    void disconnectSubtree(Long nodeId);

    @Modifying
    @Query(value = "INSERT INTO node_relation (ancestor_id, descendant_id, length) " +
            "SELECT supertree.ancestor_id, subtree.descendant_id, supertree.length + subtree.length + 1 AS length " +
            "FROM node_relation as supertree " +
            "CROSS JOIN node_relation as subtree " +
            "WHERE supertree.descendant_id = ?2 " +
            "and subtree.ancestor_id = ?1", nativeQuery = true)
    void insertSubtree(Long nodeId, Long newParentId);


    @Query(value = "SELECT MAX(nr.length) FROM node n " +
            "JOIN node_relation nr " +
            "ON (n.id = nr.ancestor_id) " +
            "WHERE nr.descendant_id = ?1", nativeQuery = true)
    Long getHeight(Long nodeId);

    @Modifying
    @Query(value = "INSERT INTO node_relation (ancestor_id, descendant_id, length) " +
            "SELECT ancestor_id, ?1, (length+1) FROM node_relation " +
            "WHERE descendant_id=?2 " +
            "UNION ALL SELECT ?1, ?1, 0", nativeQuery = true)
    void createNodeRelations(Long newNodeId, Long parentId);

}
