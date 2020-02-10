drop table if exists distributor_retry_event;
create table distributor_retry_event
(
	id bigint auto_increment comment '主键' primary key,
	retry_category varchar(50) null comment '重试事件标识',
	retry_key varchar(100) not null comment '重试事件幂等key值',
	retry_status varchar(20) default 'failure' not null comment '重试状态',
	max_retry int default 1 not null comment '最大重试次数',
	has_retry int default 0 not null comment '已经重试次数',
	method_signature varchar(1000) not null comment '方法参数签名',
	protocol varchar(20) not null comment '参数序列化协议',
	method_args text not null comment '重试参数值',
	err_message varchar(2000) null comment '错误信息',
	gmt_create datetime not null comment '记录创建时间',
	gmt_modified datetime not null comment '记录修改时间',
	is_deleted tinyint default 0 not null comment '逻辑删除',
	constraint distributor_retry_event_retry_category_retry_key_uindex
		unique (retry_category, retry_key)
)
comment '重试事件';

create index inx_retry_event_retry_status
	on atool.distributor_retry_event (retry_status);