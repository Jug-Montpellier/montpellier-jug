
package org.jug.montpellier.admin.controllers;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.apache.felix.ipojo.annotations.Requires;
import org.jooq.DSLContext;
import org.jooq.SelectOrderByStep;
import org.jooq.SelectWhereStep;
import org.jug.montpellier.core.api.JugSupport;
import org.jug.montpellier.core.controller.JugController;
import org.jug.montpellier.forms.apis.ListView;
import org.jug.montpellier.forms.apis.PropertySheet;
import org.jug.montpellier.models.Event;
import org.montpellierjug.store.jooq.Tables;
import org.montpellierjug.store.jooq.tables.daos.EventDao;
import org.wisdom.api.annotations.Body;
import org.wisdom.api.annotations.Controller;
import org.wisdom.api.annotations.Parameter;
import org.wisdom.api.annotations.Path;
import org.wisdom.api.annotations.QueryParameter;
import org.wisdom.api.annotations.Route;
import org.wisdom.api.annotations.View;
import org.wisdom.api.http.HttpMethod;
import org.wisdom.api.http.Result;
import org.wisdom.api.security.Authenticated;
import org.wisdom.api.templates.Template;
import org.wisdom.oauth2.OAuth2WisdomAuthenticator;
import org.wisdom.oauth2.controller.Role;

import com.google.common.collect.Maps;

/**
 * Created by fteychene on 04/06/2015.
 */
@Controller
@Path("/admin/event")
@Authenticated(OAuth2WisdomAuthenticator.NAME)
public class AdminEventController extends JugController {

    @View("admin")
    Template template;

    @Requires
    PropertySheet propertySheet;

    @Requires
    ListView listView;

    @Requires
    EventDao eventDao;

    @Requires
    DSLContext dslContext;

    public AdminEventController(@Requires JugSupport jugSupport) {
        super(jugSupport);
    }

    @Role("admin")
    @Route(method = HttpMethod.GET, uri = "/")
    public Result home(@QueryParameter("search") String search) throws Exception {
        SelectOrderByStep selectStep = dslContext.selectFrom(Tables.EVENT);
        if (search != null && !search.isEmpty()) {
            selectStep = ((SelectWhereStep)selectStep).where(org.montpellierjug.store.jooq.tables.Event.EVENT.TITLE.likeIgnoreCase("%" + search + "%"));
        }
        List<Event> events = selectStep.orderBy(org.montpellierjug.store.jooq.tables.Event.EVENT.DATE.desc()).fetchInto(Event.class);
        return template(template).withListview(listView.getRenderable(this, events, Event.class)).render();
    }

    @Role("admin")
    @Route(method = HttpMethod.GET, uri = "/{id}")
    public Result get(@Parameter("id") Long id) throws Exception {
        Event editedTalk = Event.build(eventDao.findById(id));
        return template(template).withPropertySheet(propertySheet.getRenderable(this, editedTalk)).render();
    }

    @Role("admin")
    @Route(method = HttpMethod.POST, uri = "/{id}")
    public Result saveTalk(@Parameter("id") Long id, @Body Event event) throws InvocationTargetException, ClassNotFoundException, IntrospectionException, IllegalAccessException {
        eventDao.update(event.into(new org.montpellierjug.store.jooq.tables.pojos.Event()));
        return redirect(".");
    }

    @Role("admin")
    @Route(method = HttpMethod.GET, uri = "/new/")
    public Result createTalk() throws Exception {
        Map<String, Object> additionalParameters = Maps.newHashMap();
        additionalParameters.put("cancelRedirect", "..");
        return template(template).withPropertySheet(propertySheet.getRenderable(this, new Event(), additionalParameters)).render();
    }

    @Role("admin")
    @Route(method = HttpMethod.POST, uri = "/new/")
    public Result saveNewTalk(@Body Event event) {
        eventDao.update(event.into(new org.montpellierjug.store.jooq.tables.pojos.Event()));
        return redirect("..");
    }

}
