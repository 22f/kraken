package com.kraken.runtime.context.environment;

import com.kraken.runtime.client.properties.RuntimeClientProperties;
import com.kraken.runtime.context.api.environment.EnvironmentPublisher;
import com.kraken.runtime.context.entity.ExecutionContextBuilder;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import com.kraken.runtime.entity.task.TaskType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import static com.google.common.collect.ImmutableList.of;
import static com.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource.BACKEND;
import static com.kraken.tools.environment.KrakenEnvironmentKeys.KRAKEN_RUNTIME_URL;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class RuntimeUrlPublisher implements EnvironmentPublisher {

  @NonNull RuntimeClientProperties properties;

  @Override
  public boolean test(final TaskType taskType) {
    return true;
  }

  @Override
  public ExecutionContextBuilder apply(final ExecutionContextBuilder context) {
    return context.addEntries(
      of(
        ExecutionEnvironmentEntry.builder().from(BACKEND).scope("").key(KRAKEN_RUNTIME_URL).value(properties.getUrl()).build()
      )
    );
  }
}
