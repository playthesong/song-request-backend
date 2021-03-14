package com.requestrealpiano.songrequest.testconfig;

import com.requestrealpiano.songrequest.controller.restdocs.RestDocsConfiguration;
import com.requestrealpiano.songrequest.testconfig.security.SecurityTestConfig;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {RestDocsConfiguration.class, SecurityTestConfig.class})
@AutoConfigureRestDocs
public class BaseControllerTest {

}
