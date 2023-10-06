package com.example.dating.account;

import com.example.dating.domain.Account;
import com.example.dating.repository.AccountRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class AccountTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void 회원가입() {
        Account account = new Account("user@user.com", "user1234");
        accountRepository.save(account);

        Optional<Account> findAccount = accountRepository.findByEmail("user@user.com");

        Assertions.assertThat(findAccount).isPresent();
    }
}