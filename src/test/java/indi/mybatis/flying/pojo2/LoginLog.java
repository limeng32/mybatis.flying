package indi.mybatis.flying.pojo2;

import java.io.Serializable;

import javax.persistence.Column;

import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.annotations.ForeignAssociation;
import indi.mybatis.flying.annotations.TableMapperAnnotation;
import indi.mybatis.flying.pojoHelper.PojoSupport;
import indi.mybatis.flying.statics.AssociationType;
import indi.mybatis.flying.statics.OpLockType;

@TableMapperAnnotation(tableName = "LoginLog")
public class LoginLog extends PojoSupport<LoginLog> implements Serializable {

	private static final long serialVersionUID = 1L;

	@FieldMapperAnnotation(dbFieldName = "iD", jdbcType = JdbcType.INTEGER, isUniqueKey = true, ignoreTag = {
			"noId" }, whiteListTag = "simple0")
	private Integer id;

	@Column
	private java.util.Date loginTime;

	@FieldMapperAnnotation(dbFieldName = "logiNIP", jdbcType = JdbcType.VARCHAR, whiteListTag = "simple0")
	private java.lang.String loginIP;

	@FieldMapperAnnotation(dbFieldName = "num", jdbcType = JdbcType.INTEGER)
	private Integer num;

	@FieldMapperAnnotation(dbFieldName = "logiNIP2", jdbcType = JdbcType.VARCHAR)
	private java.lang.String loginIP2;

	private Account account;

	private Account2 account2;

	// 下面注解中的dbAssociationUniqueKey、isUniqueKey、opLockType、associationType、associationExtra都不会真正起作用
	@FieldMapperAnnotation(dbFieldName = "accountId", jdbcType = JdbcType.INTEGER, delegate = true, dbAssociationUniqueKey = "id", isUniqueKey = true, opLockType = OpLockType.VERSION, associationType = AssociationType.LEFT_JOIN, associationExtra = {
			@ForeignAssociation(dbFieldName = "MODEL_REV", dbAssociationFieldName = "LATEST_DEPLOY_REV") })
	private Long delegateAccountId;

	@Override
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

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public java.lang.String getLoginIP2() {
		return loginIP2;
	}

	public void setLoginIP2(java.lang.String loginIP2) {
		this.loginIP2 = loginIP2;
	}

	public Long getDelegateAccountId() {
		return delegateAccountId;
	}

	public void setDelegateAccountId(Long delegateAccountId) {
		this.delegateAccountId = delegateAccountId;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account newAccount) {
		if (this.account == null || this.account != newAccount) {
			if (this.account != null) {
				Account oldAccount = this.account;
				this.account = null;
				oldAccount.removeLoginLog(this);
			}
			if (newAccount != null) {
				this.account = newAccount;
				this.account.addLoginLog(this);
			}
		}
	}
	
	public Account2 getAccount2() {
		return account2;
	}

	public void setAccount2(Account2 newAccount) {
		if (this.account2 == null || this.account2 != newAccount) {
			if (this.account2 != null) {
				Account2 oldAccount = this.account2;
				this.account2 = null;
				oldAccount.removeLoginLog(this);
			}
			if (newAccount != null) {
				this.account2 = newAccount;
				this.account2.addLoginLog(this);
			}
		}
	}

}