package com.inflearn.study.account;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@Entity
public class Account {

    @Id
    @GeneratedValue
    private Integer id;

    private String email;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<AccountRole> roles;
}
