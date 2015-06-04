package org.jug.montpellier.forms.widsom.paramconverter;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.jug.montpellier.models.Talk;
import org.montpellierjug.store.jooq.tables.interfaces.ITalk;
import org.wisdom.api.content.ParameterConverter;

/**
 * Created by fteychene on 04/06/2015.
 */
@Component
@Provides(specifications = ParameterConverter.class)
@Instantiate
public class TalkParamConverter implements ParameterConverter<ITalk> {

    @Override
    public ITalk fromString(String input) throws IllegalArgumentException {
        Long id = Long.valueOf(input);
        ITalk talk = new Talk();
        talk.setId(id);
        return talk;
    }

    @Override
    public Class<ITalk> getType() {
        return ITalk.class;
    }
}
