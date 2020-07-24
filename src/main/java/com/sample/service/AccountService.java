package com.sample.service;

import com.sample.domain.Account;
import com.sample.domain.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account add(String userName, String userPw) {
        return accountRepository.save(Account.builder().userName(userName).userPw(userPw).build());
    }

    public Account update(String userName) {
        Account account = accountRepository.findByUserName(userName);
        account.update(userName);
        return account;
    }

    @Transactional(readOnly = true)
    public List<Account> list() {
        return accountRepository.findAll();
    }

    public Account get(String name, String pw) {
        Account account = accountRepository.findByUserNameAndUserPw(name, pw);
        log.info("account : {}", account);
        return account;
    }
}
