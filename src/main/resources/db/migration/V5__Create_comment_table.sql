create table comment
(
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	parent_id BIGINT not null,
	type int not null,
	commentator int not null,
	gmt_create bigint not null,
	gmt_modified bigint not null,
	like_count bigint default 0
);