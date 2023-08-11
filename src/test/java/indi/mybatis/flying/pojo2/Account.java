package indi.mybatis.flying.pojo2;

import java.io.Serializable;

import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.annotations.ForeignAssociation;
import indi.mybatis.flying.pojo.Permission;
import indi.mybatis.flying.pojo.Role_;
import indi.mybatis.flying.pojo.StoryStatus_;
import indi.mybatis.flying.pojoHelper.PojoSupport;
import indi.mybatis.flying.statics.AssociationType;

public class Account extends PojoSupport<Account> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private java.lang.String name;

	private java.lang.String email;

	private java.lang.String password;

	private Integer opLock;

	private StoryStatus_ status;

	/**
	 * 是否已激活
	 */
	private Boolean activated;

	/**
	 * 激活码
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "activateValue", jdbcType = JdbcType.VARCHAR, whiteListTag = { "simple" })
	private java.lang.String activateValue;

	@FieldMapperAnnotation(dbFieldName = "role_id", jdbcType = JdbcType.INTEGER, delegate = true)
	private Long delegateRoleId;

	@FieldMapperAnnotation(dbFieldName = "role_id", jdbcType = JdbcType.INTEGER, dbAssociationUniqueKey = "id", ignoreTag = {
			"noRole" })
	private Role_ role;

	@FieldMapperAnnotation(dbFieldName = "deputy_id", jdbcType = JdbcType.INTEGER, dbAssociationUniqueKey = "id")
	private Role_ roleDeputy;

	@FieldMapperAnnotation(dbFieldName = "permission_id", jdbcType = JdbcType.INTEGER, dbAssociationUniqueKey = "id", associationExtra = {
			@ForeignAssociation(dbFieldName = "name", dbAssociationFieldName = "name") }, associationType = AssociationType.LEFT_JOIN, whiteListTag = {
					"simple" })
	private Permission permission;

	private java.util.Collection<LoginLog> loginLog;

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

	public void setOpLock(Integer opLock) {
		this.opLock = opLock;
	}

	public StoryStatus_ getStatus() {
		return status;
	}

	public void setStatus(StoryStatus_ status) {
		this.status = status;
	}

	public Long getDelegateRoleId() {
		return delegateRoleId;
	}

	public void setDelegateRoleId(Long delegateRoleId) {
		this.delegateRoleId = delegateRoleId;
	}

	public java.util.Collection<LoginLog> getLoginLog() {
		if (loginLog == null) {
			loginLog = new java.util.LinkedHashSet<LoginLog>();
		}
		return loginLog;
	}

	public java.util.Iterator<LoginLog> getIteratorLoginLog() {
		if (loginLog == null) {
			loginLog = new java.util.LinkedHashSet<LoginLog>();
		}
		return loginLog.iterator();
	}

	public void setLoginLog(java.util.Collection<LoginLog> newLoginLog) {
		removeAllLoginLog();
		for (java.util.Iterator<LoginLog> iter = newLoginLog.iterator(); iter.hasNext();) {
			addLoginLog((LoginLog) iter.next());
		}
	}

	public void addLoginLog(LoginLog newLoginLog) {
		if (newLoginLog == null) {
			return;
		}
		if (this.loginLog == null) {
			this.loginLog = new java.util.LinkedHashSet<LoginLog>();
		}
		if (!this.loginLog.contains(newLoginLog)) {
			this.loginLog.add(newLoginLog);
			newLoginLog.setAccount(this);
		} else {
			for (LoginLog temp : this.loginLog) {
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

	public void removeLoginLog(LoginLog oldLoginLog) {
		if (oldLoginLog == null) {
			return;
		}
		if (this.loginLog != null) {
			if (this.loginLog.contains(oldLoginLog)) {
				for (LoginLog temp : this.loginLog) {
					if (oldLoginLog.equals(temp)) {
						if (temp != oldLoginLog) {
							temp.setAccount((Account) null);
						}
						break;
					}
				}
				this.loginLog.remove(oldLoginLog);
				oldLoginLog.setAccount((Account) null);
			}
		}
	}

	public void removeAllLoginLog() {
		if (loginLog != null) {
			LoginLog oldLoginLog;
			for (java.util.Iterator<LoginLog> iter = getIteratorLoginLog(); iter.hasNext();) {
				oldLoginLog = (LoginLog) iter.next();
				iter.remove();
				oldLoginLog.setAccount((Account) null);
			}
			loginLog.clear();
		}
	}

}
