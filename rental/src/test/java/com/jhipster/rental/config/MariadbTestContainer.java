package com.jhipster.rental.config;

import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

public class MariadbTestContainer implements SqlTestContainer {

    private static final Logger log = LoggerFactory.getLogger(MariadbTestContainer.class);

    private MariaDBContainer<?> mariaDBContainer;

    @Override
    public void destroy() {
        if (null != mariaDBContainer && mariaDBContainer.isRunning()) {
            mariaDBContainer.stop();
        }
    }

    @Override
    public void afterPropertiesSet() {
        if (null == mariaDBContainer) {
            mariaDBContainer =
                new MariaDBContainer<>("mariadb:10.11.3")
                    .withDatabaseName("rental")
                    .withTmpFs(Collections.singletonMap("/testtmpfs", "rw"))
                    .withLogConsumer(new Slf4jLogConsumer(log))
                    .withReuse(true);
        }
        if (!mariaDBContainer.isRunning()) {
            mariaDBContainer.start();
        }
    }

    @Override
    public JdbcDatabaseContainer<?> getTestContainer() {
        return mariaDBContainer;
    }
}
