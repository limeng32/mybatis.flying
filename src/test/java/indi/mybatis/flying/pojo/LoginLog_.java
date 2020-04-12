package indi.mybatis.flying.pojo;

import java.io.Serializable;

import javax.persistence.Column;

import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.annotations.TableMapperAnnotation;
import indi.mybatis.flying.pojoHelper.PojoSupport;

@TableMapperAnnotation(tableName = "LoginLog_")
public class LoginLog_ extends PojoSupport<LoginLog_> implements Serializable {

	private static final long serialVersionUID = 1L;

	@FieldMapperAnnotation(dbFieldName = "iD", jdbcType = JdbcType.INTEGER, isUniqueKey = true, ignoreTag = {
			"noId" }, whiteListTag = "simple0")
	private Integer id;

	@Column
	private java.util.Date loginTime;

	@FieldMapperAnnotation(dbFieldName = "logiNIP", jdbcType = JdbcType.VARCHAR, whiteListTag = "simple0")
	private java.lang.String loginIP;

	@FieldMapperAnnotation(dbFieldName = "num", jdbcType = JdbcType.INTEGER)
	private Integer num;

	@FieldMapperAnnotation(dbFieldName = "logiNIP2", jdbcType = JdbcType.VARCHAR)
	private java.lang.String loginIP2;

	@FieldMapperAnnotation(dbFieldName = "status", jdbcType = JdbcType.VARCHAR)
	private LogStatus status;

	@FieldMapperAnnotation(dbFieldName = "accountId", jdbcType = JdbcType.INTEGER, dbAssociationUniqueKey = "id", whiteListTag = "simple0")
	private Account_ account;

	@FieldMapperAnnotation(dbFieldName = "accountId", jdbcType = JdbcType.INTEGER, delegate = true)
	private Long accountId;

	private java.util.Collection<Detail_> detail;

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public java.util.Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(java.util.Date loginTime) {
		this.loginTime = loginTime;
	}

	public java.lang.String getLoginIP() {
		return loginIP;
	}

	public void setLoginIP(java.lang.String loginIP) {
		this.loginIP = loginIP;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public java.lang.String getLoginIP2() {
		return loginIP2;
	}

	public void setLoginIP2(java.lang.String loginIP2) {
		this.loginIP2 = loginIP2;
	}

	public LogStatus getStatus() {
		return status;
	}

	public void setStatus(LogStatus status) {
		this.status = status;
	}

	public Account_ getAccount() {
		return account;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public void setAccount(Account_ newAccount) {
		if (this.account == null || !this.account.equals(newAccount)) {
			if (this.account != null) {
				Account_ oldAccount = this.account;
				this.account = null;
				oldAccount.removeLoginLog(this);
			}
			if (newAccount != null) {
				this.account = newAccount;
				this.account.addLoginLog(this);
			}
		}
	}

	public java.util.Collection<Detail_> getDetail() {
		if (detail == null) {
			detail = new java.util.LinkedHashSet<Detail_>();
		}
		return detail;
	}

	public java.util.Iterator<Detail_> getIteratorDetail() {
		if (detail == null) {
			detail = new java.util.LinkedHashSet<Detail_>();
		}
		return detail.iterator();
	}

	public void setDetail(java.util.Collection<Detail_> newDetail) {
		removeAllDetail();
		for (java.util.Iterator<Detail_> iter = newDetail.iterator(); iter.hasNext();) {
			addDetail((Detail_) iter.next());
		}
	}

	public void addDetail(Detail_ newDetail) {
		if (newDetail == null) {
			return;
		}
		if (this.detail == null) {
			this.detail = new java.util.LinkedHashSet<Detail_>();
		}
		if (!this.detail.contains(newDetail)) {
			this.detail.add(newDetail);
			newDetail.setLoginLog(this);
		} else {
			for (Detail_ temp : this.detail) {
				if (newDetail.equals(temp)) {
					if (temp != newDetail) {
						removeDetail(temp);
						this.detail.add(newDetail);
						newDetail.setLoginLog(this);
					}
					break;
				}
			}
		}
	}

	public void removeDetail(Detail_ oldDetail) {
		if (oldDetail == null) {
			return;
		}
		if (this.detail != null) {
			if (this.detail.contains(oldDetail)) {
				for (Detail_ temp : this.detail) {
					if (oldDetail.equals(temp)) {
						if (temp != oldDetail) {
							temp.setLoginLog((LoginLog_) null);
						}
						break;
					}
				}
				this.detail.remove(oldDetail);
				oldDetail.setLoginLog((LoginLog_) null);
			}
		}
	}

	public void removeAllDetail() {
		if (detail != null) {
			Detail_ oldDetail;
			for (java.util.Iterator<Detail_> iter = getIteratorDetail(); iter.hasNext();) {
				oldDetail = (Detail_) iter.next();
				iter.remove();
				oldDetail.setLoginLog((LoginLog_) null);
			}
			detail.clear();
		}
	}

}