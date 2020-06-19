package com.ultimatesoftware.domain.commands;

import static com.ultimatesoftware.domain.projectAggregate.generateAggregateId;

import java.util.StringJoiner;
import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;
import org.hibernate.validator.constraints.NotBlank;

/**
 * A base class that houses common components of all commands.
 */
public abstract class BaseCommand {

  @TargetAggregateIdentifier
  @NotBlank
  private final String aggregateId;

  @NotBlank
  private final String tenantId;

  @NotBlank
  private final String projectId;

  protected BaseCommand(String tenantId, String projectId) {
    this.tenantId = tenantId;
    this.projectId = projectId;
    this.aggregateId = generateAggregateId(tenantId, projectId);
  }

  public String getAggregateId() {
    return aggregateId;
  }

  public String getTenantId() {
    return tenantId;
  }

  public String getprojectId() {
    return projectId;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ")
      .add("'aggregateId': '" + aggregateId + "'")
      .add("'tenantId': '" + tenantId + "'")
      .add("'projectId': '" + projectId + "'")
      .toString();
  }
}
