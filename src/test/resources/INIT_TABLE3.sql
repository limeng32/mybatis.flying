DROP TABLE IF EXISTS account3;
DROP TABLE IF EXISTS t_emp_score;
CREATE TABLE account3 (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(100) DEFAULT NULL,
  email varchar(320) DEFAULT NULL,
  password varchar(64) DEFAULT NULL,
  activated tinyint(1) DEFAULT NULL,
  activateValue varchar(8) DEFAULT NULL,
  opLock int(11) DEFAULT NULL,
  status char(1) DEFAULT NULL,
  nickname varchar(32) DEFAULT NULL,
  score_id int(11) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE t_emp_score
(
   id                   int(11) not null auto_increment,
   ou                   varchar(255),
   dept_name            varchar(255),
   staff_id             varchar(11),
   staff_name           varchar(255),
   year                 char(4),
   season               char(1),
   score                decimal(15,5),
   score_coefficient    double(5),
   remark               varchar(255),
   score_type           char(1),
   emp_type             char(1),
   post_name            varchar(255),
   state                varchar(2),
   rank                 char(255),
   tag                  char(1),
   proj_id              varchar(18),
   proj_name            varchar(255),
   hours                double(10),
   checker_id           varchar(11),
   checker_name         varchar(20),
   create_time          timestamp,
   cont_degree          double(6),
   adjust_reason        varchar(255),
   unpass_reason        varchar(1000),
   update_time          timestamp,
   rank2				blob,
   secret2 				blob,
   primary key (id)
);
