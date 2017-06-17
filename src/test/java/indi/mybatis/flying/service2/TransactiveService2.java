package indi.mybatis.flying.service2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import indi.mybatis.flying.exceptions.Configurer2Exception;
import indi.mybatis.flying.pojo.Role2_;

@Service
public class TransactiveService2 {

	@Autowired
	private Account2Service account2Service;

	@Autowired
	private Role2Service role2Service;

	@Transactional(rollbackFor = {
			Configurer2Exception.class }, readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void addAccount2Transactive() throws Configurer2Exception {
		Role2_ role2 = new Role2_();
		role2.setName("role2_");
		role2Service.insert(role2);
		throw new Configurer2Exception("asd");
	}
}
