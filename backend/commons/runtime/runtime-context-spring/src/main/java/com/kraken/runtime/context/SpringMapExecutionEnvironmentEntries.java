package com.kraken.runtime.context;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.context.api.FilterScopedEntries;
import com.kraken.runtime.context.api.MapExecutionEnvironmentEntries;
import com.kraken.runtime.context.api.SortExecutionEnvironmentEntries;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class SpringMapExecutionEnvironmentEntries implements MapExecutionEnvironmentEntries {

  @NonNull FilterScopedEntries filter;
  @NonNull SortExecutionEnvironmentEntries sort;

  @Override
  public Map<String, String> apply(final String hostId, final List<ExecutionEnvironmentEntry> entries) {
    final var scopedEntries = this.filter.apply(hostId, entries);
    final var sortedEntries = this.sort.apply(scopedEntries);
    final var map = new HashMap<String, String>();
    sortedEntries.stream().forEach(entry -> map.put(entry.getKey(), entry.getValue()));
    return ImmutableMap.copyOf(map);
  }

}
