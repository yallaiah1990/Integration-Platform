package com.ultimatesoftware.domain.entities;

import static com.ultimatesoftware.domain.entities.Project.COLLECTION_NAME;
import static com.ultimatesoftware.domain.entities.Project.FIELD_PROJECT_ID;
import static com.ultimatesoftware.domain.entities.Project.FIELD_TENANT_ID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ultimatesoftware.domain.models.ProjectData;
import com.ultimatesoftware.launch.mongo.fields.SequencedField;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Data model that events are converted into.
 */
@Document(collection = COLLECTION_NAME)
@CompoundIndex(name = "unique_index", def = "{'" + FIELD_TENANT_ID + "': 1 ,'" + FIELD_PROJECT_ID + "':1}", unique = true)
// TODO: Update the unique index based on the primary keys of your upsert query.
public class Project {

  // TODO: Change collection name to hyphen notation.
  public static final String COLLECTION_NAME = "projects";
  public static final String FIELD_TENANT_ID = "tenantId";
  public static final String FIELD_PROJECT_ID = "projectId";
  public static final String FIELD_PROJECT_DATA = "projectData";

  @Indexed
  @Field(FIELD_TENANT_ID)
  private String tenantId;

  @Field(FIELD_PROJECT_ID)
  private String projectId;

  @Field(FIELD_PROJECT_DATA)
  private SequencedField<ProjectData>[] projectData;

  public Project() {
  }

  public Project(String tenantId) {
    this.tenantId = tenantId;
  }

  public String getTenantId() {
    return this.tenantId;
  }

  public String getProjectId() {
    return projectId;
  }

  public void setProjectId(String projectId) {
    this.projectId = projectId;
  }

  public SequencedField<ProjectData>[] getProjectData() {
    return projectData;
  }

  public void setProjectData(SequencedField<ProjectData>[] projectData) {
    this.projectData = projectData;
  }

  @JsonIgnore
  public ProjectData getLatestProjectData() {
    return SequencedField.getLatest(projectData);
  }
}
