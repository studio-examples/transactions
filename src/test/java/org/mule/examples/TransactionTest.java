package org.mule.examples;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.MessageExchangePattern;
import org.mule.api.MuleEvent;
import org.mule.module.client.MuleClient;
import org.mule.processor.chain.SubflowInterceptingChainLifecycleWrapper;
import org.mule.tck.junit4.FunctionalTestCase;

public class TransactionTest extends FunctionalTestCase
{
	private static final String MESSAGE = "<order>\n\t<itemId>1</itemId>\n\t<itemUnits>2</itemUnits>\n\t<customerId>1</customerId>\n</order>\n";
	private static final String USER = "moj.testik123@gmail.com";
	private static final String USER_ENC = "moj.testik123%40gmail.com";
	private static final String PASSWORD = "MrHotovo001";
	private static final String HOST = "smtp.gmail.com";
	private static final String PORT = "587";
	private static MySQLDbCreator DB = null;

	@Override
    protected String getConfigResources()
    {
        return "transactions.xml,flows/test-flows.xml";
    }

    @Test
    public void sendJMStoTransactionScopeTest() throws Exception
    {
        MuleClient client = new MuleClient(muleContext);
        client.send("jms://in", MESSAGE, null);
        SubflowInterceptingChainLifecycleWrapper subflow = getSubFlow("selectOrders");
        subflow.initialise();
        MuleEvent response = subflow.process(getTestEvent(null, MessageExchangePattern.REQUEST_RESPONSE));
        assertEquals("[{count(*)=1}]", response.getMessage().getPayloadAsString());
    }

    @BeforeClass
    public static void prepareTest() throws Exception {
      	System.setProperty("smtp.host", HOST);
       	System.setProperty("smtp.password", PASSWORD);
       	System.setProperty("smtp.user", USER_ENC);
       	System.setProperty("smtp.port", PORT);
    	System.setProperty("mail.to", USER);
    	System.setProperty("mail.from", USER);
    	System.setProperty("mail.subject", "Mule flow completed!");
    	
    	DB = new MySQLDbCreator("company", "./src/test/resources/mule.test.properties");
    	DB.setUpDatabase();

    }
    
    @AfterClass
    public static void tearDown(){    	
    	DB.tearDownDataBase();
    }
    
    

}
