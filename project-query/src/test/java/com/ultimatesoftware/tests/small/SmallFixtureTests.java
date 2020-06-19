package com.ultimatesoftware.tests.small;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.ultimatesoftware.domain.entities.Project;
import com.ultimatesoftware.domain.events.consumed.projectcmd.ProjectUpdatedEvent;
import com.ultimatesoftware.tests.TestData;
import com.ultimatesoftware.tests.small.config.MongoConfig;
import com.ultimatesoftware.tests.small.config.TestConfig;
import org.axonframework.domain.GenericDomainEventMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class, MongoConfig.class})
public class SmallFixtureTests extends BaseSmallTests {

  /**
   * Test setup.
   */
  @Before
  public void setup() {
    super.setup();
  }

  @Test
  public void givenProjectDoesNotExist_whenProjectUpdatedEventReceived_thenCreateProject() {
    ProjectUpdatedEvent updateEvent = new ProjectUpdatedEvent(TestData.TENANT_ID_1, TestData.PROJECT_ID)
        .setProperty1("property1");

    eventBus.publish(new GenericDomainEventMessage<>(null, 0, updateEvent));

    Project record = repository.findByTenantIdAndProjectId(updateEvent.getTenantId(), updateEvent.getProjectId());

    assertThat(record, is(notNullValue()));
    assertThat(record.getLatestProjectData().getProperty1(), is(updateEvent.getProperty1()));
  }

  @Test
  public void givenProjectExists_whenProjectUpdatedEventReceived_thenUpdateProject() {
    ProjectUpdatedEvent updateEvent = new ProjectUpdatedEvent(TestData.TENANT_ID_1, TestData.PROJECT_ID)
        .setProperty1("property1");

    eventBus.publish(new GenericDomainEventMessage<>(null, 0, updateEvent));
    // Make sure the original event was actually processed.
    Project record = repository.findByTenantIdAndProjectId(updateEvent.getTenantId(), updateEvent.getProjectId());
    assertThat(record, is(notNullValue()));

    updateEvent.setProperty1("updated_property1");
    eventBus.publish(new GenericDomainEventMessage<>(null, 1, updateEvent));
    // Test if the record was removed
    Project updatedRecord = repository.findByTenantIdAndProjectId(updateEvent.getTenantId(), updateEvent.getProjectId());
    assertThat(updatedRecord.getLatestProjectData().getProperty1(), is(updateEvent.getProperty1()));
  }
}
