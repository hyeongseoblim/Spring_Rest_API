package com.inflearn.study.account;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest {

    @Rule
    public ExpectedException expectedException =  ExpectedException.none();


    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Test
    public void findByUserName() {

        //given
        String email = "iii3@cnu.ac.kr";
        String password = "1234";
        Account account = Account.builder().email(email)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();

        this.accountRepository.save(account);


        //when
        UserDetailsService userDetailsService = (UserDetailsService) accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        //then
        assertThat(userDetails.getPassword()).isEqualTo(password);
    }


    @Test
    public void findByUsernameFail(){
        String username = "sdafsfsa";
//        try{
//            accountService.loadUserByUsername(username);
//            fail("supposed to be failed");
//        }catch(UsernameNotFoundException e){
//            assertThat(e.getMessage()).containsSequence(username);
//        }
        expectedException.expect(UsernameNotFoundException.class);
        expectedException.expectMessage(Matchers.containsString(username));

        accountService.loadUserByUsername(username);
    }
}