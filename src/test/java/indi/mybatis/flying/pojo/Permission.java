package indi.mybatis.flying.pojo;

import java.io.Serializable;

import javax.persistence.Table;

import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.pojoHelper.PojoSupport;

//用于测试复杂外键关联条件的实体
@Table
public class Permission extends PojoSupport<Permission> implements Serializable {

	private static final long serialVersionUID = 1L;

	@FieldMapperAnnotation(dbFieldName = "id", jdbcType = JdbcType.INTEGER, isUniqueKey = true)
	private Integer id;

	@FieldMapperAnnotation(dbFieldName = "fake_id", jdbcType = JdbcType.INTEGER)
	private Integer fakeId;

	@FieldMapperAnnotation(dbFieldName = "name", jdbcType = JdbcType.VARCHAR)
	private java.lang.String name;

	private java.util.Collection<Account_> account;

	@Override
	public Integer getId() {
		return id;
	}

	public Integer getFakeId() {
		return fakeId;
	}

	public void setFakeId(Integer fakeId) {
		this.fakeId = fakeId;
	}

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public java.util.Collection<Account_> getAccount() {
		if (account == null) {
			account = new java.util.LinkedHashSet<Account_>();
		}
		return account;
	}

	public java.util.Iterator<Account_> getIteratorAccount() {
		if (account == null) {
			account = new java.util.LinkedHashSet<Account_>();
		}
		return account.iterator();
	}

	public void setAccount(java.util.Collection<Account_> newAccount) {
		removeAllAccount();
		for (java.util.Iterator<Account_> iter = newAccount.iterator(); iter.hasNext();) {
			addAccount((Account_) iter.next());
		}
	}

	public void addAccount(Account_ newAccount) {
		if (newAccount == null) {
			return;
		}
		if (this.account == null) {
			this.account = new java.util.LinkedHashSet<Account_>();
		}
		if (!this.account.contains(newAccount)) {
			this.account.add(newAccount);
			newAccount.setPermission(this);
		} else {
			for (Account_ temp : this.account) {
				if (newAccount.equals(temp)) {
					if (temp != newAccount) {
						removeAccount(temp);
						this.account.add(newAccount);
						newAccount.setPermission(this);
					}
					break;
				}
			}
		}
	}

	public void removeAccount(Account_ oldAccount) {
		if (oldAccount == null) {
			return;
		}
		if (this.account != null) {
			if (this.account.contains(oldAccount)) {
				for (Account_ temp : this.account) {
					if (oldAccount.equals(temp)) {
						if (temp != oldAccount) {
							temp.setPermission(null);
						}
						break;
					}
				}
				this.account.remove(oldAccount);
				oldAccount.setPermission(null);
			}
		}
	}

	public void removeAllAccount() {
		if (account != null) {
			Account_ oldAccount;
			for (java.util.Iterator<Account_> iter = getIteratorAccount(); iter.hasNext();) {
				oldAccount = (Account_) iter.next();
				iter.remove();
				oldAccount.setPermission(null);
			}
			account.clear();
		}
	}
}