package com.ultimatesoftware.domain.events.consumed.projectcmd;

import com.ultimatesoftware.axon.serialization.Event;
import java.util.StringJoiner;

/**
 * Represents a updated Project event.
 */
@Event(name = "integrations.project.updated")
public class ProjectUpdatedEvent extends BaseEvent<ProjectUpdatedEvent> {

  private String property1;

  /**
   * No-argument constructor for deserialization.
   */
  private ProjectUpdatedEvent() {
    super("", "");
  }

  /**
   * @param tenantId The ID of the tenant generating this event.
   * @param projectId The ID of the project.
   */
  public ProjectUpdatedEvent(String tenantId, String projectId) {
    super(tenantId, projectId);
  }

  /**
   * @return Property.
   */
  public String getProperty1() {
    return property1;
  }

  public ProjectUpdatedEvent setProperty1(String property1) {
    this.property1 = property1;
    return this;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", "'" + ProjectUpdatedEvent.class.getSimpleName() + "': {", "}")
        .add(super.toString())
        .add("'property1': '" + property1 + "'")
        .toString();
  }
}
