drop table if exists distributor_snowflake;

create table distributor_snowflake
(
    id           bigint auto_increment primary key,
    trade_type   varchar(100)     not null comment '业务类型',
    machine_ip   varchar(15)      not null comment '机器IP',
    machine_no   int              not null comment '业务+IP分配的机器号',
    gmt_create   datetime         not null comment '创建时间',
    gmt_modified datetime         not null comment '心跳时间',
    is_deleted   bigint default 0 not null comment '逻辑删除标识',
    constraint distributor_snowflake_machine_ip_trade_type_is_deleted_uindex
        unique (machine_ip, trade_type, is_deleted),
    constraint distributor_snowflake_trade_type_machine_no_is_deleted_uindex
        unique (trade_type, machine_no, is_deleted)
);