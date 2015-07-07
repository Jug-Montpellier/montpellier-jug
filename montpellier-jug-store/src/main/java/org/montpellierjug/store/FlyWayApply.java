package org.montpellierjug.store;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;

/**
 * Created by chelebithil on 05/03/15.
 */
public class FlyWayApply {


    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void init(){
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);

        flyway.setLocations("db.migration");

        flyway.setClassLoader(Flyway.class.getClassLoader());

        flyway.setSchemas("jug");
        flyway.repair();
        flyway.migrate();

    }

}
