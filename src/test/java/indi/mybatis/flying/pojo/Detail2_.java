package indi.mybatis.flying.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.pojoHelper.PojoSupport;

@Table
public class Detail2_ extends PojoSupport<Detail2_> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@FieldMapperAnnotation(dbFieldName = "id", jdbcType = JdbcType.INTEGER, isUniqueKey = true)
	private Integer id;

	@Column(name = "name")
	@FieldMapperAnnotation(dbFieldName = "name", jdbcType = JdbcType.VARCHAR)
	private java.lang.String name;

	@Column(name = "detail")
	@FieldMapperAnnotation(dbFieldName = "detail", jdbcType = JdbcType.VARCHAR)
	private java.lang.String detail;

	@Column(name = "create")
	@FieldMapperAnnotation(dbFieldName = "createtime", jdbcType = JdbcType.TIMESTAMP)
	private Date createtime;

	@FieldMapperAnnotation(dbFieldName = "loginlog_id", jdbcType = JdbcType.INTEGER, dbAssociationUniqueKey = "iD")
	private LoginLogSource2 loginLogSource2;

	@Override
	public Integer getId() {
		return id;
	}

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public java.lang.String getDetail() {
		return detail;
	}

	public void setDetail(java.lang.String detail) {
		this.detail = detail;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LoginLogSource2 getLoginLogSource2() {
		return loginLogSource2;
	}

	public void setLoginLogSource2(LoginLogSource2 newLoginLogSource2) {
		if (this.loginLogSource2 == null || !this.loginLogSource2.equals(newLoginLogSource2)) {
			if (this.loginLogSource2 != null) {
				LoginLogSource2 oldLoginLogSource2 = this.loginLogSource2;
				this.loginLogSource2 = null;
				oldLoginLogSource2.removeDetail2(this);
			}
			if (newLoginLogSource2 != null) {
				this.loginLogSource2 = newLoginLogSource2;
				this.loginLogSource2.addDetail2(this);
			}
		}
	}

}
