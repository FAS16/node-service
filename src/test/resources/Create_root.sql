DELETE FROM node;
DELETE FROM node_relation;
INSERT INTO node (id, parent_id, root_id, height) VALUES (1, null, 1, 0);

INSERT INTO node_relation (ancestor_id, descendant_id, length) VALUES (1, 1, 0);