package com.ultimatesoftware.tests.small;

import com.ultimatesoftware.domain.projectAggregate;
import com.ultimatesoftware.domain.commands.UpdateprojectCommand;
import com.ultimatesoftware.domain.events.produced.projectUpdatedEvent;
import com.ultimatesoftware.domain.handlers.projectCommandHandler;
import com.ultimatesoftware.service.resources.projectRequest;
import com.ultimatesoftware.tests.TestData;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SmallFixtureTests extends BaseSmallTests {
  private FixtureConfiguration fixture;

  /**
   * Test setup to run before each test.
   */
  @Before
  public void setup() {
    super.setup();
    fixture = Fixtures.newGivenWhenThenFixture(projectAggregate.class);

    fixture.registerAnnotatedCommandHandler(new projectCommandHandler(fixture.getEventBus(), fixture.getEventStore()));
  }

  @Test
  public void whenUpdateprojectCommandReceived_thenprojectUpdatedEventEmitted() {
    projectRequest request = new projectRequest()
        .setProperty1("property1Updated");

    UpdateprojectCommand updateCommand = new UpdateprojectCommand(TestData.TENANT_ID_1, TestData.PROJECT_ID, request);

    projectUpdatedEvent updatedEvent = new projectUpdatedEvent(updateCommand.getTenantId(), updateCommand.getprojectId())
        .setProperty1(request.getProperty1());

    fixture
        .givenNoPriorActivity()
        .when(updateCommand)
        .expectEvents(updatedEvent);
  }
}
