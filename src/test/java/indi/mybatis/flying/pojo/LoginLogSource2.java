package indi.mybatis.flying.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.annotations.TableMapperAnnotation;
import indi.mybatis.flying.pojoHelper.PojoSupport;

@TableMapperAnnotation(tableName = "LoginLog_")
public class LoginLogSource2 extends PojoSupport<LoginLogSource2> implements Serializable {

	private static final long serialVersionUID = 1L;

	@FieldMapperAnnotation(dbFieldName = "iD", jdbcType = JdbcType.INTEGER, isUniqueKey = true)
	private Integer id;

	@Column
	private java.util.Date loginTime;

	@Column(columnDefinition = "date foo bar")
	private Date madetime;

	@Column(columnDefinition = "  ")
	private Date confirmtime;

	@FieldMapperAnnotation(dbFieldName = "logiNIP", jdbcType = JdbcType.VARCHAR)
	private java.lang.String loginIP;

	@FieldMapperAnnotation(dbFieldName = "accountId", jdbcType = JdbcType.INTEGER, ignoreTag = {
			"noAccount" }, dbAssociationTypeHandler = indi.mybatis.flying.typeHandler.AccountTypeHandler.class)
	private Account_ account;

	private java.util.Collection<Detail2_> detail2;

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

	public Date getMadetime() {
		return madetime;
	}

	public void setMadetime(Date madetime) {
		this.madetime = madetime;
	}

	public Date getConfirmtime() {
		return confirmtime;
	}

	public void setConfirmtime(Date confirmtime) {
		this.confirmtime = confirmtime;
	}

	public Account_ getAccount() {
		return account;
	}

	public void setAccount(Account_ newAccount) {
		if (this.account == null || !this.account.equals(newAccount)) {
			if (this.account != null) {
				Account_ oldAccount = this.account;
				this.account = null;
				oldAccount.removeLoginLogSource2(this);
			}
			if (newAccount != null) {
				this.account = newAccount;
				this.account.addLoginLogSource2(this);
			}
		}
	}

	public java.util.Collection<Detail2_> getDetail2() {
		if (detail2 == null) {
			detail2 = new java.util.LinkedHashSet<Detail2_>();
		}
		return detail2;
	}

	public java.util.Iterator<Detail2_> getIteratorDetail2() {
		if (detail2 == null) {
			detail2 = new java.util.LinkedHashSet<Detail2_>();
		}
		return detail2.iterator();
	}

	public void setDetail2(java.util.Collection<Detail2_> newDetail2) {
		removeAllDetail2();
		for (java.util.Iterator<Detail2_> iter = newDetail2.iterator(); iter.hasNext();) {
			addDetail2((Detail2_) iter.next());
		}
	}

	public void addDetail2(Detail2_ newDetail2) {
		if (newDetail2 == null) {
			return;
		}
		if (this.detail2 == null) {
			this.detail2 = new java.util.LinkedHashSet<Detail2_>();
		}
		if (!this.detail2.contains(newDetail2)) {
			this.detail2.add(newDetail2);
			newDetail2.setLoginLogSource2(this);
		} else {
			for (Detail2_ temp : this.detail2) {
				if (newDetail2.equals(temp)) {
					if (temp != newDetail2) {
						removeDetail2(temp);
						this.detail2.add(newDetail2);
						newDetail2.setLoginLogSource2(this);
					}
					break;
				}
			}
		}
	}

	public void removeDetail2(Detail2_ oldDetail2) {
		if (oldDetail2 == null) {
			return;
		}
		if (this.detail2 != null) {
			if (this.detail2.contains(oldDetail2)) {
				for (Detail2_ temp : this.detail2) {
					if (oldDetail2.equals(temp)) {
						if (temp != oldDetail2) {
							temp.setLoginLogSource2((LoginLogSource2) null);
						}
						break;
					}
				}
				this.detail2.remove(oldDetail2);
				oldDetail2.setLoginLogSource2((LoginLogSource2) null);
			}
		}
	}

	public void removeAllDetail2() {
		if (detail2 != null) {
			Detail2_ oldDetail2;
			for (java.util.Iterator<Detail2_> iter = getIteratorDetail2(); iter.hasNext();) {
				oldDetail2 = (Detail2_) iter.next();
				iter.remove();
				oldDetail2.setLoginLogSource2((LoginLogSource2) null);
			}
			detail2.clear();
		}
	}
}