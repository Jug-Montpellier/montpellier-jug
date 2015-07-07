package org.jug.montpellier.cartridges.home.services;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.jug.montpellier.cartridges.home.controllers.HomeController;
import org.jug.montpellier.core.api.Cartridge;
import org.wisdom.api.router.Router;

@Component
@Provides(specifications = Cartridge.class)
@Instantiate
public class HomeCartridge implements Cartridge {

    @Requires
    private Router router;

    @Override
    public String label() {
        return "Home";
    }

    @Override
    public String routeUri() {
        return router.getReverseRouteFor(HomeController.class, "welcome");
    }

    @Override
    public int position() {
        return 0;
    }
}
