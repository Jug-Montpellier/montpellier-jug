package org.montpellierjug.store.async;

import java.sql.Connection;
import java.sql.SQLException;

import org.jooq.Configuration;
import org.jooq.exception.DataAccessException;

/**
 * Created by cheleb on 30/06/15.
 */
public class AsyncConfiguration implements AutoCloseable {



    private final Connection connection;

    private final Configuration configuration;


    public AsyncConfiguration(Configuration configuration) throws SQLException {
        this.connection = configuration.connectionProvider().acquire();
        this.connection.setAutoCommit(false);
        this.configuration = configuration.derive(connection);
    }

    @Override
    public void close() throws Exception {
        configuration.connectionProvider().release(connection);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void commit()  {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public void rollback()  {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

}
