
DROP TABLE IF EXISTS `form_student`;

CREATE TABLE `form_student` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `age` int DEFAULT NULL COMMENT '年龄',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建用户id',
  `update_by` varchar(64) DEFAULT NULL COMMENT '修改用户id',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='学生表';