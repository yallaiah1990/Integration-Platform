package com.ultimatesoftware.domain.events.produced;

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

  /**
   * Creates the event and assigns primary keys.
   * Within the service, this constructor should be used to instantiate events.
   * The setters for primary key fields should only be used for test cases.
   */
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

  public String getprojectId() {
    return projectId;
  }

  public T setprojectId(String projectId) {
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
