package org.jug.montpellier.core.api;

import java.util.List;

import org.montpellierjug.store.jooq.tables.pojos.Yearpartner;

/**
 * Created by manland on 16/03/15.
 */
public interface PartnerSupport {

    public List<Yearpartner> getPartners();

}
