package indi.mybatis.flying.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import indi.mybatis.flying.exceptions.ConfigurerException;
import indi.mybatis.flying.service2.TransactiveService2;

//@Service
public class TransactiveService4 {

	@Autowired
	private TransactiveService transactiveService;

	@Autowired
	private TransactiveService2 transactiveService2;

	@Transactional(rollbackFor = {
			ConfigurerException.class }, readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void testTransactive() throws ConfigurerException {
		transactiveService2.addAccount2Transactive3();
		transactiveService.addAccountTransactive();
	}

}
