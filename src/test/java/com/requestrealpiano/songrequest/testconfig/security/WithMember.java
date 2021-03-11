package com.requestrealpiano.songrequest.testconfig.security;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(roles = "MEMBER")
public @interface WithMember {
}
