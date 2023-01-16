package com.aadm.cardexchange.client.routes;

import com.aadm.cardexchange.client.places.AuthenticationPlace;
import com.aadm.cardexchange.client.places.CardPlace;
import com.aadm.cardexchange.client.places.HomePlace;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

/**
 * PlaceHistoryMapper interface is used to attach all places which the
 * PlaceHistoryHandler should be aware of. This is done via the @WithTokenizers
 * annotation or by extending PlaceHistoryMapperWithFactory and creating a
 * separate TokenizerFactory.
 */
@WithTokenizers({HomePlace.Tokenizer.class, CardPlace.Tokenizer.class, AuthenticationPlace.Tokenizer.class})
public interface AppPlaceHistoryMapper extends PlaceHistoryMapper {
}