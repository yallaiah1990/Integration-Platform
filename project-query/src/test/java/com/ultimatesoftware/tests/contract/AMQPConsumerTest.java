package com.ultimatesoftware.tests.contract;

import com.ultimatesoftware.test.helpers.contract.consumer.annotations.ConsumerEventPackage;
import com.ultimatesoftware.test.helpers.contract.consumer.annotations.ConsumerEventPackages;
import com.ultimatesoftware.test.helpers.contract.consumer.annotations.PactConsumer;
import com.ultimatesoftware.test.helpers.contract.consumer.runners.EventConsumerContractRunner;
import com.ultimatesoftware.test.helpers.contract.producer.annotations.PactBrokerNU;
import com.ultimatesoftware.tests.categories.Contract;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;

@RunWith(EventConsumerContractRunner.class)
@ActiveProfiles("contract-test")
@Category(Contract.class)
@PactConsumer("project-query-amqp")
@PactBrokerNU(host = "integrations-pact-broker.apps.mia.ulti.io")
@ConsumerEventPackages({
    @ConsumerEventPackage(provider = "project-cmd-amqp",
        path = "com.ultimatesoftware.domain.events.consumed.projectcmd"),
})
public class AMQPConsumerTest {
}
