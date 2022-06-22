INSERT INTO node (id, parent_id, root_id, height) VALUES (1, null, 1, 0);
INSERT INTO node (id, parent_id, root_id, height) VALUES (2, 1, 1, 1);
INSERT INTO node (id, parent_id, root_id, height) VALUES (3, 2, 1, 2);
INSERT INTO node (id, parent_id, root_id, height) VALUES (4, 1, 1, 1);
INSERT INTO node (id, parent_id, root_id, height) VALUES (5, 4, 1, 2);
INSERT INTO node (id, parent_id, root_id, height) VALUES (6, 4, 1, 2);
INSERT INTO node (id, parent_id, root_id, height) VALUES (7, 6, 1, 3);

INSERT INTO node_relation (ancestor_id, descendant_id, length) VALUES (1, 1, 0);
INSERT INTO node_relation (ancestor_id, descendant_id, length) VALUES (1, 2, 1);
INSERT INTO node_relation (ancestor_id, descendant_id, length) VALUES (1, 3, 2);
INSERT INTO node_relation (ancestor_id, descendant_id, length) VALUES (1, 4, 1);
INSERT INTO node_relation (ancestor_id, descendant_id, length) VALUES (1, 5, 2);
INSERT INTO node_relation (ancestor_id, descendant_id, length) VALUES (1, 6, 2);
INSERT INTO node_relation (ancestor_id, descendant_id, length) VALUES (1, 7, 3);
INSERT INTO node_relation (ancestor_id, descendant_id, length) VALUES (2, 3, 1);
INSERT INTO node_relation (ancestor_id, descendant_id, length) VALUES (3, 3, 0);
INSERT INTO node_relation (ancestor_id, descendant_id, length) VALUES (4, 4, 0);
INSERT INTO node_relation (ancestor_id, descendant_id, length) VALUES (4, 5, 1);
INSERT INTO node_relation (ancestor_id, descendant_id, length) VALUES (4, 6, 1);
INSERT INTO node_relation (ancestor_id, descendant_id, length) VALUES (4, 7, 2);
INSERT INTO node_relation (ancestor_id, descendant_id, length) VALUES (5, 5, 0);
INSERT INTO node_relation (ancestor_id, descendant_id, length) VALUES (6, 6, 0);
INSERT INTO node_relation (ancestor_id, descendant_id, length) VALUES (6, 7, 1);
INSERT INTO node_relation (ancestor_id, descendant_id, length) VALUES (7, 7, 0);