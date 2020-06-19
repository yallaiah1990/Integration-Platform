package com.ultimatesoftware.domain.handlers;

import com.ultimatesoftware.domain.entities.Project;
import com.ultimatesoftware.domain.events.consumed.projectcmd.ProjectUpdatedEvent;
import com.ultimatesoftware.domain.models.ProjectData;
import com.ultimatesoftware.launch.mongo.utils.RetryableMongoService;
import com.ultimatesoftware.launch.mongo.utils.UpdateBuilder;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventhandling.annotation.SequenceNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

/**
 * Handler for project-cmd events.
 */
@Component
public class ProjectCmdEventHandler {

  private RetryableMongoService retryableMongoService;

  @Autowired
  public ProjectCmdEventHandler(RetryableMongoService retryableMongoService) {
    this.retryableMongoService = retryableMongoService;
  }

  /**
   * @param event The event that updates a(n) Project.
   */
  @EventHandler
  public void on(ProjectUpdatedEvent event, @SequenceNumber long sequenceNumber) {
    Query query = new Query(Criteria.where(Project.FIELD_TENANT_ID).is(event.getTenantId())
        .and(Project.FIELD_PROJECT_ID).is(event.getProjectId()));

    ProjectData projectData = new ProjectData()
        .setProperty1(event.getProperty1());

    Update update = new UpdateBuilder()
        .pushSortBySequenceNumberAndSliceOne(Project.FIELD_PROJECT_DATA, projectData, sequenceNumber);

    retryableMongoService.upsert(query, update, Project.COLLECTION_NAME);
  }
}
