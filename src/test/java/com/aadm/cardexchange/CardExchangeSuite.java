package com.aadm.cardexchange;

import com.aadm.cardexchange.client.ExampleActivityTest;
import com.aadm.cardexchange.server.ExampleRPCTest;
import com.aadm.cardexchange.server.ImportJSONTest;
import com.aadm.cardexchange.server.ParseJSONStringTest;
import com.aadm.cardexchange.shared.CardTest;
import com.aadm.cardexchange.shared.MagicCardDecoratorTest;
import com.aadm.cardexchange.shared.PokemonCardDecoratorTest;
import com.aadm.cardexchange.shared.YuGiOhCardDecoratorTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        CardTest.class,
        YuGiOhCardDecoratorTest.class,
        PokemonCardDecoratorTest.class,
        MagicCardDecoratorTest.class,
        ParseJSONStringTest.class,
        ImportJSONTest.class,
        ExampleActivityTest.class,
        ExampleRPCTest.class
})
public class CardExchangeSuite {
}
