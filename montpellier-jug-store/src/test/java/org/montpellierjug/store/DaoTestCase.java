package org.montpellierjug.store;

import java.sql.Connection;
import java.sql.DriverManager;

import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DefaultConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chelebithil on 06/03/15.
 */
public abstract  class DaoTestCase {

    static final Logger LOGGER = LoggerFactory.getLogger(DaoTestCase.class);

    public void jooq(JooqCommand command) {
        String userName = "jug";
        String password = "jug";
        String url = "jdbc:postgresql:jugbuild";

        try (Connection conn = DriverManager.getConnection(url, userName, password)) {
            Configuration conf = new DefaultConfiguration();
            conf.set(new ConnectionProvider() {
                @Override
                public Connection acquire() throws DataAccessException {
                    return conn;
                }

				@Override
				public void release(Connection connection) throws DataAccessException {
					
				}

              
            });

            command.run(conf);
        }

        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
