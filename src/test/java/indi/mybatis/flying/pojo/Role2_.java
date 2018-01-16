package indi.mybatis.flying.pojo;

import java.io.Serializable;

import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.annotations.TableMapperAnnotation;
import indi.mybatis.flying.pojoHelper.PojoSupport;

@TableMapperAnnotation(tableName = "role2_")
public class Role2_ extends PojoSupport<Role2_> implements Serializable {

	private static final long serialVersionUID = 1L;

	@FieldMapperAnnotation(dbFieldName = "id", jdbcType = JdbcType.INTEGER, isUniqueKey = true)
	private Integer id;

	@FieldMapperAnnotation(dbFieldName = "name", jdbcType = JdbcType.VARCHAR)
	private java.lang.String name;

	private java.util.Collection<Account2_> account;

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

	public void setId(Integer id) {
		this.id = id;
	}

	public java.util.Collection<Account2_> getAccount() {
		if (account == null) {
			account = new java.util.LinkedHashSet<Account2_>();
		}
		return account;
	}

	public java.util.Iterator<Account2_> getIteratorAccount() {
		if (account == null) {
			account = new java.util.LinkedHashSet<Account2_>();
		}
		return account.iterator();
	}

	public void setAccount(java.util.Collection<Account2_> newAccount) {
		removeAllAccount();
		for (java.util.Iterator<Account2_> iter = newAccount.iterator(); iter.hasNext();) {
			addAccount((Account2_) iter.next());
		}
	}

	public void addAccount(Account2_ newAccount) {
		if (newAccount == null) {
			return;
		}
		if (this.account == null) {
			this.account = new java.util.LinkedHashSet<Account2_>();
		}
		if (!this.account.contains(newAccount)) {
			this.account.add(newAccount);
			newAccount.setRole(this);
		} else {
			for (Account2_ temp : this.account) {
				if (newAccount.equals(temp)) {
					if (temp != newAccount) {
						removeAccount(temp);
						this.account.add(newAccount);
						newAccount.setRole(this);
					}
					break;
				}
			}
		}
	}

	public void removeAccount(Account2_ oldAccount) {
		if (oldAccount == null) {
			return;
		}
		if (this.account != null) {
			if (this.account.contains(oldAccount)) {
				for (Account2_ temp : this.account) {
					if (oldAccount.equals(temp)) {
						if (temp != oldAccount) {
							temp.setRole((Role2_) null);
						}
						break;
					}
				}
				this.account.remove(oldAccount);
				oldAccount.setRole((Role2_) null);
			}
		}
	}

	public void removeAllAccount() {
		if (account != null) {
			Account2_ oldAccount;
			for (java.util.Iterator<Account2_> iter = getIteratorAccount(); iter.hasNext();) {
				oldAccount = (Account2_) iter.next();
				iter.remove();
				oldAccount.setRole((Role2_) null);
			}
			account.clear();
		}
	}
}
