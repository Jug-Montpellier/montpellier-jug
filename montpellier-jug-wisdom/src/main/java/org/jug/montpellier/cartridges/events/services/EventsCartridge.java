package org.jug.montpellier.cartridges.events.services;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.jug.montpellier.cartridges.events.controller.EventsController;
import org.jug.montpellier.core.api.Cartridge;
import org.wisdom.api.router.Router;

@Component
@Provides(specifications = Cartridge.class)
@Instantiate
public class EventsCartridge implements Cartridge {

    @Requires
    private Router router;

    @Override
    public String label() {
        return "Events";
    }

    @Override
    public String routeUri() {
        return router.getReverseRouteFor(EventsController.class, "events");
    }

    @Override
    public int position() {
        return 1;
    }
}
