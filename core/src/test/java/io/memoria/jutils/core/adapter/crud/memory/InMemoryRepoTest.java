package io.memoria.jutils.core.adapter.crud.memory;

import io.memoria.jutils.core.domain.Err.AlreadyExists;
import io.memoria.jutils.core.domain.Err.NotFound;
import io.memoria.jutils.core.domain.port.crud.ReadRepo;
import io.memoria.jutils.core.domain.port.crud.WriteRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Map;

import static io.vavr.control.Option.some;

public class InMemoryRepoTest {
  private static record User(String id, int age) {}

  private final Map<String, User> db = new HashMap<>();
  private final ReadRepo<String, User> readRepo = new InMemoryReadRepo<>(db);
  private final WriteRepo<String, User> writeRepo = new InMemoryWriteRepo<>(db);
  private final User user = new User("bob", 20);
  private final User otherUser = new User("bob", 23);

  @AfterEach
  public void afterEach() {
    db.clear();
  }

  @Test
  @DisplayName("Already exists")
  public void alreadyExists() {
    db.put(this.user.id, this.user);
    StepVerifier.create(writeRepo.create(user.id, user)).expectError(AlreadyExists.class).verify();
  }

  @Test
  @DisplayName("Should crud the entity")
  public void crudTest() {
    StepVerifier.create(writeRepo.create(user.id, user)).expectNext(user).expectComplete().verify();
    Assertions.assertEquals(new User("bob", 20), db.get("bob"));
    StepVerifier.create(writeRepo.update(otherUser.id, otherUser)).expectComplete().verify();
    StepVerifier.create(writeRepo.delete(user.id)).expectComplete().verify();
  }

  @Test
  @DisplayName("Should delete successfully")
  public void delete() {
    db.put(this.user.id, this.user);
    StepVerifier.create(writeRepo.delete(user.id)).expectComplete().verify();
    Assertions.assertNull(db.get(user.id));
  }

  @Test
  @DisplayName("Should exists")
  public void exists() {
    db.put(this.user.id, this.user);
    StepVerifier.create(readRepo.get(user.id)).expectNext(some(user)).expectComplete().verify();
    StepVerifier.create(readRepo.exists(user.id)).expectNext(true).expectComplete().verify();
    db.clear();
    StepVerifier.create(readRepo.exists(user.id)).expectNext(false).expectComplete().verify();
  }

  @Test
  @DisplayName("Should be not found")
  public void notFoundTest() {
    StepVerifier.create(writeRepo.update(user.id, user)).expectError(NotFound.class).verify();
  }
}
