DROP TABLE IF EXISTS account2_;
CREATE TABLE account2_ (
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
  KEY role1 (role_id) USING BTREE,
) ENGINE=InnoDB AUTO_INCREMENT=456 DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS detail2_;
CREATE TABLE detail2_ (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(32) DEFAULT NULL,
  detail varchar(32) DEFAULT NULL,
  createtime datetime DEFAULT NULL,
  number_ int(11) DEFAULT NULL,
  loginlog_id int(11) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS loginlog_;
CREATE TABLE loginlog_ (
  id int(11) NOT NULL AUTO_INCREMENT,
  loginIP varchar(50) DEFAULT NULL,
  accountId bigint DEFAULT NULL,
  loginTime datetime DEFAULT NULL,
  madetime datetime DEFAULT NULL,
  confirmtime datetime DEFAULT NULL,
  PRIMARY KEY (id),
  KEY account1 (accountId)
) ENGINE=InnoDB AUTO_INCREMENT=333 DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS role2_;
CREATE TABLE role2_ (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(50) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
