package net.boardrank.account.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.boardrank.account.domain.Account;
import net.boardrank.account.domain.AccountRole;
import org.springframework.util.Assert;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class AccountDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class AccountCreateDTO {

        @NotNull
        @Email
        private String email;

        @NotNull
        private String password;

        @NotNull
        private Set<AccountRole> role;

        @Builder
        public AccountCreateDTO(String email, String password, Set<AccountRole> role) {
            Assert.notNull(email, "Not Null");
            Assert.notNull(password, "Not Null");
            Assert.notNull(role, "Not Null");

            this.email = email;
            this.password = password;
            this.role = role;
        }

        public Account toEntity() {

            Account account = Account.builder()
                    .email(email)
                    .password(password)
                    .roles(role)
                    .build();

            return account;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class AccountChangePasswordDTO {

        @NotNull
        @Email
        private String email;

        @NotNull
        private String newPassword;

        @Builder
        public AccountChangePasswordDTO(String email, String newPassword) {
            Assert.notNull(email, "Not Null");
            Assert.notNull(newPassword, "Not Null");

            this.email = email;
            this.newPassword = newPassword;
        }
    }

//    @Getter
//    @Setter
//    @NoArgsConstructor
//    public static class AccountMgmRequestDTO {
//        SearchTargetType searchTargetType;
//        String searchText;
//
//        public static AccountMgmRequestDTO emptyOf() {
//            AccountMgmRequestDTO emptyDTO = new AccountMgmRequestDTO();
//            emptyDTO.searchTargetType = null;
//            emptyDTO.searchText = null;
//            return emptyDTO;
//        }
//    }
}
