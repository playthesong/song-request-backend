package live.playthesong.songrequest.controller;

import live.playthesong.songrequest.domain.account.AccountDetail;
import live.playthesong.songrequest.global.response.ApiResponse;
import live.playthesong.songrequest.global.response.StatusCode;
import live.playthesong.songrequest.security.oauth.LoginAccount;
import live.playthesong.songrequest.security.oauth.OAuthAccount;
import live.playthesong.songrequest.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @GetMapping("/auth")
    public void generateToken(@RequestHeader HttpHeaders httpHeaders, HttpServletResponse response) {
        String jwtToken = accountService.generateJwtToken(httpHeaders.getFirst(HttpHeaders.AUTHORIZATION));
        response.addHeader(HttpHeaders.AUTHORIZATION, jwtToken);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @GetMapping("/auth/validation")
    public void validateToken(@RequestHeader HttpHeaders httpHeaders) {
        accountService.validateJwtToken(httpHeaders.getFirst(HttpHeaders.AUTHORIZATION));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/detail")
    public ApiResponse<AccountDetail> detail(@LoginAccount OAuthAccount loginAccount) {
        AccountDetail accountDetail = accountService.findAccountDetail(loginAccount);
        return ApiResponse.SUCCESS(StatusCode.OK, accountDetail);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    public void delete(@LoginAccount OAuthAccount loginAccount) {
        accountService.deleteAccount(loginAccount);
    }
}
