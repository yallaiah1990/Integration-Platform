package com.ultimatesoftware.tests.small;

import com.ultimatesoftware.tests.categories.Small;
import org.junit.experimental.categories.Category;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("small-test")
@Category(Small.class)
public abstract class BaseSmallTests {

  /**
   * Test setup.
   */
  public void setup() {
  }
}
