package indi.mybatis.flying.pojo;

import java.io.Serializable;

import javax.persistence.Table;

import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.pojoHelper.PojoSupport;

@Table(name = "detail_")
public class Detail_ extends PojoSupport<Detail_> implements Serializable {

	private static final long serialVersionUID = 1L;

	@FieldMapperAnnotation(dbFieldName = "id", jdbcType = JdbcType.INTEGER, isUniqueKey = true)
	private Integer id;

	@FieldMapperAnnotation(dbFieldName = "name", jdbcType = JdbcType.VARCHAR)
	private java.lang.String name;

	@FieldMapperAnnotation(dbFieldName = "loginlog_id", jdbcType = JdbcType.INTEGER, dbAssociationUniqueKey = "id")
	private LoginLog_ loginLog;

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

	public LoginLog_ getLoginLog() {
		return loginLog;
	}

	public void setLoginLog(LoginLog_ newLoginLog) {
		if (this.loginLog == null || !this.loginLog.equals(newLoginLog)) {
			if (this.loginLog != null) {
				LoginLog_ oldLoginLog = this.loginLog;
				this.loginLog = null;
				oldLoginLog.removeDetail(this);
			}
			if (newLoginLog != null) {
				this.loginLog = newLoginLog;
				this.loginLog.addDetail(this);
			}
		}
	}

}
