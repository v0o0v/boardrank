package net.bgmgr.account.controller;

import net.bgmgr.account.domain.Account;
import net.bgmgr.account.service.AccountService;
import net.bgmgr.global.exception.BusinessException;
import net.bgmgr.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/getInfo")
    public ResponseEntity<Account> getAccountInfo(@AuthenticationPrincipal User user) {

        Account account = this.accountService.getAccountByUsername(user.getUsername());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(account);
    }

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody @Valid AccountDTO.AccountCreateDTO dto) {

        if (this.accountService.isExist(dto.getEmail())) {
            throw new BusinessException("This account already exists.", ErrorCode.EMAIL_DUPLICATION);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.accountService.addAccount(dto.toEntity()))
                ;
    }

    @PutMapping("/changePassword")
    public ResponseEntity<Account> changePassword(@RequestBody @Valid AccountDTO.AccountChangePasswordDTO dto, @AuthenticationPrincipal User user) {

        if (!user.getUsername().equals(dto.getEmail())) {
            throw new BusinessException("Accounts do not match.", ErrorCode.HANDLE_ACCESS_DENIED);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.accountService.changePassword(dto.getEmail(), dto.getNewPassword()))
                ;
    }

//    @PostMapping("/getAccounts")
//    public ResponseEntity<Page<Account>> getAccounts(Pageable pageable
//            , @RequestBody(required = false) AccountDTO.AccountMgmRequestDTO requestDTO
//    ) {
//        if (requestDTO == null)
//            requestDTO = AccountDTO.AccountMgmRequestDTO.emptyOf();
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .body(this.accountService.getAccounts(pageable
//                        , requestDTO.getSearchTargetType()
//                        , requestDTO.getSearchText())
//                );
//    }
}
