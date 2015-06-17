package org.montpellierjug.store;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.jooq.DSLContext;
import org.montpellierjug.store.jooq.Tables;
import org.montpellierjug.store.jooq.tables.pojos.Speaker;

import java.util.List;

/**
 * Created by chelebithil on 26/04/15.
 */
@Provides
@Component
@Instantiate
public class SpeakerDaoCustom  {


    @Requires
    DSLContext dsl;

    public List<Speaker> findAllMembers(){
        return dsl.select().from(Tables.SPEAKER).where(Tables.SPEAKER.JUGMEMBER.isTrue()).fetchInto(Speaker.class);
    }

}
