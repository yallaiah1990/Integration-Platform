package com.ultimatesoftware.domain.events.produced;

import com.ultimatesoftware.axon.serialization.Event;
import com.ultimatesoftware.axon.spring.boot.amqp.SuppressTopicBinding;
import java.util.StringJoiner;

/**
 * Represents a updated project.
 */
@Event(name = "integrations.project.updated")
@SuppressTopicBinding
public class projectUpdatedEvent extends BaseEvent<projectUpdatedEvent> {

  private String property1;

  /**
   * No-argument constructor for deserialization.
   */
  private projectUpdatedEvent() {
    super("", "");
  }

  /**
   * @param tenantId The ID of the tenant generating this event.
   */
  public projectUpdatedEvent(String tenantId, String projectId) {
    super(tenantId, projectId);
  }

  /**
   * @return Really, really beautiful property of the project that was updated.
   */
  public String getProperty1() {
    return property1;
  }

  public projectUpdatedEvent setProperty1(String property1) {
    this.property1 = property1;
    return this;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", "'" + projectUpdatedEvent.class.getSimpleName() + "': {", "}")
        .add(super.toString())
        .add("'property1': '" + property1 + "'")
        .toString();
  }
}
