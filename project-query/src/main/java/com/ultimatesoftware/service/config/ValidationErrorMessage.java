package com.ultimatesoftware.service.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

/**
 * Error messages that are sent back through the API.
 */
public class ValidationErrorMessage {

  private static final Logger LOGGER = LoggerFactory.getLogger(ValidationErrorMessage.class);

  private final List<Message> messages;

  /**
   * Translates given error codes into {@link Message}s using the given message source.
   * @param messageSource Used to localize error codes.
   * @param errorCodes Error codes to translate. May be the actual codes to look up, or the message templates to use.
   */
  public ValidationErrorMessage(MessageSource messageSource, List<String> errorCodes) {
    this.messages = new ArrayList<>();

    List<String> newMessages = new ArrayList<>();
    for (String error : errorCodes) {
      try {
        // If it is an error code, it should be found in messages.properties.
        newMessages.add(messageSource.getMessage(error, null, Locale.ENGLISH));
      } catch (NoSuchMessageException ex) {
        // If it's not found, simply provide the error or message template.
        newMessages.add(error);
      }
    }
    this.messages.add(new Message("errors", newMessages));
  }

  /**
   * Translates given error codes into {@link Message Messages} using the given message source.
   * @param messageSource Used to localize error codes.
   * @param errorCodes Error codes to translate.
   */
  public ValidationErrorMessage(MessageSource messageSource, Map<String, List<String>> errorCodes) {
    this.messages = new ArrayList<>();

    List<String> errorMessages;
    for (Map.Entry<String, List<String>> error : errorCodes.entrySet()) {
      try {
        errorMessages = new ArrayList<>(Collections.singletonList(messageSource.getMessage(error.getKey(), null, Locale.ENGLISH)));
      } catch (NoSuchMessageException ex) {
        errorMessages = error.getValue();
        if (errorMessages == null || errorMessages.isEmpty()) {
          LOGGER.error("Error message is empty.", ex);
        }
      }
      this.messages.add(new Message(error.getKey(), errorMessages));
    }
  }

  /**
   * @return List of translated messages.
   */
  public List<Message> getMessages() {
    return messages;
  }

  /**
   * A generic error code message.
   */
  public static class Message {

    private final String code;
    private final List<String> errorMessages;

    /**
     * @param code Error code.
     * @param message Error message.
     */
    public Message(String code, String message) {
      this.code = code;
      this.errorMessages = new ArrayList<>(Collections.singletonList(message));
    }

    /**
     * @param code Error code.
     * @param messages Error message.
     */
    public Message(String code, List<String> messages) {
      this.code = code;
      this.errorMessages = messages;
    }

    /**
     * @return Error code.
     */
    public String getCode() {
      return code;
    }

    /**
     * @return Error message.
     */
    public List<String> getMessages() {
      return errorMessages;
    }
  }
}
