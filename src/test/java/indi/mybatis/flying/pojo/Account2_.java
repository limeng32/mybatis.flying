package indi.mybatis.flying.pojo;

import java.io.Serializable;

import org.apache.ibatis.type.JdbcType;

import com.alibaba.fastjson.annotation.JSONField;

import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.annotations.TableMapperAnnotation;
import indi.mybatis.flying.pojoHelper.PojoSupport;
import indi.mybatis.flying.statics.OpLockType;

@TableMapperAnnotation(tableName = "account2_")
public class Account2_ extends PojoSupport<Account2_> implements Serializable {

	private static final long serialVersionUID = 1L;

	@FieldMapperAnnotation(dbFieldName = "id", jdbcType = JdbcType.INTEGER, isUniqueKey = true)
	private Integer id;

	@FieldMapperAnnotation(dbFieldName = "name", jdbcType = JdbcType.VARCHAR)
	private java.lang.String name;

	@FieldMapperAnnotation(dbFieldName = "email", jdbcType = JdbcType.VARCHAR)
	private java.lang.String email;

	@FieldMapperAnnotation(dbFieldName = "password", jdbcType = JdbcType.VARCHAR, ignoredSelect = true)
	private java.lang.String password;

	@FieldMapperAnnotation(dbFieldName = "opLock", jdbcType = JdbcType.INTEGER, opLockType = OpLockType.Version)
	private Integer opLock;

	@FieldMapperAnnotation(dbFieldName = "status", jdbcType = JdbcType.CHAR)
	private StoryStatus_ status;

	/**
	 * 是否已激活
	 */
	@FieldMapperAnnotation(dbFieldName = "activated", jdbcType = JdbcType.BOOLEAN)
	private Boolean activated;

	/**
	 * 激活码
	 * 
	 */
	@JSONField(serialize = false)
	@FieldMapperAnnotation(dbFieldName = "activateValue", jdbcType = JdbcType.VARCHAR)
	private java.lang.String activateValue;

	/**
	 * 别名
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "nickname", jdbcType = JdbcType.VARCHAR)
	private String nickname;

	@FieldMapperAnnotation(dbFieldName = "role_id", jdbcType = JdbcType.INTEGER, dbAssociationUniqueKey = "id")
	private Role2_ role;

	// @FieldMapperAnnotation(dbFieldName = "deputy_id", jdbcType =
	// JdbcType.INTEGER, dbAssociationUniqueKey = "id")
	// private Role_ roleDeputy;
	//
	// private java.util.Collection<LoginLog_> loginLog;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public java.lang.String getEmail() {
		return email;
	}

	public void setEmail(java.lang.String email) {
		this.email = email;
	}

	public java.lang.String getPassword() {
		return password;
	}

	public void setPassword(java.lang.String password) {
		this.password = password;
	}

	public Boolean getActivated() {
		return activated;
	}

	public void setActivated(Boolean activated) {
		this.activated = activated;
	}

	public java.lang.String getActivateValue() {
		return activateValue;
	}

	public void setActivateValue(java.lang.String activateValue) {
		this.activateValue = activateValue;
	}

	public Integer getOpLock() {
		return opLock;
	}

	public StoryStatus_ getStatus() {
		return status;
	}

	public void setStatus(StoryStatus_ status) {
		this.status = status;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	// public java.util.Collection<LoginLog_> getLoginLog() {
	// if (loginLog == null)
	// loginLog = new java.util.LinkedHashSet<LoginLog_>();
	// return loginLog;
	// }
	//
	// public java.util.Iterator<LoginLog_> getIteratorLoginLog() {
	// if (loginLog == null)
	// loginLog = new java.util.LinkedHashSet<LoginLog_>();
	// return loginLog.iterator();
	// }
	//
	// public void setLoginLog(java.util.Collection<LoginLog_> newLoginLog) {
	// removeAllLoginLog();
	// for (java.util.Iterator<LoginLog_> iter = newLoginLog.iterator();
	// iter.hasNext();)
	// addLoginLog((LoginLog_) iter.next());
	// }
	//
	// public void addLoginLog(LoginLog_ newLoginLog) {
	// if (newLoginLog == null)
	// return;
	// if (this.loginLog == null)
	// this.loginLog = new java.util.LinkedHashSet<LoginLog_>();
	// if (!this.loginLog.contains(newLoginLog)) {
	// this.loginLog.add(newLoginLog);
	// newLoginLog.setAccount(this);
	// }
	// }
	//
	// public void removeLoginLog(LoginLog_ oldLoginLog) {
	// if (oldLoginLog == null)
	// return;
	// if (this.loginLog != null)
	// if (this.loginLog.contains(oldLoginLog)) {
	// this.loginLog.remove(oldLoginLog);
	// oldLoginLog.setAccount((Account_) null);
	// }
	// }
	//
	// public void removeAllLoginLog() {
	// if (loginLog != null) {
	// LoginLog_ oldLoginLog;
	// for (java.util.Iterator<LoginLog_> iter = getIteratorLoginLog();
	// iter.hasNext();) {
	// oldLoginLog = (LoginLog_) iter.next();
	// iter.remove();
	// oldLoginLog.setAccount((Account_) null);
	// }
	// }
	// }
	//
	public Role2_ getRole() {
		return role;
	}

	public void setRole(Role2_ newRole) {
		if (this.role == null || !this.role.equals(newRole)) {
			if (this.role != null) {
				Role2_ oldRole = this.role;
				this.role = null;
				oldRole.removeAccount(this);
			}
			if (newRole != null) {
				this.role = newRole;
				this.role.addAccount(this);
			}
		}
	}

	// public Role_ getRoleDeputy() {
	// return roleDeputy;
	// }
	//
	// public void setRoleDeputy(Role_ newRoleDeputy) {
	// if (this.roleDeputy == null || !this.roleDeputy.equals(newRoleDeputy)) {
	// if (this.roleDeputy != null) {
	// Role_ oldRoleDeputy = this.roleDeputy;
	// this.roleDeputy = null;
	// oldRoleDeputy.removeAccountDeputy(this);
	// }
	// if (newRoleDeputy != null) {
	// this.roleDeputy = newRoleDeputy;
	// this.roleDeputy.addAccountDeputy(this);
	// }
	// }
	// }

}
