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
import org.jug.montpellier.models.News;
import org.jug.montpellier.models.Speaker;
import org.jug.montpellier.models.Yearpartner;
import org.montpellierjug.store.jooq.Tables;
import org.montpellierjug.store.jooq.tables.daos.YearpartnerDao;
import org.wisdom.api.annotations.*;
import org.wisdom.api.http.HttpMethod;
import org.wisdom.api.http.Result;
import org.wisdom.api.templates.Template;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@Controller
@Path("/admin/partner")
public class AdminPartnerController extends JugController {

    @View("admin")
    Template template;

    @Requires
    PropertySheet propertySheet;
    @Requires
    ListView listView;

    @Requires
    YearpartnerDao yearpartnerDao;
    @Requires
    DSLContext dslContext;

    public AdminPartnerController(@Requires JugSupport jugSupport) {
        super(jugSupport);
    }

    @Route(method = HttpMethod.GET, uri = "/")
    public Result all(@QueryParameter("search") String search) throws Exception {
        SelectOrderByStep selectStep = dslContext.selectFrom(Tables.YEARPARTNER);
        if (search != null && !search.isEmpty()) {
            selectStep = ((SelectWhereStep)selectStep).where(org.montpellierjug.store.jooq.tables.Yearpartner.YEARPARTNER.NAME.likeIgnoreCase("%" + search + "%"));
        }
        List<Yearpartner> yearpartners = selectStep.orderBy(org.montpellierjug.store.jooq.tables.Yearpartner.YEARPARTNER.NAME.asc()).fetchInto(Yearpartner.class);
        return template(template).withListview(listView.getRenderable(this, yearpartners, Yearpartner.class)).withParam("search", search).render();
    }

    @Route(method = HttpMethod.GET, uri = "/{id}")
    public Result partner(@Parameter("id") Long id) throws InvocationTargetException, ClassNotFoundException, IntrospectionException, IllegalAccessException {
        Yearpartner yearpartner = Yearpartner.build(yearpartnerDao.findById(id));
        return template(template).withPropertySheet(propertySheet.getRenderable(this, yearpartner)).render();
    }

    @Route(method = HttpMethod.POST, uri = "/{id}")
    public Result savePartner(@Parameter("id") Long id, @Body Yearpartner yearpartner) throws InvocationTargetException, ClassNotFoundException, IntrospectionException, IllegalAccessException {
        yearpartnerDao.update(yearpartner.into(new org.montpellierjug.store.jooq.tables.pojos.Yearpartner()));
        return redirect("/admin/partner/" + id);
    }

    @Route(method = HttpMethod.GET, uri = "/new/")
    public Result createPartner() throws ClassNotFoundException, IntrospectionException, IllegalAccessException, InvocationTargetException {
        Map<String, Object> additionalParameters = Maps.newHashMap();
        additionalParameters.put("cancelRedirect", "..");
        return template(template).withPropertySheet(propertySheet.getRenderable(this, new Yearpartner(), additionalParameters)).render();
    }

    @Route(method = HttpMethod.POST, uri = "/new/")
    public Result saveNewPartner(@Body Yearpartner yearpartner) {
        yearpartnerDao.insert(yearpartner.into(new org.montpellierjug.store.jooq.tables.pojos.Yearpartner()));
        return redirect("..");
    }

}
