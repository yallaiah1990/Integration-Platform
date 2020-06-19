package com.ultimatesoftware.tests.small;

import static com.ultimatesoftware.axon.spring.boot.validation.AnnotationAssertion.assertThatAnnotationIsNotPresent;

import com.ultimatesoftware.axon.spring.boot.amqp.SuppressTopicBinding;
import org.junit.Test;

/**
 * Tests that events have all required annotations.
 */
public class AnnotationTests extends BaseSmallTests {

  /**
   * Tests that all events do not have the {@link SuppressTopicBinding} annotation.
   */
  @Test
  public void testEventsDoNotHaveSuppressTopicBindingAnnotation() {
    assertThatAnnotationIsNotPresent(SuppressTopicBinding.class, true, "com.ultimatesoftware.domain.events.consumed");
  }
}
