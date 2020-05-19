package com.kraken.runtime.context.environment;

import com.kraken.runtime.context.entity.ExecutionContextBuilderTest;
import com.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;


import static com.kraken.runtime.entity.task.TaskType.*;
import static com.kraken.tools.environment.KrakenEnvironmentKeys.KRAKEN_LOCAL_IP;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = LocalIPPublisher.class)
@ActiveProfiles(profiles = {"docker", "kubernetes"})
public class LocalIPPublisherProdTest {

  @Autowired
  LocalIPPublisher publisher;

  @Test
  public void shouldGet() {
    final var env = publisher.apply(ExecutionContextBuilderTest.EXECUTION_CONTEXT_BUILDER).block();
    assertThat(env).isNotNull();
    assertThat(env).isEmpty();
  }

}