DROP TABLE IF EXISTS account_;
CREATE TABLE account_ (
  id bigint NOT NULL AUTO_INCREMENT,
  name varchar(100) DEFAULT NULL,
  email varchar(320) DEFAULT NULL,
  password varchar(64) DEFAULT NULL,
  activated tinyint(1) DEFAULT NULL,
  activateValue varchar(8) DEFAULT NULL,
  opLock int(11) DEFAULT NULL,
  status char(1) DEFAULT NULL,
  role_id int(11) DEFAULT NULL,
  deputy_id int(11) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY role1 (role_id) USING BTREE,
  KEY role2 (deputy_id) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=456 DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS detail_;
CREATE TABLE detail_ (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(32) DEFAULT NULL,
  loginlog_id int(11) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY loginlog1 (loginlog_id) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS loginlog_;
CREATE TABLE loginlog_ (
  id int(11) NOT NULL AUTO_INCREMENT,
  loginIP varchar(50) DEFAULT NULL,
  accountId bigint DEFAULT NULL,
  loginTime datetime DEFAULT NULL,
  num int DEFAULT NULL,
  loginIP2 varchar(50) DEFAULT NULL,
  status char(1) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY account1 (accountId)
) ENGINE=InnoDB AUTO_INCREMENT=333 DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS role_;
CREATE TABLE role_ (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(50) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS product;
CREATE TABLE product (
  id varchar(100) NOT NULL,
  name varchar(50) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS account22;
CREATE TABLE account22 (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(100) DEFAULT NULL,
  email varchar(320) DEFAULT NULL,
  password varchar(64) DEFAULT NULL,
  activated tinyint(1) DEFAULT NULL,
  activateValue varchar(8) DEFAULT NULL,
  opLock int(11) DEFAULT NULL,
  status char(1) DEFAULT NULL,
  nickname varchar(32) DEFAULT NULL,
  role_id int(11) DEFAULT NULL,
  PRIMARY KEY (id),
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;