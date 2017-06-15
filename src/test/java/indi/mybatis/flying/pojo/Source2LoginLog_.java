package indi.mybatis.flying.pojo;

import java.io.Serializable;

import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.annotations.TableMapperAnnotation;
import indi.mybatis.flying.pojoHelper.PojoSupport;

@TableMapperAnnotation(tableName = "LoginLog_")
public class Source2LoginLog_ extends PojoSupport<Source2LoginLog_> implements Serializable {

	private static final long serialVersionUID = 1L;

	@FieldMapperAnnotation(dbFieldName = "iD", jdbcType = JdbcType.INTEGER, isUniqueKey = true)
	private Integer id;

	@FieldMapperAnnotation(dbFieldName = "loginTime", jdbcType = JdbcType.TIMESTAMP)
	private java.util.Date loginTime;

	@FieldMapperAnnotation(dbFieldName = "logiNIP", jdbcType = JdbcType.VARCHAR)
	private java.lang.String loginIP;

//	@FieldMapperAnnotation(dbFieldName = "accountId", jdbcType = JdbcType.INTEGER, dbAssociationUniqueKey = "ID")
//	private Account_ account;

//	private java.util.Collection<Detail_> detail;

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

//	public Account_ getAccount() {
//		return account;
//	}
//
//	public void setAccount(Account_ newAccount) {
//		if (this.account == null || !this.account.equals(newAccount)) {
//			if (this.account != null) {
//				Account_ oldAccount = this.account;
//				this.account = null;
//				oldAccount.removeLoginLog(this);
//			}
//			if (newAccount != null) {
//				this.account = newAccount;
//				this.account.addLoginLog(this);
//			}
//		}
//	}
//
//	public java.util.Collection<Detail_> getDetail() {
//		if (detail == null)
//			detail = new java.util.LinkedHashSet<Detail_>();
//		return detail;
//	}
//
//	public java.util.Iterator<Detail_> getIteratorDetail() {
//		if (detail == null)
//			detail = new java.util.LinkedHashSet<Detail_>();
//		return detail.iterator();
//	}
//
//	public void setDetail(java.util.Collection<Detail_> newDetail) {
//		removeAllDetail();
//		for (java.util.Iterator<Detail_> iter = newDetail.iterator(); iter.hasNext();)
//			addDetail((Detail_) iter.next());
//	}
//
//	public void addDetail(Detail_ newDetail) {
//		if (newDetail == null)
//			return;
//		if (this.detail == null)
//			this.detail = new java.util.LinkedHashSet<Detail_>();
//		if (!this.detail.contains(newDetail)) {
//			this.detail.add(newDetail);
//			newDetail.setLoginLog(this);
//		}
//	}
//
//	public void removeDetail(Detail_ oldDetail) {
//		if (oldDetail == null)
//			return;
//		if (this.detail != null)
//			if (this.detail.contains(oldDetail)) {
//				this.detail.remove(oldDetail);
//				oldDetail.setLoginLog((LoginLog_) null);
//			}
//	}
//
//	public void removeAllDetail() {
//		if (detail != null) {
//			Detail_ oldDetail;
//			for (java.util.Iterator<Detail_> iter = getIteratorDetail(); iter.hasNext();) {
//				oldDetail = (Detail_) iter.next();
//				iter.remove();
//				oldDetail.setLoginLog((LoginLog_) null);
//			}
//		}
//	}

}