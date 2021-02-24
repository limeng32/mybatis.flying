package indi.mybatis.flying.pojo;

import java.util.Date;

import org.apache.ibatis.type.JdbcType;

import com.alibaba.fastjson.annotation.JSONField;

import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.annotations.TableMapperAnnotation;
import indi.mybatis.flying.handler.ByteArrayHandler;

/**
 * 个人考核结果
 */
@TableMapperAnnotation(tableName = "t_emp_score")
public class EmpScore {
	/**
	 * 考核结果编号
	 */
	@FieldMapperAnnotation(dbFieldName = "id", jdbcType = JdbcType.BIGINT, isUniqueKey = true)
	private Long id;
	@FieldMapperAnnotation(dbFieldName = "ou", jdbcType = JdbcType.VARCHAR)
	private String ou;
	/**
	 * 部门名称
	 */
	@FieldMapperAnnotation(dbFieldName = "dept_name", jdbcType = JdbcType.VARCHAR)
	private String deptName;
	/**
	 * 员工编码
	 */
	@FieldMapperAnnotation(dbFieldName = "staff_id", jdbcType = JdbcType.VARCHAR)
	private String staffId;
	/**
	 * 员工姓名
	 */
	@FieldMapperAnnotation(dbFieldName = "staff_name", jdbcType = JdbcType.VARCHAR)
	private String staffName;
	/**
	 * 考核年度
	 */
	@FieldMapperAnnotation(dbFieldName = "year", jdbcType = JdbcType.VARCHAR)
	private String year;
	/**
	 * 考核季度（0：年度考核 1~4：季度考核）
	 */
	@FieldMapperAnnotation(dbFieldName = "season", jdbcType = JdbcType.VARCHAR)
	private String season;
	/**
	 * 考核结果
	 */
	@FieldMapperAnnotation(dbFieldName = "score", jdbcType = JdbcType.DOUBLE)
	private Double score;
	/**
	 * 考核得分系数
	 */
	@FieldMapperAnnotation(dbFieldName = "score_coefficient", jdbcType = JdbcType.DOUBLE)
	private Double scoreCoefficient;
	/**
	 * 备注
	 */
	@FieldMapperAnnotation(dbFieldName = "remark", jdbcType = JdbcType.VARCHAR)
	private String remark;
	/**
	 * 绩效结果类型（0：综合绩效 1：项目绩效）
	 */
	@FieldMapperAnnotation(dbFieldName = "score_type", jdbcType = JdbcType.VARCHAR)
	private String scoreType;
	/**
	 * 员工类别（0：普通员工 1：项目经理 2：分院常设机构负责人 3.小组长 4.业务架构师 5.年度考核中分中心经理 6.副总架构师 7.BP 8.巡查组）
	 */
	@FieldMapperAnnotation(dbFieldName = "emp_type", jdbcType = JdbcType.VARCHAR)
	private String empType;
	/**
	 * 员工职位名称
	 */
	@FieldMapperAnnotation(dbFieldName = "post_name", jdbcType = JdbcType.VARCHAR)
	private String postName;
	/**
	 * 指标状态(0：未录入 1：待审核 2：审核未通过 3：待评价 4：待确认 5：复议 6：评价完成，项目内待评级 7：部门内待评级 10：考核完成）
	 */
	@FieldMapperAnnotation(dbFieldName = "state", jdbcType = JdbcType.VARCHAR)
	private String state;
	/**
	 * 考核等级(A:1.25 B:1.1 C:1.00 D:0.8 )
	 */
	@FieldMapperAnnotation(dbFieldName = "rank", jdbcType = JdbcType.VARCHAR)
	private String rank;
	/**
	 * 标志位（0：综合/项目绩效考核结果 1：部门内考核结果）
	 */
	@FieldMapperAnnotation(dbFieldName = "tag", jdbcType = JdbcType.VARCHAR)
	private String tag;
	@FieldMapperAnnotation(dbFieldName = "proj_id", jdbcType = JdbcType.VARCHAR)
	private String projId;
	@FieldMapperAnnotation(dbFieldName = "proj_name", jdbcType = JdbcType.VARCHAR)
	private String projName;
	/**
	 * 员工季度工时
	 */
	@FieldMapperAnnotation(dbFieldName = "hours", jdbcType = JdbcType.DOUBLE)
	private Double hours = 1.0;
	/**
	 * 正态分布结果调整人id
	 */
	@FieldMapperAnnotation(dbFieldName = "checker_id", jdbcType = JdbcType.VARCHAR)
	private String checkerId;
	/**
	 * 正态分布结果调整人姓名
	 */
	@FieldMapperAnnotation(dbFieldName = "checker_name", jdbcType = JdbcType.VARCHAR)
	private String checkerName;
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@FieldMapperAnnotation(dbFieldName = "create_time", jdbcType = JdbcType.TIMESTAMP)
	private Date createTime;
	@FieldMapperAnnotation(dbFieldName = "cont_degree", jdbcType = JdbcType.DOUBLE)
	private Double contDegree;
	@FieldMapperAnnotation(dbFieldName = "adjust_reason", jdbcType = JdbcType.VARCHAR)
	private String adjustReason;
	/**
	 * 审核未通过理由
	 */
	@FieldMapperAnnotation(dbFieldName = "unpass_reason", jdbcType = JdbcType.VARCHAR)
	private String unpassReason;
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@FieldMapperAnnotation(dbFieldName = "update_time", jdbcType = JdbcType.TIMESTAMP)
	private Date updateTime;

	/**
	 * 考核等级加密
	 */
	@FieldMapperAnnotation(dbFieldName = "rank_encrypt", jdbcType = JdbcType.VARCHAR)
	private String rankEncrypt;

	@FieldMapperAnnotation(dbFieldName = "secret2", jdbcType = JdbcType.VARCHAR, customTypeHandler = ByteArrayHandler.class, cryptKeyColumn = "staff_id")
	private String secret2;

	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param newUpdateTime
	 */
	public void setUpdateTime(Date newUpdateTime) {
		updateTime = newUpdateTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOu() {
		return ou;
	}

	public void setOu(String ou) {
		this.ou = ou;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public Double getScoreCoefficient() {
		return scoreCoefficient;
	}

	public void setScoreCoefficient(Double scoreCoefficient) {
		this.scoreCoefficient = scoreCoefficient;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getScoreType() {
		return scoreType;
	}

	public void setScoreType(String scoreType) {
		this.scoreType = scoreType;
	}

	public String getEmpType() {
		return empType;
	}

	public void setEmpType(String empType) {
		this.empType = empType;
	}

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getProjId() {
		return projId;
	}

	public void setProjId(String projId) {
		this.projId = projId;
	}

	public String getProjName() {
		return projName;
	}

	public void setProjName(String projName) {
		this.projName = projName;
	}

	public Double getHours() {
		return hours;
	}

	public void setHours(Double hours) {
		this.hours = hours;
	}

	public String getCheckerId() {
		return checkerId;
	}

	public void setCheckerId(String checkerId) {
		this.checkerId = checkerId;
	}

	public String getCheckerName() {
		return checkerName;
	}

	public void setCheckerName(String checkerName) {
		this.checkerName = checkerName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Double getContDegree() {
		return contDegree;
	}

	public void setContDegree(Double contDegree) {
		this.contDegree = contDegree;
	}

	public String getAdjustReason() {
		return adjustReason;
	}

	public void setAdjustReason(String adjustReason) {
		this.adjustReason = adjustReason;
	}

	public String getUnpassReason() {
		return unpassReason;
	}

	public void setUnpassReason(String unpassReason) {
		this.unpassReason = unpassReason;
	}

	public String getRankEncrypt() {
		return rankEncrypt;
	}

	public void setRankEncrypt(String rankEncrypt) {
		this.rankEncrypt = rankEncrypt;
	}

	public String getSecret2() {
		return secret2;
	}

	public void setSecret2(String secret2) {
		this.secret2 = secret2;
	}

}