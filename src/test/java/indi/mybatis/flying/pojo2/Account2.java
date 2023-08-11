package indi.mybatis.flying.pojo2;

import java.io.Serializable;
import java.util.Map;

import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.annotations.ForeignAssociation;
import indi.mybatis.flying.pojo.Permission;
import indi.mybatis.flying.pojo.Role_;
import indi.mybatis.flying.pojo.StoryStatus_;
import indi.mybatis.flying.pojoHelper.PojoSupport;
import indi.mybatis.flying.statics.AssociationType;

public class Account2 extends PojoSupport<Account> implements Serializable {
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

	private Map<Object, LoginLog> loginlogMap;

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

	public Map<Object, LoginLog> getLoginlogMap() {
		if (loginlogMap == null) {
			loginlogMap = new java.util.LinkedHashMap<Object, LoginLog>();
		}
		return loginlogMap;
	}

	public java.util.Collection<LoginLog> getLoginLog() {
		return getLoginlogMap().values();
	}

	public java.util.Iterator<LoginLog> getIteratorLoginLog() {
		if (loginlogMap == null) {
			loginlogMap = new java.util.LinkedHashMap<Object, LoginLog>();
		}
		return loginlogMap.values().iterator();
	}

	public void setLoginLog(java.util.Collection<LoginLog> newLoginlog) {
		removeAllLoginLog();
		for (java.util.Iterator<LoginLog> iter = newLoginlog.iterator(); iter.hasNext();)
			addLoginLog(iter.next());
	}

	public void addLoginLog(LoginLog newLoginlog) {
		if (newLoginlog == null)
			return;
		if (this.loginlogMap == null)
			this.loginlogMap = new java.util.LinkedHashMap<Object, LoginLog>();
		if (!this.loginlogMap.containsKey(newLoginlog.getId())) {
			this.loginlogMap.put(newLoginlog.getId(), newLoginlog);
			newLoginlog.setAccount2(this);
		} else {
			LoginLog temp = loginlogMap.get(newLoginlog.getId());
			if (newLoginlog.equals(temp) && temp != newLoginlog) {
				removeLoginLog(temp);
				this.loginlogMap.put(newLoginlog.getId(), newLoginlog);
				newLoginlog.setAccount2(this);
			}
		}
	}

	public void removeLoginLog(LoginLog oldLoginlog) {
		if (oldLoginlog == null)
			return;
		if (this.loginlogMap != null && this.loginlogMap.containsKey(oldLoginlog.getId())) {
			LoginLog temp = loginlogMap.get(oldLoginlog.getId());
			if (oldLoginlog.equals(temp) && temp != oldLoginlog) {
				temp.setAccount2(null);
			}
			this.loginlogMap.remove(oldLoginlog.getId());
			oldLoginlog.setAccount2(null);
		}
	}

	public void removeAllLoginLog() {
		if (loginlogMap != null) {
			LoginLog oldLoginlog;
			for (java.util.Iterator<LoginLog> iter = getIteratorLoginLog(); iter.hasNext();) {
				oldLoginlog = iter.next();
				iter.remove();
				oldLoginlog.setAccount2(null);
			}
		}
	}
}
