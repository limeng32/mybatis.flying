package indi.mybatis.flying.pojo;

import java.io.Serializable;

import javax.persistence.Id;

import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.annotations.TableMapperAnnotation;
import indi.mybatis.flying.pojoHelper.PojoSupport;

@TableMapperAnnotation(tableName = "proj_ratio")
public class ProjRatio extends PojoSupport<ProjRatio> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@FieldMapperAnnotation(dbFieldName = "id", jdbcType = JdbcType.BIGINT)
	private Long id;

	@FieldMapperAnnotation(dbFieldName = "proj_name", jdbcType = JdbcType.VARCHAR)
	private String projName;

	@FieldMapperAnnotation(dbFieldName = "staff_id", jdbcType = JdbcType.VARCHAR)
	private String staffId;

	@FieldMapperAnnotation(dbFieldName = "year", jdbcType = JdbcType.VARCHAR)
	private String year;

	@FieldMapperAnnotation(dbFieldName = "season", jdbcType = JdbcType.VARCHAR)
	private Integer season;

	@Override
	public Long getId() {
		return id;
	}

	public String getProjName() {
		return projName;
	}

	public void setProjName(String projName) {
		this.projName = projName;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Integer getSeason() {
		return season;
	}

	public void setSeason(Integer season) {
		this.season = season;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
