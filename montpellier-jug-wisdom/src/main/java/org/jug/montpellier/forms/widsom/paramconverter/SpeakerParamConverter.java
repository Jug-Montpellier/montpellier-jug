package org.jug.montpellier.forms.widsom.paramconverter;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.jug.montpellier.models.Speaker;
import org.montpellierjug.store.jooq.tables.interfaces.ISpeaker;
import org.wisdom.api.content.ParameterConverter;

/**
 * Created by fteychene on 04/06/2015.
 */
@Component
@Provides(specifications = ParameterConverter.class)
@Instantiate
public class SpeakerParamConverter implements ParameterConverter<ISpeaker> {

    @Override
    public ISpeaker fromString(String input) throws IllegalArgumentException {
        Long id = Long.valueOf(input);
        ISpeaker speaker = new Speaker();
        speaker.setId(id);
        return speaker;
    }

    @Override
    public Class<ISpeaker> getType() {
        return ISpeaker.class;
    }
}
