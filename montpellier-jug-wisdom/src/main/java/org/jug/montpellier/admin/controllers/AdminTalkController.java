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
import org.jug.montpellier.core.api.JugSupport;
import org.jug.montpellier.core.controller.JugController;
import org.jug.montpellier.forms.apis.ListView;
import org.jug.montpellier.forms.apis.PropertySheet;
import org.jug.montpellier.models.Event;
import org.jug.montpellier.models.Talk;
import org.montpellierjug.store.jooq.tables.daos.TalkDao;
import org.wisdom.api.annotations.*;
import org.wisdom.api.http.HttpMethod;
import org.wisdom.api.http.Result;
import org.wisdom.api.templates.Template;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@Controller
@Path("/admin/talk")
public class AdminTalkController extends JugController {

    @View("admin")
    Template template;

    @Requires
    PropertySheet propertySheet;

    @Requires
    ListView listView;

    @Requires
    TalkDao talkDao;

    public AdminTalkController(@Requires JugSupport jugSupport) {
        super(jugSupport);
    }

    @Route(method = HttpMethod.GET, uri = "/")
    public Result home() throws Exception {
        List<Talk> talks = Talk.build(talkDao.findAll().stream().sorted((p1, p2) -> p1.getOrderinevent().compareTo(p2.getOrderinevent())));
        return template(template).withListview(listView.getRenderable(this, talks, Talk.class)).render();
    }

    @Route(method = HttpMethod.GET, uri = "/{id}")
    public Result get(@Parameter("id") Long id) throws InvocationTargetException, ClassNotFoundException, IntrospectionException, IllegalAccessException {
        Talk editedTalk = Talk.build(talkDao.findById(id));
        return template(template).withPropertySheet(propertySheet.getRenderable(this, editedTalk)).render();
    }

    @Route(method = HttpMethod.GET, uri="/new/")
    public Result createTalk() throws ClassNotFoundException, IntrospectionException, IllegalAccessException, InvocationTargetException {
        Map<String, Object> additionalParameters = Maps.newHashMap();
        additionalParameters.put("cancelRedirect", "..");
        return template(template).withPropertySheet(propertySheet.getRenderable(this, new Talk(), additionalParameters)).render();
    }

    @Route(method=HttpMethod.POST, uri="/new/")
    public Result saveNewTalk(@Body Talk talk) {
        talkDao.update(talk.into(new org.montpellierjug.store.jooq.tables.pojos.Talk()));
        return redirect("..");
    }

    @Route(method = HttpMethod.POST, uri = "/{id}")
    public Result saveTalk(@Parameter("id") Long id, @Body Talk talk) throws InvocationTargetException, ClassNotFoundException, IntrospectionException, IllegalAccessException {
        talkDao.update(talk.into(new org.montpellierjug.store.jooq.tables.pojos.Talk()));
        return redirect(".");
    }

    @Route(method = HttpMethod.GET, uri="/fruit/")
    public Result test() throws ClassNotFoundException, IntrospectionException, IllegalAccessException, InvocationTargetException {

        return template(template).withPropertySheet(propertySheet.getRenderable(this, new Event())).render();
    }

    @Route(method = HttpMethod.POST, uri="/fruit/")
    public Result testPost(@Body Event test) {

        return redirect(".");
    }

}
