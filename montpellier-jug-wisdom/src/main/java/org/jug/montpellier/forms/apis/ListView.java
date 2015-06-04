package org.jug.montpellier.forms.apis;

import org.wisdom.api.Controller;
import org.wisdom.api.http.Renderable;

import java.util.List;
import java.util.Map;

/**
 * Created by Eric Taix on 24/03/15.
 */
public interface ListView {

    public <T extends Object> Renderable getRenderable(Controller controller, List<T> object, Class<T> objectClass) throws Exception;

    public <T extends Object> Renderable getRenderable(Controller controller, List<T> object, Class<T> objectClass, Map<String, Object> additionnalParameters) throws Exception;

}
