package indi.mybatis.flying.pojo;

import java.io.Serializable;

import javax.persistence.Id;

import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.annotations.ForeignAssociation;
import indi.mybatis.flying.annotations.TableMapperAnnotation;
import indi.mybatis.flying.pojoHelper.PojoSupport;

@TableMapperAnnotation(tableName = "emp_score")
public class EmpScore2 extends PojoSupport<EmpScore2> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@FieldMapperAnnotation(dbFieldName = "id", jdbcType = JdbcType.BIGINT)
	private Long id;

	@FieldMapperAnnotation(dbFieldName = "staff_name", jdbcType = JdbcType.VARCHAR)
	private String staffName;

//	@FieldMapperAnnotation(dbFieldName = "staff_id", jdbcType = JdbcType.VARCHAR)
	private String staffId;

	@FieldMapperAnnotation(dbFieldName = "year", jdbcType = JdbcType.VARCHAR)
	private String year;

	@FieldMapperAnnotation(dbFieldName = "season", jdbcType = JdbcType.VARCHAR)
	private Integer season;

	@FieldMapperAnnotation(dbFieldName = "state", jdbcType = JdbcType.VARCHAR)
	private String state;

	@FieldMapperAnnotation(dbFieldName = "staff_id", dbAssociationUniqueKey = "staff_id", associationExtra = {
			@ForeignAssociation(dbFieldName = "season", dbAssociationFieldName = "season"),
			@ForeignAssociation(dbFieldName = "year", dbAssociationFieldName = "year") })
	private ProjRatio projRatio;

	@Override
	public Long getId() {
		return id;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public ProjRatio getProjRatio() {
		return projRatio;
	}

	public void setProjRatio(ProjRatio projRatio) {
		this.projRatio = projRatio;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
