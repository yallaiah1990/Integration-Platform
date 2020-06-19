package com.ultimatesoftware.domain;

import com.ultimatesoftware.domain.commands.BaseCommand;
import com.ultimatesoftware.domain.events.produced.BaseEvent;
import com.ultimatesoftware.domain.events.produced.projectUpdatedEvent;
import org.axonframework.domain.DomainEventMessage;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Responsible for handling any command related to project.
 */
public class projectAggregate extends AbstractAnnotatedAggregateRoot {

  private static final Logger LOGGER = LoggerFactory.getLogger(projectAggregate.class);

  public static String generateAggregateId(String tenantId, String projectId) {
    return String.format("%s_%s", tenantId, projectId);
  }

  @AggregateIdentifier
  private String aggregateId;

  private String tenantId;

  private String projectId;

  /**
   * No-argument constructor for deserialization.
   */
  private projectAggregate() {
  }

  /**
   * Create instance of project.
   */
  public projectAggregate(BaseCommand command) {
    this.aggregateId = command.getAggregateId();
    this.tenantId = command.getTenantId();
    this.projectId = command.getprojectId();
  }

  public void update(projectUpdatedEvent event) {
    this.apply(event);
  }

  /**
   * Stores the initial state of the aggregate if it is not initialized yet.
   */
  @Override
  protected void handle(DomainEventMessage message) {
    if (aggregateId == null) {
      aggregateId = (String) message.getAggregateIdentifier();
      BaseEvent event = (BaseEvent) message.getPayload();
      tenantId = event.getTenantId();
      projectId = event.getprojectId();
    }
    super.handle(message);
  }

  @Override
  protected void apply(Object eventPayload) {
    super.apply(eventPayload);
    LOGGER.info("Emitted event: {}.", eventPayload);
  }
}
