transactions
============

This example demonstrates transactional scope, rollback error handling strategy and mapping XML to Object.

Step 1: import the project
Step 2: start activeMQ server
Step 3: run import.sql packaged in the scr/main/app in the mysql workbench
Step 4: run mule app
Step 5: go to http://localhost:8161/admin/send.jsp and send to in queue a with message body:

<order>
	<itemId>1</itemId>
	<itemUnits>2</itemUnits>
	<customerId>1</customerId>
</order>

Step 6: go to the console and see a log:

"Message with id "ID:Lenovo-PC-49554-1404281777345-3:1:1:1:65" has been redelivered 3 times on endpoint "jms://in", which exceeds the maxRedelivery setting of 2 on the connector "Active_MQ". Message payload is of type: ActiveMQTextMessage (org.mule.transport.jms.redelivery.MessageRedeliveredException)
The error occurs after SQL insert but if you look at the content of orders table in your DB using MySQL workbench you see that no record was inserted. this is because DB and JMS operations are in the transactional scope.

