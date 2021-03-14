package com.requestrealpiano.songrequest.testobject;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.security.jwt.JwtProperties;
import com.requestrealpiano.songrequest.security.jwt.JwtTokenProvider;

import static com.requestrealpiano.songrequest.testobject.AccountFactory.createMember;

public class JwtFactory {

    /*
     *
     * createMockObject()
     *   - Test parameter 에 의존하지 않는 테스트 객체 생성
     *     (ex. Mocking 에서 자주 사용되는 테스트 객체)
     *
     *
     * createMockObjectOf(T parameter1, T parameter2, ...)
     *   - Test parameter 에 의존하는 테스트 객체 생성
     *     (ex. 예외 검증, 경우의 수를 따져야 하는 테스트)
     *
     */

    // JwtProperties - Valid
    public static JwtProperties createValidJwtProperties() {
        return new JwtProperties("TestJwtSecret", "100000", "Authorization",
                     "Bearer ", "http://localhost:3000", "TestGenerationKeySecret", "10000");
    }

    // JwtProperties - Invalid
    public static JwtProperties createInvalidJwtProperties() {
        return new JwtProperties("InvalidJwtTokenSecret", "10000",
                          "Authorization", "Bearer ", "http://localhost:3000", "InvalidTestGenerationKey", "10000");
    }

    // JwtProperties - Expired
    public static JwtProperties createExpiredJwtProperties() {
        return new JwtProperties("TestJwtSecret", "0", "Authorization",
                     "Bearer ", "http://localhost:3000", "TestGenerationKeySecret", "10000");
    }

    // JwtProperties - Invalid GenerationKey
    public static JwtProperties createInvalidGenerationKeyJwtProperties() {
        return new JwtProperties("TestJwtSecret", "100000", "Authorization",
                     "Bearer ", "http://localhost:3000", "InvalidGenerationKeySecret", "10000");
    }

    // JwtProperties - Expired GenerationKey
    public static JwtProperties createExpiredGenerationKeyJwtProperties() {
        return new JwtProperties("TestJwtSecret", "100000", "Authorization",
                     "Bearer ", "http://localhost:3000", "InvalidGenerationKeySecret", "0");
    }

    // JwtToken - Valid
    public static String createValidJwtTokenOf(Account account) {
        JwtProperties jwtProperties = createValidJwtProperties();
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(jwtProperties);
        return jwtTokenProvider.createJwtToken(account);
    }

    // JwtToken - Invalid
    public static String createInvalidJwtTokenOf(Account account) {
        JwtProperties jwtProperties = createInvalidJwtProperties();
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(jwtProperties);
        return jwtTokenProvider.createJwtToken(account);
    }

    // JwtToken - Expired
    public static String createExpiredJwtTokenOf(Account account) {
        JwtProperties jwtProperties = createExpiredJwtProperties();
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(jwtProperties);
        return jwtTokenProvider.createJwtToken(account);
    }

    // GenerationKey - Valid
    public static String createValidGenerationKeyOf(String email) {
        JwtProperties jwtProperties = createValidJwtProperties();
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(jwtProperties);
        return "Bearer " + jwtTokenProvider.createGenerationKey(email);
    }

    // GenerationKey - Invalid
    public static String createInvalidGenerationKeyOf(String email) {
        JwtProperties jwtProperties = createInvalidGenerationKeyJwtProperties();
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(jwtProperties);
        return "Bearer " + jwtTokenProvider.createGenerationKey(email);
    }

    // GenerationKey - Expired
    public static String createExpiredGenerationKeyOf(String email) {
        JwtProperties jwtProperties = createExpiredGenerationKeyJwtProperties();
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(jwtProperties);
        return "Bearer " + jwtTokenProvider.createGenerationKey(email);
    }

}
