package com.ultimatesoftware.tests.medium;

import static com.ultimatesoftware.service.projectController.BASE_ENDPOINT;
import static com.ultimatesoftware.service.projectController.TENANT_HEADER;
import static com.ultimatesoftware.tests.TestData.TENANT_ID_1;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.http.ContentType;
import com.ultimatesoftware.projectCmdSvcApplication;
import com.ultimatesoftware.axon.spring.boot.amqp.SuppressTopicBinding;
import com.ultimatesoftware.test.helpers.AMQPHelper;
import com.ultimatesoftware.test.helpers.AxonHelper;
import com.ultimatesoftware.test.helpers.EventBusHelper;
import com.ultimatesoftware.test.helpers.ProcessedEventListener;
import com.ultimatesoftware.tests.categories.Medium;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.awaitility.Awaitility;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.Cluster;
import org.axonframework.eventhandling.annotation.AnnotationEventListenerAdapter;
import org.junit.Before;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Queue;
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
@SpringBootTest(classes = projectCmdSvcApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public abstract class BaseMediumTests {

  protected static final int TIMEOUT = 10;
  @Autowired
  protected AMQPHelper amqpHelper;
  @Autowired
  protected AxonHelper axonHelper;
  @LocalServerPort
  protected int port;
  @Value("${axon.terminal.rabbitmq.exchange-name}")
  protected String exchangeName;
  /**
   * Event bus to use when listening to events emitted by this service.
   * This bus uses an external queue not directly bound to the service, and is required to listen to events annotated with @SuppressTopicBinding.
   */
  protected EventBusHelper queueEventBus;
  /**
   * Event bus to use when publishing mock events to this service.
   * This bus is bound directly to the service cluster, and is required to send events with ProcessedEventListener.
   * TODO: Remove this bus if the service does not listen to events.
   */
  protected EventBusHelper clusterEventBus;
  protected ProcessedEventListener eventListener;
  @Autowired
  private Cluster cluster;
  @Autowired
  protected MongoTemplate mongoTemplate;

  @Autowired
  protected CommandGateway commandGateway;

  /**
   * Setup for medium tests.
   */
  @Before
  public void setup() {
    eventListener = new ProcessedEventListener(cluster);
    axonHelper.setPackageName("com.ultimatesoftware");
    AnnotationEventListenerAdapter eventListenerAdapter = new AnnotationEventListenerAdapter(eventListener);

    String queueName = UUID.randomUUID().toString();
    Queue queue = amqpHelper.createQueue(exchangeName, queueName);
    queueEventBus = axonHelper.createEventBus(exchangeName, queue);
    queueEventBus.subscribe(new AnnotationEventListenerAdapter(eventListener));
    queueEventBus.subscribe(eventMessage -> {
      if (eventMessage.getPayloadType().getAnnotation(SuppressTopicBinding.class) != null) {
        // Store suppressed topic events as well.
        eventListener.onEventProcessingCompleted(Collections.singletonList(eventMessage));
      }
    });
    clusterEventBus = axonHelper.createEventBus(exchangeName, cluster);
    clusterEventBus.subscribe(eventListenerAdapter);

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
