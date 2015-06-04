package org.jug.montpellier.cartridges.members.controllers;

import org.apache.felix.ipojo.annotations.Requires;
import org.jug.montpellier.core.api.JugSupport;
import org.jug.montpellier.core.controller.JugController;
import org.montpellierjug.store.SpeakerDaoCustom;
import org.wisdom.api.annotations.Controller;
import org.wisdom.api.annotations.Path;
import org.wisdom.api.annotations.Route;
import org.wisdom.api.annotations.View;
import org.wisdom.api.http.HttpMethod;
import org.wisdom.api.http.Result;
import org.wisdom.api.templates.Template;

/**
 * Created by chelebithil on 06/03/15.
 */
@Controller
@Path("/members")
public class MembersController extends JugController{

    @Requires
    SpeakerDaoCustom speakerDao;

    @View("members")
    Template template;

    /**
     * Use this contructor to display a normal page with full menu
     *
     * @param jugSupport
     */
    public MembersController(@Requires JugSupport jugSupport) {
        super(jugSupport);
    }

    @Route(method = HttpMethod.GET, uri = "/")
    public Result all(){
        return template(template).withParam("members", speakerDao.findAllMembers()).render();
    }

}
