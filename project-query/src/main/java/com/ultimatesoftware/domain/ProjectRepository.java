package com.ultimatesoftware.domain;

import com.ultimatesoftware.domain.entities.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Database entrypoint for Projects.
 */
@Repository
public interface ProjectRepository extends PagingAndSortingRepository<Project, String> {

  /**
   * @param tenantId The tenant that the Project belongs to.
   * @param page The paging information.
   * @return A page of Projects.
   */
  Page<Project> findAllByTenantId(String tenantId, Pageable page);

  /**
   * @param tenantId The tenant that the Project belongs to.
   * @param projectId The Id of the Project.
   * @return The Project that meets the criteria given, null if no Project is found.
   */
  Project findByTenantIdAndProjectId(String tenantId, String projectId);

  /**
   * @param tenantId The tenant that the Project belongs to.
   * @return The count of records by tenantId.
   */
  long countByTenantId(String tenantId);
}
