package indi.mybatis.flying.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import indi.mybatis.flying.exceptions.Configurer2Exception;
import indi.mybatis.flying.exceptions.ConfigurerException;
import indi.mybatis.flying.pojo.Role_;

@Service
public class TransactiveService {

	@Autowired
	private AccountService accountService;

	@Autowired
	private RoleService roleService;

	@Transactional(rollbackFor = {
			ConfigurerException.class }, readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void addAccountTransactive() throws Configurer2Exception {
		Role_ role2 = new Role_();
		role2.setName("role_");
		roleService.insert(role2);
		throw new Configurer2Exception("qwe");
	}
}
