package com.ultimatesoftware.service.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * The API representation of a(n) project response.
 */
@JsonInclude(Include.NON_NULL)
public class projectResponse {

  @ApiModelProperty(value = "project ID", required = true)
  @NotBlank
  private String id;

  @ApiModelProperty(value = "Property 1", required = true)
  private String property1;

  public projectResponse(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public projectResponse setId(String id) {
    this.id = id;
    return this;
  }

  public String getProperty1() {
    return property1;
  }

  public projectResponse setProperty1(String property1) {
    this.property1 = property1;
    return this;
  }
}
