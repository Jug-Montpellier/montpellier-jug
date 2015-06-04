/*
 * #%L
 * Wisdom-Framework
 * %%
 * Copyright (C) 2013 - 2014 Wisdom Framework
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package org.jug.montpellier.admin.controllers;

import com.google.common.collect.Maps;
import org.apache.felix.ipojo.annotations.Requires;
import org.jooq.DSLContext;
import org.jooq.SelectOrderByStep;
import org.jooq.SelectWhereStep;
import org.jug.montpellier.core.api.JugSupport;
import org.jug.montpellier.core.controller.JugController;
import org.jug.montpellier.forms.apis.ListView;
import org.jug.montpellier.forms.apis.PropertySheet;
import org.jug.montpellier.models.Talk;
import org.montpellierjug.store.jooq.Tables;
import org.montpellierjug.store.jooq.tables.daos.TalkDao;
import org.wisdom.api.annotations.*;
import org.wisdom.api.http.HttpMethod;
import org.wisdom.api.http.Result;
import org.wisdom.api.security.Authenticated;
import org.wisdom.api.templates.Template;
import org.wisdom.oauth2.OAuth2WisdomAuthenticator;
import org.wisdom.oauth2.controller.Role;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@Controller
@Path("/admin/talk")
@Authenticated(OAuth2WisdomAuthenticator.NAME)
public class AdminTalkController extends JugController {

    @View("admin")
    Template template;

    @Requires
    PropertySheet propertySheet;

    @Requires
    ListView listView;

    @Requires
    TalkDao talkDao;

    @Requires
    DSLContext dslContext;

    public AdminTalkController(@Requires JugSupport jugSupport) {
        super(jugSupport);
    }

    @Role("admin")
    @Route(method = HttpMethod.GET, uri = "/")
    public Result home(@QueryParameter("search") String search) throws Exception {
        SelectOrderByStep selectStep = dslContext.selectFrom(Tables.TALK);
        if (search != null && !search.isEmpty()) {
            selectStep = ((SelectWhereStep)selectStep).where(org.montpellierjug.store.jooq.tables.Talk.TALK.TITLE.likeIgnoreCase("%" + search + "%"));
        }
        List<Talk> talks = selectStep.orderBy(org.montpellierjug.store.jooq.tables.Talk.TALK.EVENT_ID.desc(),
                org.montpellierjug.store.jooq.tables.Talk.TALK.ORDERINEVENT.asc()).fetchInto(Talk.class);
        return template(template).withListview(listView.getRenderable(this, talks, Talk.class)).render();
    }

    @Role("admin")
    @Route(method = HttpMethod.GET, uri = "/{id}")
    public Result get(@Parameter("id") Long id) throws InvocationTargetException, ClassNotFoundException, IntrospectionException, IllegalAccessException {
        Talk editedTalk = Talk.build(talkDao.findById(id));
        return template(template).withPropertySheet(propertySheet.getRenderable(this, editedTalk)).render();
    }

    @Role("admin")
    @Route(method = HttpMethod.POST, uri = "/{id}")
    public Result saveTalk(@Parameter("id") Long id, @Body Talk talk) throws InvocationTargetException, ClassNotFoundException, IntrospectionException, IllegalAccessException {
        talkDao.update(talk.into(new org.montpellierjug.store.jooq.tables.pojos.Talk()));
        return redirect(".");
    }

    @Role("admin")
    @Route(method = HttpMethod.GET, uri = "/new/")
    public Result createTalk() throws ClassNotFoundException, IntrospectionException, IllegalAccessException, InvocationTargetException {
        Map<String, Object> additionalParameters = Maps.newHashMap();
        additionalParameters.put("cancelRedirect", "..");
        return template(template).withPropertySheet(propertySheet.getRenderable(this, new Talk(), additionalParameters)).render();
    }

    @Role("admin")
    @Route(method = HttpMethod.POST, uri = "/new/")
    public Result saveNewTalk(@Body Talk talk) {
        talkDao.update(talk.into(new org.montpellierjug.store.jooq.tables.pojos.Talk()));
        return redirect("..");
    }

}
