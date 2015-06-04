package org.jug.montpellier.cartridges.partners.controllers;

import org.apache.felix.ipojo.annotations.Requires;
import org.jug.montpellier.core.api.JugSupport;
import org.jug.montpellier.core.controller.JugController;
import org.montpellierjug.store.jooq.tables.daos.YearpartnerDao;
import org.wisdom.api.annotations.*;
import org.wisdom.api.http.HttpMethod;
import org.wisdom.api.http.Result;
import org.wisdom.api.templates.Template;

/**
 * Created by manland on 04/06/15.
 */
@Controller
@Path("/partners")
public class PartnersController extends JugController {

    @Requires
    YearpartnerDao yearpartnerDao;

    @View("partners")
    Template template;

    public PartnersController(@Requires JugSupport jugSupport) {
        super(jugSupport);
    }

    @Route(method = HttpMethod.GET, uri = "/")
    public Result all(){
        return template(template).render();
    }

    @Route(method = HttpMethod.GET, uri = "/{id}")
    public Result event(@Parameter("id") Long id) {
        return template(template).withParam("partner", yearpartnerDao.findById(id)).render();
    }

}
