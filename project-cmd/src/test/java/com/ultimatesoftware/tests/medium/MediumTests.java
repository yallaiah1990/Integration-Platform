package com.ultimatesoftware.tests.medium;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.jayway.restassured.response.Response;
import com.ultimatesoftware.domain.events.produced.projectUpdatedEvent;
import com.ultimatesoftware.service.resources.projectRequest;
import java.util.List;
import org.junit.Test;
import org.springframework.http.HttpStatus;

public class MediumTests extends BaseMediumTests {

  @Test
  public void whenUpdateproject_thenExpectprojectUpdatedEventEmitted() {
    projectRequest createRequest = new projectRequest()
        .setProperty1("property1");

    Response createResponse = createproject(createRequest);
    String createdId = createResponse.getBody().jsonPath().get("id");
    String createdProperty1 = createResponse.getBody().jsonPath().get("property1");

    projectRequest updateRequest = new projectRequest()
        .setProperty1("property1updated");

    Response updateResponse = updateproject(updateRequest, createdId);
    String updatedProperty1 = updateResponse.getBody().jsonPath().get("property1");

    updateResponse
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("property1", is(updateRequest.getProperty1()));

    List<projectUpdatedEvent> emittedUpdatedEvents = eventListener.waitForEvents(2, projectUpdatedEvent.class);

    assertThat(emittedUpdatedEvents.get(0).getProperty1(), is(createdProperty1));
    assertThat(emittedUpdatedEvents.get(1).getProperty1(), is(updatedProperty1));
  }

  private Response createproject(projectRequest request) {
    return given()
        .content(request)
        .post("/");
  }

  private Response updateproject(projectRequest request, String projectId) {
    return given()
        .content(request)
        .post("/{projectId}", projectId);
  }
}
