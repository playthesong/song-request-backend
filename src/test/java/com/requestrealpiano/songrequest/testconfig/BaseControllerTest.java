package com.requestrealpiano.songrequest.testconfig;

import com.requestrealpiano.songrequest.controller.LetterController;
import com.requestrealpiano.songrequest.controller.restdocs.RestDocsConfiguration;
import com.requestrealpiano.songrequest.security.SecurityConfig;
import com.requestrealpiano.songrequest.testconfig.security.SecurityTestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@ContextConfiguration(classes = {RestDocsConfiguration.class, SecurityTestConfig.class})
@AutoConfigureRestDocs
@WebMvcTest(controllers = LetterController.class,
            excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class))
public class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;
}
