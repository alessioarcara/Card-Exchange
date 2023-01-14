package com.aadm.cardexchange;

import com.aadm.cardexchange.client.HomeActivityTest;
import com.aadm.cardexchange.server.CardSerializerTest;
import com.aadm.cardexchange.server.CardServiceTest;
import com.aadm.cardexchange.server.DBInitializeTest;
import com.aadm.cardexchange.server.JSONParserTest;
import com.aadm.cardexchange.shared.CardTest;
import com.aadm.cardexchange.shared.MagicCardDecoratorTest;
import com.aadm.cardexchange.shared.PokemonCardDecoratorTest;
import com.aadm.cardexchange.shared.YuGiOhCardDecoratorTest;
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
        DBInitializeTest.class,
        CardServiceTest.class,
        HomeActivityTest.class,
})
public class CardExchangeSuite {
}
