package live.playthesong.songrequest.testconfig;

import live.playthesong.songrequest.config.jpa.JpaConfig;
import live.playthesong.songrequest.global.time.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import({JpaConfig.class, Scheduler.class})
@DataJpaTest
public class BaseRepositoryTest {

    @Autowired
    protected Scheduler scheduler;
}
