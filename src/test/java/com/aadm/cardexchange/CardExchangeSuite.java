package com.aadm.cardexchange;

import com.aadm.cardexchange.client.AuthActivityTest;
import com.aadm.cardexchange.client.AuthSubjectTest;
import com.aadm.cardexchange.client.CardActivityTest;
import com.aadm.cardexchange.client.HomeActivityTest;
import com.aadm.cardexchange.server.*;
import com.aadm.cardexchange.shared.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        CardTest.class,
        UserTest.class,
        LoginInfoTest.class,
        CardDecoratorTest.class,
        YuGiOhCardDecoratorTest.class,
        PokemonCardDecoratorTest.class,
        MagicCardDecoratorTest.class,
        PhysicalCardDecoratorTest.class,
        PhysicalCardImplTest.class,
        DeckTest.class,
        GsonSerializerTest.class,
        JSONParserTest.class,
        CardServiceTest.class,
        AuthServiceTest.class,
        ListenerImplTest.class,
        HomeActivityTest.class,
        CardActivityTest.class,
        AuthActivityTest.class,
        AuthSubjectTest.class,
        DeckServiceTest.class,
})
public class CardExchangeSuite {
}
