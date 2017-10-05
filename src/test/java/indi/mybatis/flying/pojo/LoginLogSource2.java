package indi.mybatis.flying.pojo;

import java.io.Serializable;

import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.annotations.TableMapperAnnotation;
import indi.mybatis.flying.pojoHelper.PojoSupport;

@TableMapperAnnotation(tableName = "LoginLog_")
public class LoginLogSource2 extends PojoSupport<LoginLogSource2> implements Serializable {

	private static final long serialVersionUID = 1L;

	@FieldMapperAnnotation(dbFieldName = "iD", jdbcType = JdbcType.INTEGER, isUniqueKey = true)
	private Integer id;

	@FieldMapperAnnotation(dbFieldName = "loginTime", jdbcType = JdbcType.TIMESTAMP)
	private java.util.Date loginTime;

	@FieldMapperAnnotation(dbFieldName = "logiNIP", jdbcType = JdbcType.VARCHAR)
	private java.lang.String loginIP;

	@FieldMapperAnnotation(dbFieldName = "accountId", jdbcType = JdbcType.INTEGER, ignoreTag = {
			"noAccount" }, dbAssociationTypeHandler = indi.mybatis.flying.typeHandler.AccountTypeHandler.class)
	private Account_ account;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public java.util.Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(java.util.Date loginTime) {
		this.loginTime = loginTime;
	}

	public java.lang.String getLoginIP() {
		return loginIP;
	}

	public void setLoginIP(java.lang.String loginIP) {
		this.loginIP = loginIP;
	}

	public Account_ getAccount() {
		return account;
	}

	public void setAccount(Account_ newAccount) {
		if (this.account == null || !this.account.equals(newAccount)) {
			if (this.account != null) {
				Account_ oldAccount = this.account;
				this.account = null;
				oldAccount.removeLoginLogSource2(this);
			}
			if (newAccount != null) {
				this.account = newAccount;
				this.account.addLoginLogSource2(this);
			}
		}
	}

}