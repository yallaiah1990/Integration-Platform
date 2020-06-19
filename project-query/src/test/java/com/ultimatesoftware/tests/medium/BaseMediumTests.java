package com.ultimatesoftware.tests.medium;

import static com.ultimatesoftware.service.ProjectController.BASE_ENDPOINT;
import static com.ultimatesoftware.service.ProjectController.TENANT_HEADER;
import static com.ultimatesoftware.tests.TestData.TENANT_ID_1;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.http.ContentType;
import com.ultimatesoftware.ProjectQuerySvcApplication;
import com.ultimatesoftware.test.helpers.AxonHelper;
import com.ultimatesoftware.test.helpers.EventBusHelper;
import com.ultimatesoftware.test.helpers.ProcessedEventListener;
import com.ultimatesoftware.tests.categories.Medium;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.awaitility.Awaitility;
import org.axonframework.eventhandling.Cluster;
import org.axonframework.eventhandling.annotation.AnnotationEventListenerAdapter;
import org.junit.Before;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("medium-test")
@Category(Medium.class)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProjectQuerySvcApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseMediumTests {

  protected static final int TIMEOUT = 10;
  @Autowired
  protected AxonHelper axonHelper;
  @LocalServerPort
  protected int port;
  @Value("${axon.terminal.rabbitmq.exchange-name}")
  protected String exchangeName;
  protected EventBusHelper eventBus;
  protected ProcessedEventListener eventListener;
  @Autowired
  private Cluster cluster;
  @Autowired
  protected MongoTemplate mongoTemplate;

  /**
   * Test setup.
   */
  @Before
  public void setup() {
    eventListener = new ProcessedEventListener(cluster);

    eventBus = axonHelper.createEventBus(exchangeName, cluster);
    eventBus.subscribe(new AnnotationEventListenerAdapter(eventListener));

    RestAssured.requestSpecification = new RequestSpecBuilder() //NOSONAR - This is how RestAssured is used.
        .addHeader(TENANT_HEADER, TENANT_ID_1)
        .setBasePath(BASE_ENDPOINT)
        .setContentType(ContentType.JSON)
        .setPort(port)
        .build();
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

    Awaitility.setDefaultTimeout(TIMEOUT, TimeUnit.SECONDS);

    cleanUpDB();
  }

  public void cleanUpDB() {
    Set<String> collectionNames = mongoTemplate.getDb().getCollectionNames();
    for (String collectionName : collectionNames) {
      mongoTemplate.remove(new Query(), collectionName);
    }
  }
}
