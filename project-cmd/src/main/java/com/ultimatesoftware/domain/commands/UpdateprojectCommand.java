package com.ultimatesoftware.domain.commands;

import com.ultimatesoftware.service.resources.projectRequest;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Command used to update a(n) project.
 */
public class UpdateprojectCommand extends BaseCommand {

  @NotBlank
  private String property1;

  /**
   * @param tenantId The ID of the tenant that triggered this command.
   * @param projectId the ID of the project.
   */
  public UpdateprojectCommand(String tenantId, String projectId) {
    super(tenantId, projectId);
  }

  /**
   * @param tenantId The ID of the tenant that triggered this command.
   * @param projectId the ID of the project.
   * @param request The data to be updated.
   */
  public UpdateprojectCommand(String tenantId, String projectId, projectRequest request) {
    super(tenantId, projectId);
    this.property1 = request.getProperty1();
  }

  public String getProperty1() {
    return property1;
  }

  public UpdateprojectCommand setProperty1(String property1) {
    this.property1 = property1;
    return this;
  }
}
