package com.ultimatesoftware.service.resources;

import com.ultimatesoftware.domain.entities.Project;
import io.swagger.annotations.ApiModelProperty;

/**
 * The API representation of a(n) Project.
 */
public class ProjectResponse {

  @ApiModelProperty(value = "Project ID", required = true)
  private String projectId;

  @ApiModelProperty(value = "Property 1", required = true)
  private String property1;

  public ProjectResponse(Project project) {
    this.projectId = project.getProjectId();
    this.property1 = project.getLatestProjectData().getProperty1();
  }

  public String getProjectId() {
    return projectId;
  }

  public ProjectResponse setProjectId(String id) {
    this.projectId = id;
    return this;
  }

  public String getProperty1() {
    return property1;
  }

  public ProjectResponse setProperty1(String property1) {
    this.property1 = property1;
    return this;
  }
}
