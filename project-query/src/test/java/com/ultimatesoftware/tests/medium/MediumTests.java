package com.ultimatesoftware.tests.medium;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import com.ultimatesoftware.domain.events.consumed.projectcmd.ProjectUpdatedEvent;
import com.ultimatesoftware.tests.TestData;
import org.junit.Test;
import org.springframework.http.HttpStatus;

public class MediumTests extends BaseMediumTests {

  @Test
  public void givenProjectUpdatedEvent_whenGetProject_thenReturnProject() {
    ProjectUpdatedEvent updateEvent = new ProjectUpdatedEvent(TestData.TENANT_ID_1, TestData.PROJECT_ID)
        .setProperty1("property1");

    eventBus.publishAndWait(updateEvent);

    given()
        .get("/{projectId}", updateEvent.getProjectId())
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("property1", is("property1"));
  }

  @Test
  public void givenProjectUpdatedEvent_whenGetProjects_thenReturnProjects() {
    ProjectUpdatedEvent updateEvent = new ProjectUpdatedEvent(TestData.TENANT_ID_1, TestData.PROJECT_ID)
        .setProperty1("property1");

    eventBus.publishAndWait(updateEvent);

    given()
        .get()
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("content[0].projectId", is(TestData.PROJECT_ID))
        .body("content[0].property1", is("property1"));
  }
}
