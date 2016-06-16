CREATE DATABASE blauto DEFAULT CHARACTER SET utf8;
GRANT ALL PRIVILEGES ON blauto.* TO 'blauto'@'localhost' IDENTIFIED BY 'blauto';

CREATE TABLE do_log (
  id           INT(11) NOT NULL AUTO_INCREMENT COMMENT 'pk',
  user_name    VARCHAR(45)      DEFAULT NULL COMMENT '用户名',
  task_type    VARCHAR(45)      DEFAULT NULL COMMENT '任务类型',
  execute_time DATETIME         DEFAULT NULL COMMENT '执行时间',
  finish_time  DATETIME         DEFAULT NULL COMMENT '完成时间',
  PRIMARY KEY (id),
  KEY idx_u_t_e (user_name, task_type, execute_time),
  KEY idx_u_t_f (user_name, task_type, finish_time)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;
