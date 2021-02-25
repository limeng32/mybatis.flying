package indi.mybatis.flying.pojo;

import java.io.Serializable;

import org.apache.ibatis.type.JdbcType;

import com.alibaba.fastjson.annotation.JSONField;

import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.annotations.TableMapperAnnotation;
import indi.mybatis.flying.pojoHelper.PojoSupport;
import indi.mybatis.flying.statics.OpLockType;

@TableMapperAnnotation(tableName = "account3")
public class Account3 extends PojoSupport<Account3> implements Serializable {

	private static final long serialVersionUID = 1L;

	@FieldMapperAnnotation(dbFieldName = "id", jdbcType = JdbcType.INTEGER, isUniqueKey = true)
	private Integer id;

	@FieldMapperAnnotation(dbFieldName = "name", jdbcType = JdbcType.VARCHAR)
	private String name;

	@FieldMapperAnnotation(dbFieldName = "email", jdbcType = JdbcType.VARCHAR)
	private String email;

	@FieldMapperAnnotation(dbFieldName = "password", jdbcType = JdbcType.VARCHAR, ignoreTag = { "noPassword" })
	private String password;

	@FieldMapperAnnotation(dbFieldName = "opLock", jdbcType = JdbcType.INTEGER, opLockType = OpLockType.VERSION)
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

	@FieldMapperAnnotation(dbFieldName = "score_id", jdbcType = JdbcType.INTEGER, dbAssociationUniqueKey = "id")
	private EmpScore empScore;

	@Override
	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public EmpScore getEmpScore() {
		return empScore;
	}

	public void setEmpScore(EmpScore newEmpScore) {
		if (this.empScore == null || this.empScore != newEmpScore) {
			if (this.empScore != null) {
				EmpScore oldEmpScore = this.empScore;
				this.empScore = null;
				oldEmpScore.removeAccount3(this);
			}
			if (newEmpScore != null) {
				this.empScore = newEmpScore;
				this.empScore.addAccount3(this);
			}
		}
	}
}
