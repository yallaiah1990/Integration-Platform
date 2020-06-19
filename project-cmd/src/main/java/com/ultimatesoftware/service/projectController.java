package com.ultimatesoftware.service;

import static com.ultimatesoftware.service.projectController.BASE_ENDPOINT;

import com.ultimatesoftware.domain.commands.UpdateprojectCommand;
import com.ultimatesoftware.service.resources.projectRequest;
import com.ultimatesoftware.service.resources.projectResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * The API that this artifact exposes.
 */
@RestController
@RequestMapping(value = BASE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
public class projectController {

  public static final String TENANT_HEADER = "x-tenant";
  /** Prefix for all endpoints in this service. */
  public static final String BASE_ENDPOINT = "/v1/projects";

  @Autowired
  private CommandGateway commandGateway;

  /**
   * Updates a(n) project.
   * @param request The body of the request.
   * @return The HTTP response with the ID of the created project.
   */
  @ApiOperation(value = "Create or update an existing project.", response = projectResponse.class)
  @ApiResponses(@ApiResponse(code = 400, message = "Invalid project object provided."))
  @PostMapping({"/", "/{projectId}"})
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity update(
      @RequestHeader(TENANT_HEADER) String tenantId,
      @Valid @ApiParam("project ID") @PathVariable("projectId") Optional<String> projectId,
      @Valid @ApiParam(value = "project object", required = true) @RequestBody projectRequest request) {
    UpdateprojectCommand command = new UpdateprojectCommand(tenantId, projectId.orElse(UUID.randomUUID().toString()), request);
    commandGateway.sendAndWait(command);
    projectResponse response = new projectResponse(command.getprojectId())
        .setProperty1(command.getProperty1());

    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
