package com.aadm.cardexchange;

import com.aadm.cardexchange.client.ExampleActivityTest;
import com.aadm.cardexchange.server.ExampleRPCTest;
import com.google.gwt.junit.tools.GWTTestSuite;
import junit.framework.Test;
import junit.framework.TestSuite;

public class CardExchangeSuite extends GWTTestSuite {
  public static Test suite() {
    TestSuite suite = new TestSuite("Tests for MyApp");
    suite.addTestSuite(ExampleActivityTest.class);
    suite.addTestSuite(ExampleRPCTest.class);
    return suite;
  }
}
