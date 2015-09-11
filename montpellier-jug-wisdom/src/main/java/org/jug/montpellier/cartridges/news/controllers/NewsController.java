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
package org.jug.montpellier.cartridges.news.controllers;

import java.util.Arrays;
import java.util.List;

import org.apache.felix.ipojo.annotations.Requires;
import org.jug.montpellier.core.api.JugSupport;
import org.jug.montpellier.core.controller.JugController;
import org.jug.montpellier.models.News;
import org.wisdom.api.annotations.Controller;
import org.wisdom.api.annotations.Path;
import org.wisdom.api.annotations.Route;
import org.wisdom.api.annotations.View;
import org.wisdom.api.http.HttpMethod;
import org.wisdom.api.http.Result;
import org.wisdom.api.templates.Template;

@Controller
@Path("/news")
public class NewsController extends JugController {


    @View("news")
    Template template;

    public NewsController(@Requires JugSupport jugSupport) {
        super(jugSupport);
    }

    List<News> buildNews() {
        return Arrays.asList(
            new News("Nouveau site", "Trop trop bien ;)")
        );
    }
    
    @Route(method = HttpMethod.GET, uri = "/")
    public Result news() {
        return template(template).withParam("news", buildNews()).render();
    }

}
