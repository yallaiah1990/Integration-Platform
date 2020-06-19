package com.ultimatesoftware.service;

import static com.ultimatesoftware.service.ProjectController.BASE_ENDPOINT;

import com.ultimatesoftware.domain.ProjectRepository;
import com.ultimatesoftware.domain.entities.Project;
import com.ultimatesoftware.service.resources.ProjectResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.ArrayList;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * The API that this artifact exposes.
 */
@RestController
@RequestMapping(value = BASE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProjectController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);

  public static final String TENANT_HEADER = "x-tenant";
  /** Prefix for all endpoints in this service. */
  public static final String BASE_ENDPOINT = "/v1/undefineds";

  @Autowired
  private ProjectRepository repository;

  /**
   * Retrieves a page of undefineds.
   * @param page The details of the page to retrieve.
   * @return The HTTP response with the page of ${aggregates}s.
   */
  @ApiOperation(value = "Fetch a page of Projects.", response = PagedResources.class)
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<Page<ProjectResponse>> getAll(
      @PageableDefault Pageable page,
      @RequestHeader(TENANT_HEADER) String tenantId) {
    Page<Project> records = repository.findAllByTenantId(tenantId, page);
    ArrayList<ProjectResponse> projectResponses = new ArrayList<>();
    for (Project project : records.getContent()) {
      projectResponses.add(new ProjectResponse(project));
    }
    Page<ProjectResponse> pagedResponses = new PageImpl<>(projectResponses,
        new PageRequest(page.getPageNumber(), page.getPageSize()), projectResponses.size());
    return new ResponseEntity<>(pagedResponses, HttpStatus.OK);
  }

  /**
   * Retrieves an existing Project.
   * @param projectId The ID of the Project to retrieve.
   * @return The HTTP response representing the operation.
   */
  @ApiOperation(value = "Retrieve a single Project", response = ProjectResponse.class)
  @ApiResponses(@ApiResponse(code = 400, message = "Invalid Project ID provided."))
  @GetMapping("/{projectId}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<ProjectResponse> getSingle(
      @Valid @ApiParam(value = "Project ID", required = true) @PathVariable("projectId") String projectId,
      @RequestHeader(TENANT_HEADER) String tenantId) {
    Project record = repository.findByTenantIdAndProjectId(tenantId, projectId);
    if (record == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(new ProjectResponse(record), HttpStatus.OK);
  }
}
