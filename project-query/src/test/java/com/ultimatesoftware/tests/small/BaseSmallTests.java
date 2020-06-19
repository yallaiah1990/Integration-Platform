package com.ultimatesoftware.tests.small;

import com.ultimatesoftware.domain.ProjectRepository;
import com.ultimatesoftware.tests.categories.Small;
import java.util.Set;
import org.axonframework.eventhandling.EventBus;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("small-test")
@Category(Small.class)
public abstract class BaseSmallTests {

  @Autowired
  protected EventBus eventBus;

  @Autowired
  protected MongoTemplate mongoTemplate;

  @Autowired
  protected ProjectRepository repository;

  /**
   * Test setup.
   */
  public void setup() {
    cleanUpDB();
  }

  public void cleanUpDB() {
    Set<String> collectionNames = mongoTemplate.getDb().getCollectionNames();
    for (String collectionName : collectionNames) {
      mongoTemplate.remove(new Query(), collectionName);
    }
  }
}
