package org.montpellierjug.store;

import org.jooq.Configuration;

/**
 * Created by chelebithil on 06/03/15.
 */
public interface JooqCommand {

    void run(Configuration conf);
}
