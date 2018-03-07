package indi.mybatis.flying.service2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import indi.mybatis.flying.exceptions.Configurer2Exception;
import indi.mybatis.flying.service.TransactiveService;

//@Service
public class TransactiveService3 {

	@Autowired
	private TransactiveService transactiveService;

	@Autowired
	private TransactiveService2 transactiveService2;

	@Transactional(rollbackFor = {
			Configurer2Exception.class }, readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void testTransactive() throws Configurer2Exception {
		transactiveService.addAccountTransactive2();
		transactiveService2.addAccount2Transactive();
	}

}
