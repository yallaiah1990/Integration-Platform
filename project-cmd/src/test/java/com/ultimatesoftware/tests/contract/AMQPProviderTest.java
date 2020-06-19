package com.ultimatesoftware.tests.contract;

import au.com.dius.pact.provider.junit.Provider;
import com.ultimatesoftware.test.helpers.contract.producer.annotations.PactBrokerNU;
import com.ultimatesoftware.test.helpers.contract.producer.annotations.ProviderEventPackage;
import com.ultimatesoftware.test.helpers.contract.producer.runners.PactAmqpRunner;
import com.ultimatesoftware.tests.categories.Contract;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;

@RunWith(PactAmqpRunner.class)
@ActiveProfiles("contract-test")
@Category(Contract.class)
@PactBrokerNU(host = "integrations-pact-broker.apps.mia.ulti.io")
@Provider("project-cmd-amqp")
@ProviderEventPackage("com.ultimatesoftware.domain.events.produced")
public class AMQPProviderTest {
}
