package org.montpellierjug.store.async;

import java.sql.SQLException;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.jooq.Configuration;

/**
 * Created by cheleb on 30/06/15.
 */
@Component
@Provides
@Instantiate
public class AsyncConfigurationProvider {

    @Requires
    private Configuration configuration;


    public AsyncConfiguration asyncConfiguration() throws SQLException {
        return new AsyncConfiguration(configuration);
    }

}
