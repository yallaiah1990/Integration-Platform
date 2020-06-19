package com.ultimatesoftware.tests.small;

import static com.ultimatesoftware.service.projectController.BASE_ENDPOINT;
import static com.ultimatesoftware.service.projectController.TENANT_HEADER;
import static com.ultimatesoftware.tests.TestData.TENANT_ID_1;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ultimatesoftware.projectCmdSvcApplication;
import com.ultimatesoftware.service.resources.projectRequest;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(classes = projectCmdSvcApplication.class)
@RunWith(SpringRunner.class)
@WebAppConfiguration
public class SmallControllerTests extends BaseSmallTests {

  public static final String PROPERTY_1 = "property1";

  @Autowired
  protected WebApplicationContext webApplicationContext;

  @Autowired
  protected CommandGateway commandGateway;

  private MockMvc mvc;

  /**
   * Test setup.
   */
  @Before
  public void setup() {
    super.setup();
    mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .alwaysDo(MockMvcResultHandlers.print())
        .build();
  }

  @Test
  public void whenValidRequestToUpdateproject_thenExpectHttpOk() throws Exception {
    projectRequest request = new projectRequest()
        .setProperty1(PROPERTY_1);

    when(commandGateway.sendAndWait(any())).thenReturn(null);

    mvc.perform(
        post(BASE_ENDPOINT + "/anyid")
            .header(TENANT_HEADER, TENANT_ID_1)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsBytes(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").isString())
        .andExpect(jsonPath("$.property1").isString());
  }

  @Test
  public void whenInvalidRequestToUpdateproject_thenExpectHttpBadRequest() throws Exception {
    projectRequest request = new projectRequest()
        .setProperty1("");

    mvc.perform(
        post(BASE_ENDPOINT + "/anyid")
            .header(TENANT_HEADER, TENANT_ID_1)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsBytes(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.messages").isArray());
  }
}
