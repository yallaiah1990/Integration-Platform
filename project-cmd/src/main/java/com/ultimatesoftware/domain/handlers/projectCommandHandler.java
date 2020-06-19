package com.ultimatesoftware.domain.handlers;

import com.ultimatesoftware.domain.projectAggregate;
import com.ultimatesoftware.domain.commands.BaseCommand;
import com.ultimatesoftware.domain.commands.UpdateprojectCommand;
import com.ultimatesoftware.domain.events.produced.projectUpdatedEvent;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventstore.EventStore;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.LockManager;
import org.axonframework.repository.PessimisticLockManager;
import org.axonframework.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Responsible for handling any command related to project.
 */
@Component
public class projectCommandHandler {

  private final Repository<projectAggregate> repository;

  /**
   * Externally managed lock manager for creating aggregates when they are not found.
   */
  private final LockManager lockManager;

  @Autowired
  public projectCommandHandler(EventBus eventBus, EventStore eventStore) {
    this.lockManager = new PessimisticLockManager();
    EventSourcingRepository<projectAggregate> newRepository =
        new EventSourcingRepository<>(projectAggregate.class, eventStore, lockManager);
    newRepository.setEventBus(eventBus);
    this.repository = newRepository;
  }

  /**
   * Handle update command.
   * @param command Command.
   */
  @CommandHandler
  public void handle(UpdateprojectCommand command) {
    projectAggregate project = createOrLoadproject(command);
    projectUpdatedEvent updateEvent = new projectUpdatedEvent(command.getTenantId(), command.getprojectId())
        .setProperty1(command.getProperty1());
    project.update(updateEvent);
  }

  private projectAggregate createOrLoadproject(BaseCommand command) {
    projectAggregate project;
    String aggregateId = command.getAggregateId();
    lockManager.obtainLock(aggregateId);
    try {
      project = repository.load(command.getAggregateId());
    } catch (AggregateNotFoundException ex) { //NOSONAR - If aggregate not found, create new one.
      project = new projectAggregate(command);
      repository.add(project);
    } finally {
      lockManager.releaseLock(aggregateId);
    }
    return project;
  }
}
