create table node
(
    id bigint primary key,
    parent_id bigint null,
    root_id   bigint not null,
    height   bigint not null
);

create table node_relation
(
    ancestor_id   bigint,
    descendant_id bigint,
    length      bigint not null,
    primary key (ancestor_id, descendant_id),
    constraint node_fk_ancestor
        foreign key (ancestor_id) references node (id) ON DELETE CASCADE,
    constraint node_fk_descendant
        foreign key (descendant_id) references node (id) ON DELETE CASCADE
);

create index node_fk_ancestor
    on node_relation (ancestor_id);

create index node_fk_descendant
    on node_relation (descendant_id);