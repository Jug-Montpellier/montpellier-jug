package org.jug.montpellier.forms.widsom.paramconverter;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.jug.montpellier.models.Event;
import org.montpellierjug.store.jooq.tables.interfaces.IEvent;
import org.wisdom.api.content.ParameterConverter;

/**
 * Created by fteychene on 04/06/2015.
 */
@Component
@Provides(specifications = ParameterConverter.class)
@Instantiate
public class EventParamConverter implements ParameterConverter<IEvent> {

    @Override
    public IEvent fromString(String input) throws IllegalArgumentException {
        Long id = Long.valueOf(input);
        IEvent event = new Event();
        event.setId(id);
        return event;
    }

    @Override
    public Class<IEvent> getType() {
        return IEvent.class;
    }
}
