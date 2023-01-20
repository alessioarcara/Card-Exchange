package com.aadm.cardexchange;

import com.aadm.cardexchange.client.AuthenticationActivityTest;
import com.aadm.cardexchange.client.CardActivityTest;
import com.aadm.cardexchange.client.HomeActivityTest;
import com.aadm.cardexchange.server.CardSerializerTest;
import com.aadm.cardexchange.server.CardServiceTest;
import com.aadm.cardexchange.server.JSONParserTest;
import com.aadm.cardexchange.server.ListenerImplTest;
import com.aadm.cardexchange.shared.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        CardTest.class,
        CardDecoratorTest.class,
        YuGiOhCardDecoratorTest.class,
        PokemonCardDecoratorTest.class,
        MagicCardDecoratorTest.class,
        CardSerializerTest.class,
        JSONParserTest.class,
        CardServiceTest.class,
        ListenerImplTest.class,
        HomeActivityTest.class,
        CardActivityTest.class,
        AuthenticationActivityTest.class
})
public class CardExchangeSuite {
}
