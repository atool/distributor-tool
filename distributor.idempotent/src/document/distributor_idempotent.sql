drop table if exists distributor_idempotent;
create table distributor_idempotent
(
    id           bigint auto_increment primary key,
    idem_type    varchar(20)             not null comment '幂等类型',
    idem_key     varchar(100)            not null comment '幂等键值',
    protocol     varchar(20)             not null comment 'idem_value序列化协议',
    idem_value   varchar(4000)           null comment '幂等结果值',
    idem_status  varchar(10)             not null comment '幂等执行状态',
    gmt_create   datetime                not null comment '幂等创建时间',
    gmt_modified datetime                not null comment '修改时间',
    is_deleted   bigint default 0        not null comment '逻辑删除兼删除时间戳',
    expire_time  int    default 31536000 null comment '超时时间或过期清理时间（单位秒）, 默认365天',
    constraint uk_idem_type_key_deleted unique (idem_type, idem_key, is_deleted)
);
create index idx_idem_status_gmt_create on distributor_idempotent (idem_status, gmt_create);
