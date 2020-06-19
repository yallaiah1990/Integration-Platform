package com.ultimatesoftware.domain.events.consumed.projectcmd ;

import java.util.StringJoiner;

/**
 * A base class that houses common components of all events.
 */
public abstract class BaseEvent<T extends BaseEvent<T>> {

  private String tenantId;
  private String projectId;

  protected BaseEvent() {
    this.tenantId = "";
    this.projectId = "";
  }

  protected BaseEvent(String tenantId, String projectId) {
    this.tenantId = tenantId;
    this.projectId = projectId;
  }

  public String getTenantId() {
    return tenantId;
  }

  public T setTenantId(String tenantId) {
    this.tenantId = tenantId;
    return (T) this;
  }

  public String getProjectId() {
    return projectId;
  }

  public T setProjectId(String projectId) {
    this.projectId = projectId;
    return (T) this;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ")
      .add("'tenantId': '" + tenantId + "'")
      .add("'projectId': '" + projectId + "'")
      .toString();
  }
}
