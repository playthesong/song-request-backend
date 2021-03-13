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

    // JwtProperties
    public static JwtProperties createJwtProperties() {
        return new JwtProperties("TestJwtSecret", "100000", "Authorization",
                     "Bearer ", "http://localhost:3000", "TestGenerationKeySecret", "10000");
    }

    public static JwtProperties createJwtPropertiesOf(String tokenSecret, String tokenExpiration, String header,
                                                      String headerPrefix, String tokenUrl, String generationKeySecret,
                                                      String generationKeyExpiration) {
        return new JwtProperties(tokenSecret, tokenExpiration, header, headerPrefix, tokenUrl, generationKeySecret,
                                 generationKeyExpiration);
    }

    // JwtTokenProvider
    public static JwtTokenProvider createJwtTokenProvider() {
        return new JwtTokenProvider(createJwtProperties());
    }

    // JwtToken - Valid
    public static String createValidJwtToken() {
        JwtTokenProvider jwtTokenProvider = createJwtTokenProvider();
        return jwtTokenProvider.createJwtToken(createMember());
    }

    public static String createValidJwtTokenOf(Account account) {
        JwtTokenProvider jwtTokenProvider = createJwtTokenProvider();
        return jwtTokenProvider.createJwtToken(account);
    }

    // JwtToken - Invalid
    public static String createInvalidJwtTokenOf(Account account) {
        JwtProperties jwtProperties = createJwtPropertiesOf("InvalidJwtTokenSecret", "10000",
                                                     "Authorization", "Bearer ", "http://localhost:3000", "InvalidTestGenerationKey", "10000");
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(jwtProperties);
        return jwtTokenProvider.createJwtToken(account);
    }

    // JwtToken - Expired
    public static String createExpiredJwtTokenOf(Account account) {
        JwtProperties jwtProperties = createJwtPropertiesOf("TestJwtSecret", "0", "Authorization",
                                                "Bearer ", "http://localhost:3000", "TestGenerationKeySecret", "10000");
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(jwtProperties);
        return jwtTokenProvider.createJwtToken(account);
    }
}