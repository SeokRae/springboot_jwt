package com.sample.service;

import com.sample.domain.account.Account;
import com.sample.domain.account.AccountRepository;
import com.sample.domain.refresh.RefreshTokenRepository;
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

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public void add(String userName, String userPw) {
        accountRepository.save(Account.builder().userName(userName).userPw(userPw).build());
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

    @Transactional(readOnly = true)
    public Account get(String name, String pw) {
        return accountRepository.findByUserNameAndUserPw(name, pw);
    }

    /* 유저네임으로 사용자 조회 */
    public Account getAccountByUserName(String userName) {
        return accountRepository.findByUserName(userName);
    }
}
