package indi.mybatis.flying.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;

import org.apache.ibatis.type.JdbcType;

import com.alibaba.fastjson.annotation.JSONField;

import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.annotations.ForeignAssociation;
import indi.mybatis.flying.annotations.TableMapperAnnotation;
import indi.mybatis.flying.pojoHelper.PojoSupport;
import indi.mybatis.flying.statics.AssociationType;
import indi.mybatis.flying.statics.OpLockType;

@TableMapperAnnotation(tableName = "account_")
public class Account_ extends PojoSupport<Account_> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@FieldMapperAnnotation(dbFieldName = "id", jdbcType = JdbcType.BIGINT)
	private Long id;

	@FieldMapperAnnotation(dbFieldName = "name", jdbcType = JdbcType.VARCHAR)
	private java.lang.String name;

	@Column
	private java.lang.String email;

	@FieldMapperAnnotation(dbFieldName = "password", jdbcType = JdbcType.VARCHAR, ignoreTag = { "noPassword" })
	private java.lang.String password;

	@FieldMapperAnnotation(dbFieldName = "opLock", jdbcType = JdbcType.INTEGER, opLockType = OpLockType.Version)
	private Integer opLock;

	@FieldMapperAnnotation(dbFieldName = "status", jdbcType = JdbcType.CHAR)
	private StoryStatus_ status;

	/**
	 * 是否已激活
	 */
	@Column
	private Boolean activated;

	/**
	 * 激活码
	 * 
	 */
	@JSONField(serialize = false)
	@FieldMapperAnnotation(dbFieldName = "activateValue", jdbcType = JdbcType.VARCHAR)
	private java.lang.String activateValue;

	@FieldMapperAnnotation(dbFieldName = "role_id", jdbcType = JdbcType.INTEGER, dbAssociationUniqueKey = "id", ignoreTag = {
			"noRole" }, associationType = AssociationType.RightJoin)
	private Role_ role;

	@FieldMapperAnnotation(dbFieldName = "deputy_id", jdbcType = JdbcType.INTEGER, dbAssociationUniqueKey = "id")
	private Role_ roleDeputy;

	@FieldMapperAnnotation(dbFieldName = "permission_id", jdbcType = JdbcType.INTEGER, dbAssociationUniqueKey = "id", associationExtra = {
			@ForeignAssociation(dbFieldName = "name", dbAssociationFieldName = "name") }, associationType = AssociationType.RightJoin)
	private Permission permission;

	private java.util.Collection<LoginLog_> loginLog;

	private java.util.Collection<LoginLogSource2> loginLogSource2;

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public java.util.Collection<LoginLogSource2> getLoginLogSource2() {
		if (loginLogSource2 == null) {
			loginLogSource2 = new java.util.LinkedHashSet<LoginLogSource2>();
		}
		return loginLogSource2;
	}

	public java.util.Iterator<LoginLogSource2> getIteratorLoginLogSource2() {
		if (loginLogSource2 == null) {
			loginLogSource2 = new java.util.LinkedHashSet<LoginLogSource2>();
		}
		return loginLogSource2.iterator();
	}

	public void setLoginLogSource2(java.util.Collection<LoginLogSource2> newLoginLogSource2) {
		removeAllLoginLogSource2();
		for (java.util.Iterator<LoginLogSource2> iter = newLoginLogSource2.iterator(); iter.hasNext();) {
			addLoginLogSource2((LoginLogSource2) iter.next());
		}
	}

	public void addLoginLogSource2(LoginLogSource2 newLoginLogSource2) {
		if (newLoginLogSource2 == null) {
			return;
		}
		if (this.loginLogSource2 == null) {
			this.loginLogSource2 = new java.util.LinkedHashSet<LoginLogSource2>();
		}
		if (!this.loginLogSource2.contains(newLoginLogSource2)) {
			this.loginLogSource2.add(newLoginLogSource2);
			newLoginLogSource2.setAccount(this);
		} else {
			for (LoginLogSource2 temp : this.loginLogSource2) {
				if (newLoginLogSource2.equals(temp)) {
					if (temp != newLoginLogSource2) {
						removeLoginLogSource2(temp);
						this.loginLogSource2.add(newLoginLogSource2);
						newLoginLogSource2.setAccount(this);
					}
					break;
				}
			}
		}
	}

	public void removeLoginLogSource2(LoginLogSource2 oldLoginLogSource2) {
		if (oldLoginLogSource2 == null) {
			return;
		}
		if (this.loginLogSource2 != null) {
			if (this.loginLogSource2.contains(oldLoginLogSource2)) {
				for (LoginLogSource2 temp : this.loginLogSource2) {
					if (oldLoginLogSource2.equals(temp)) {
						if (temp != oldLoginLogSource2) {
							temp.setAccount((Account_) null);
						}
						break;
					}
				}
				this.loginLogSource2.remove(oldLoginLogSource2);
				oldLoginLogSource2.setAccount((Account_) null);
			}
		}
	}

	public void removeAllLoginLogSource2() {
		if (loginLogSource2 != null) {
			LoginLogSource2 oldLoginLogSource2;
			for (java.util.Iterator<LoginLogSource2> iter = getIteratorLoginLogSource2(); iter.hasNext();) {
				oldLoginLogSource2 = (LoginLogSource2) iter.next();
				iter.remove();
				oldLoginLogSource2.setAccount((Account_) null);
			}
			loginLogSource2.clear();
		}
	}

	public java.util.Collection<LoginLog_> getLoginLog() {
		if (loginLog == null) {
			loginLog = new java.util.LinkedHashSet<LoginLog_>();
		}
		return loginLog;
	}

	public java.util.Iterator<LoginLog_> getIteratorLoginLog() {
		if (loginLog == null) {
			loginLog = new java.util.LinkedHashSet<LoginLog_>();
		}
		return loginLog.iterator();
	}

	public void setLoginLog(java.util.Collection<LoginLog_> newLoginLog) {
		removeAllLoginLog();
		for (java.util.Iterator<LoginLog_> iter = newLoginLog.iterator(); iter.hasNext();) {
			addLoginLog((LoginLog_) iter.next());
		}
	}

	public void addLoginLog(LoginLog_ newLoginLog) {
		if (newLoginLog == null) {
			return;
		}
		if (this.loginLog == null) {
			this.loginLog = new java.util.LinkedHashSet<LoginLog_>();
		}
		if (!this.loginLog.contains(newLoginLog)) {
			this.loginLog.add(newLoginLog);
			newLoginLog.setAccount(this);
		} else {
			for (LoginLog_ temp : this.loginLog) {
				if (newLoginLog.equals(temp)) {
					if (temp != newLoginLog) {
						removeLoginLog(temp);
						this.loginLog.add(newLoginLog);
						newLoginLog.setAccount(this);
					}
					break;
				}
			}
		}
	}

	public void removeLoginLog(LoginLog_ oldLoginLog) {
		if (oldLoginLog == null) {
			return;
		}
		if (this.loginLog != null) {
			if (this.loginLog.contains(oldLoginLog)) {
				for (LoginLog_ temp : this.loginLog) {
					if (oldLoginLog.equals(temp)) {
						if (temp != oldLoginLog) {
							temp.setAccount((Account_) null);
						}
						break;
					}
				}
				this.loginLog.remove(oldLoginLog);
				oldLoginLog.setAccount((Account_) null);
			}
		}
	}

	public void removeAllLoginLog() {
		if (loginLog != null) {
			LoginLog_ oldLoginLog;
			for (java.util.Iterator<LoginLog_> iter = getIteratorLoginLog(); iter.hasNext();) {
				oldLoginLog = (LoginLog_) iter.next();
				iter.remove();
				oldLoginLog.setAccount((Account_) null);
			}
			loginLog.clear();
		}
	}

	public Role_ getRole() {
		return role;
	}

	public void setRole(Role_ newRole) {
		if (this.role == null || !this.role.equals(newRole)) {
			if (this.role != null) {
				Role_ oldRole = this.role;
				this.role = null;
				oldRole.removeAccount(this);
			}
			if (newRole != null) {
				this.role = newRole;
				this.role.addAccount(this);
			}
		}
	}

	public Role_ getRoleDeputy() {
		return roleDeputy;
	}

	public void setRoleDeputy(Role_ newRoleDeputy) {
		if (this.roleDeputy == null || !this.roleDeputy.equals(newRoleDeputy)) {
			if (this.roleDeputy != null) {
				Role_ oldRoleDeputy = this.roleDeputy;
				this.roleDeputy = null;
				oldRoleDeputy.removeAccountDeputy(this);
			}
			if (newRoleDeputy != null) {
				this.roleDeputy = newRoleDeputy;
				this.roleDeputy.addAccountDeputy(this);
			}
		}
	}

	public Permission getPermission() {
		return permission;
	}

	public void setPermission(Permission newPermission) {
		if (this.permission == null || !this.permission.equals(newPermission)) {
			if (this.permission != null) {
				Permission oldPermission = this.permission;
				this.permission = null;
				oldPermission.removeAccount(this);
			}
			if (newPermission != null) {
				this.permission = newPermission;
				this.permission.addAccount(this);
			}
		}
	}
}