package org.jug.montpellier.cartridges.news.services;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.jug.montpellier.cartridges.news.controllers.NewsController;
import org.jug.montpellier.core.api.Cartridge;
import org.wisdom.api.router.Router;

@Component
@Provides(specifications = Cartridge.class)
@Instantiate
public class NewsCartridge implements Cartridge {

    @Requires
    private Router router;

    @Override
    public String label() {
        return "News";
    }

    @Override
    public String routeUri() {
        return router.getReverseRouteFor(NewsController.class, "news");
    }

    @Override
    public int position() {
        return 2;
    }
}
