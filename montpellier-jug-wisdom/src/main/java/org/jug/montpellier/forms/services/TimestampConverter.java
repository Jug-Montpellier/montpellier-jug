package org.jug.montpellier.forms.services;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.wisdom.api.content.ParameterConverter;

/**
 * Created by Eric Taix on 23/03/15.
 */
@Component
@Provides(specifications = ParameterConverter.class)
@Instantiate
public class TimestampConverter implements ParameterConverter<Timestamp> {

    @Override
    public Timestamp fromString(String input) throws IllegalArgumentException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd");
        try {
            Date date = simpleDateFormat.parse(input);
            return new Timestamp(date.getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException();
        }


    }

    @Override
    public Class getType() {
        return Timestamp.class;
    }
}
