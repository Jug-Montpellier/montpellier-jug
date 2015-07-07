package org.jug.montpellier.forms.services;

import java.util.Map;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.jug.montpellier.forms.apis.IntrospectorRegistry;
import org.jug.montpellier.forms.apis.PropertySheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wisdom.api.Controller;
import org.wisdom.api.annotations.View;
import org.wisdom.api.http.Renderable;
import org.wisdom.api.templates.Template;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

/**
 * Created by Eric Taix on 07/03/2015.
 */
@Component
@Provides(specifications = PropertySheet.class)
@Instantiate
public class DefaultPropertySheet implements PropertySheet {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultPropertySheet.class);

    @Requires
    IntrospectorRegistry introspectorRegistry;

    @View("editors/propertysheet")
    Template template;

    public DefaultPropertySheet() {
    }

    @Override
    public Renderable getRenderable(Controller controller, Object object) throws Exception {
       return getRenderable(controller, object, Maps.newHashMap());
    }

    @Override
    public Renderable getRenderable(Controller controller, Object object, Map<String, Object> additionalParameters) throws Exception {
        Preconditions.checkNotNull(object, "PropertySheet getRenderable need a value as parameter");
        if (object != null) {
            Map<String, Object> parameters = Maps.newHashMap(additionalParameters);
            parameters.put("properties", introspectorRegistry.getPropertyValues(object, controller));
            return template.render(controller, parameters);
        }
        return null;
    }
}

