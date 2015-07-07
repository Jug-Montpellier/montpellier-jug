package org.jug.montpellier.forms.apis;

import java.util.Map;

import org.wisdom.api.Controller;
import org.wisdom.api.http.Renderable;

/**
 * Created by Eric Taix on 07/03/2015.
 */
public interface PropertySheet {

    public Renderable getRenderable(Controller controller, Object object) throws Exception;

    public Renderable getRenderable(Controller controller, Object object, Map<String, Object> additionalParameters) throws Exception;

}
