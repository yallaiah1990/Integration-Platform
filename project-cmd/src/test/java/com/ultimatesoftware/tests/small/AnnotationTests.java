package com.ultimatesoftware.tests.small;

import static com.ultimatesoftware.axon.spring.boot.validation.AnnotationAssertion.assertThatAnnotationIsPresent;
import static com.ultimatesoftware.axon.spring.boot.validation.AnnotationAssertion.assertThatCommandValidationAnnotationsArePresent;

import com.ultimatesoftware.axon.spring.boot.amqp.SuppressTopicBinding;
import org.junit.Test;

/**
 * Tests that commands and events have all required annotations.
 */
public class AnnotationTests extends BaseSmallTests {

  /**
   * Tests that all commands have validation annotations.
   */
  @Test
  public void testCommandValidationAnnotationsArePresent() {
    assertThatCommandValidationAnnotationsArePresent("com.ultimatesoftware.domain.commands");
  }

  /**
   * Tests that all events have the {@link SuppressTopicBinding} annotation.
   */
  @Test
  public void testEventsHaveSuppressTopicBindingAnnotation() {
    assertThatAnnotationIsPresent(SuppressTopicBinding.class, true, "com.ultimatesoftware.domain.events.produced");
  }
}
