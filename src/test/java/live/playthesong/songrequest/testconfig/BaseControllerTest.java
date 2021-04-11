package live.playthesong.songrequest.testconfig;

import live.playthesong.songrequest.controller.restdocs.RestDocsConfiguration;
import live.playthesong.songrequest.testconfig.security.SecurityTestConfig;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {RestDocsConfiguration.class, SecurityTestConfig.class})
@AutoConfigureRestDocs
public class BaseControllerTest {

}
