package org.jug.montpellier.core.controller;

import java.util.HashMap;
import java.util.Map;

import org.jug.montpellier.core.api.JugSupport;
import org.wisdom.api.DefaultController;
import org.wisdom.api.http.Renderable;
import org.wisdom.api.http.Result;
import org.wisdom.api.templates.Template;

public class JugController extends DefaultController {

    public static final String CARTRIDGES = "cartridges";
    public static final String PROPERTY_SHEET = "propertysheet";
    public static final String NEXT_EVENT = "nextEvent";
    public static final String PARTNERS = "partners";


    private final JugSupport jugSupport;

    /**
     * Use this contructor to display a normal page with full menu
     */
    public JugController(JugSupport jugSupport) {
        this.jugSupport = jugSupport;
    }

    public Templatable template(Template template) {
        return new Templatable(template);
    }

    public class Templatable {
        private Map<String, Object> paramsMap = new HashMap<>();
        private Template template;

        public Templatable(Template template) {
            this.template = template;
        }

        public Templatable withPropertySheet(Renderable propertySheetRenderable) {
            paramsMap.put(PROPERTY_SHEET, propertySheetRenderable.content());
            return this;
        }

        public Templatable withListview(Renderable listViewtRenderable) {
            paramsMap.put(PROPERTY_SHEET, listViewtRenderable.content());
            return this;
        }
        public Result render() {
            paramsMap.put(CARTRIDGES, jugSupport.cartridges());

            if (jugSupport != null) {
                paramsMap.put(NEXT_EVENT, jugSupport.getNextEvent());
            }

            if (jugSupport != null) {
                paramsMap.put(PARTNERS, jugSupport.getPartners());
            }
            return JugController.ok(JugController.this.render(template, paramsMap));
        }

        public Templatable withParam(String paramKey, Object paramValue) {
            paramsMap.put(paramKey, paramValue);
            return this;
        }
    }
}
