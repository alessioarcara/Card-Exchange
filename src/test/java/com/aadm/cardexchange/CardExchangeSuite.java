package com.aadm.cardexchange;

import com.aadm.cardexchange.client.*;
import com.aadm.cardexchange.server.*;
import com.aadm.cardexchange.shared.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        UserTest.class,
        LoginInfoTest.class,
        YuGiOhCardTest.class,
        PokemonCardTest.class,
        MagicCardTest.class,
        PhysicalCardWithEmailTest.class,
        PhysicalCardWithNameTest.class,
        PhysicalCardTest.class,
        DeckTest.class,
        MapDBTest.class,
        GsonSerializerTest.class,
        JSONParserTest.class,
        CardServiceTest.class,
        AuthServiceTest.class,
        DeckServiceTest.class,
        ListenerImplTest.class,
        HomeActivityTest.class,
        CardActivityTest.class,
        AuthActivityTest.class,
        DecksActivityTest.class,
        AuthSubjectTest.class,
        DeckServiceTest.class,
        ProposalTest.class,
        NewExchangePlaceTest.class,
        ExchangeServiceTest.class,
})
public class CardExchangeSuite {
}
