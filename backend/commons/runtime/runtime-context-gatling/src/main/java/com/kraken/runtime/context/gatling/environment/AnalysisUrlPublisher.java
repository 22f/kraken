package com.kraken.runtime.context.gatling.environment;

import com.google.common.collect.ImmutableList;
import com.kraken.analysis.client.properties.AnalysisClientProperties;
import com.kraken.runtime.context.api.environment.EnvironmentPublisher;
import com.kraken.runtime.context.entity.ExecutionContextBuilder;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import static com.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource.BACKEND;
import static com.kraken.tools.environment.KrakenEnvironmentKeys.KRAKEN_ANALYSIS_URL;
import static com.kraken.tools.environment.KrakenEnvironmentKeys.KRAKEN_RUNTIME_URL;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class AnalysisUrlPublisher implements EnvironmentPublisher {

  @NonNull AnalysisClientProperties properties;

  @Override
  public boolean test(final String taskType) {
    return "DEBUG".equals(taskType) || "RECORD".equals(taskType);
  }

  @Override
  public ExecutionContextBuilder apply(final ExecutionContextBuilder context) {
    return context.addEntries(ImmutableList.of(
        ExecutionEnvironmentEntry.builder().from(BACKEND).scope("").key(KRAKEN_ANALYSIS_URL).value(properties.getAnalysisUrl()).build()
    ));
  }
}
