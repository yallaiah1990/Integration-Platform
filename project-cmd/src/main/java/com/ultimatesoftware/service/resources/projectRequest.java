package com.ultimatesoftware.service.resources;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * The API representation of a(n) project request resource.
 */
public class projectRequest {

  @ApiModelProperty(value = "Property 1", required = true)
  @NotBlank
  private String property1;

  public String getProperty1() {
    return property1;
  }

  public projectRequest setProperty1(String property1) {
    this.property1 = property1;
    return this;
  }
}
