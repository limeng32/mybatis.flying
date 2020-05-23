# mybatis.flying - 极光 （Aurora）

[![Build Status](https://travis-ci.org/limeng32/mybatis.flying.svg?branch=master)](https://travis-ci.org/limeng32/mybatis.flying)
[![Codecov](https://codecov.io/gh/limeng32/mybatis.flying/branch/master/graph/badge.svg)](https://codecov.io/gh/limeng32/mybatis.flying/branch/master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.limeng32/mybatis.flying/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.limeng32/mybatis.flying)
[![GitHub release](https://img.shields.io/github/release/limeng32/mybatis.flying.svg)](https://github.com/limeng32/mybatis.flying/releases)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

项目介绍请见 [flying-doc.limeng32.com](http://flying-doc.limeng32.com)（国外）[https://flyingdoc.gitee.io](https://flyingdoc.gitee.io)（国内） ，国内代码托管网站请见 [gitee.com/limeng32/mybatis.flying](http://gitee.com/limeng32/mybatis.flying)，我们为开发最好的 mybatis 插件而努力。

flying 是一个可以极大增加 mybatis 开发速度的插件组，它提供了一种全新的操作数据的方式，希望能对您有所帮助。

众所周知，mybatis 虽然易于上手，但放到互联网环境下使用时，不可避免的要面对诸如‘’一级缓存存在脏数据‘’、‘’需要写大量明文 SQL 语句‘’等问题。对于这些问题 mybatis 的开发团队选择了一种谦逊的方式，他们开放 mybatis 接口，允许用户开发插件，按自己的方式来解决这些问题。于是，一切 ORM 领域相关的问题在 mybatis 上通过插件都有了解决方案。

#### flying 主要特点：

以前我们在 mapper.xml 中要写很复杂的 sql 语句，但现在在 mapper.xml 中只需这样：

```xml
    <select id="select" resultMap="result">
        {"action":"select#{?}"}
    </select>

    <select id="selectOne" resultMap="result">
        {"action":"selectOne"}
    </select>

    <insert id="insert">
        {"action":"insert"}
    </insert>

    <update id="update">
        {"action":"update"}
    </update>

    <delete id="delete">
        {"action":"delete"}
    </delete>
```
再在您的实体类上加上这样一些标注：

```Java
package myPackage;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
    
@Table(name = "account")
public class Account {
    @Id
    @Column
    private Integer id;
	    
    @Column
    private java.lang.String name;

    @Column
    private Integer age;
	    
    /* 省略 getter 和 setter */
}
```

 flying 就完全明白您的数据结构和您想做的事情了。 接下来您增删改查这个实体就会变得非常简单：
 
```Java
    /* 新增 */
    Account newAccount = new Account();
    newAccount.setName("ann");
    newAccount.setAge(18);
    accountService.insert(newAccount);

    /* 按主键查询 */
    Account account = accountService.select(newAccount.getId());
    
    /* 按姓名查询，这里忽略了年龄 */
    Account accountC1 = new Account();
    accountC1.setName("ann");
    Account account1 = accountService.selectOne(accountC1);
    /* account1 和 account 代表相同的业务数据 */
    
    /* 按年龄查询，这里忽略了姓名 */
    Account accountC2 = new Account();
    accountC2.setAge(18);
    Account account2 = accountService.selectOne(accountC2);
    /* account2 和 account 代表相同的业务数据 */
    
    /* 按姓名和年龄查询 */
    Account accountC3 = new Account();
    accountC3.setName("ann");
    accountC3.setAge(18);
    Account account3 = accountService.selectOne(accountC3);
    /* account3 和 account 代表相同的业务数据 */
    
    /* 修改 */
    account.setName("bob");
    accountService.update(newAccount);
    
    /* 按主键删除 */
    accountService.delete(newAccount);
```

由于 flying 掌握了您全部的数据结构和实体关系，所以操作数据变得非常简单，您再也不需要定义 “getAccountById、getAccountByName、getAccountByAge” 这样重复性强的方法了，由此带来更大的好处是您的 service 层只需要关注事务方面的逻辑即可，它从低级代码中完全解放了出来。以上只是 flying 功能的冰山一角，其它的功能如多表联查、分页、乐观锁、或逻辑查询、索引、复杂外键关系、批量执行等 flying 都有简单的解决方案，您可以在 [flying-doc.limeng32.com](http://flying-doc.limeng32.com)（国外）[https://flyingdoc.gitee.io](https://flyingdoc.gitee.io)（国内）中进行查看。

flying 特点总结如下：

- 数据操作入参和返回类型都是自定义的实体类，完全 no sql，杜绝各种‘’手滑‘’，项目可随意重构。

- 非侵占工作机制，可以和您已有的 mybatis 方法协同工作。

- 灵活定制查询字段黑名单和白名单，方便您管理敏感数据。

- 可以自定义主键生成器，全面支持或逻辑查询。（初雪版新增特性）

- 支持复杂的外键关联方式。（清明版新增特性）

- 支持使用索引查询，支持批量执行增删改。（极光版新增特性）

#### flying 获取方式：

  flying 最新版的 maven 坐标为：

```xml
    <groupId>com.github.limeng32</groupId>
    <artifactId>mybatis.flying</artifactId>
    <version>1.0.0</version>
```

mybatis 版本与 flying 版本的对应关系见下：

| mybatis 版本     | flying 版本   |  英文标题   |
|:--------|:-------:|-------:|
| `3.4.x` | `0.9.4 阳春` | `sunny-spring` |
| `3.4.x` | `0.9.9 清明` | `qing-ming` |
| `3.4.x` | `1.0.0 极光` | `Aurora` |
 
#### flying 代码示例：
我们还为您提供了一个快速上手的示例：

1. 最新版本demo：[https://gitee.com/limeng32/flying-aurora-demo](https://gitee.com/limeng32/flying-aurora-demo)

更多内容请您参见软件文档 [flying-doc.limeng32.com](http://flying-doc.limeng32.com)（国外）[https://flyingdoc.gitee.io](https://flyingdoc.gitee.io)（国内）。
