package com.sample.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUserName(String userName);

    Account findByUserNameAndUserPw(String userName, String userPw);
}
