package net.bgmgr.account.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "email")
public class Account {

    @Id
    private String email;

    @JsonIgnore
    private String password;

    @Enumerated(EnumType.ORDINAL)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<AccountRole> roles = new HashSet<>();

    @Builder
    public Account(String email, String password, Set<AccountRole> roles) {
        Assert.notNull(email, "Not Null");
        Assert.notNull(password, "Not Null");
        Assert.notNull(roles, "Not Null");
        Assert.isTrue(roles.size() >= 1, "Not Null");

        this.email = email;
        this.password = password;
        this.roles = roles;
    }
}
