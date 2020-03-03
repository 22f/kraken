package com.kraken.runtime.context.gatling.environment;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.kraken.runtime.context.api.environment.EnvironmentPublisher;
import com.kraken.runtime.context.entity.ExecutionContextBuilder;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuples;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource.BACKEND;
import static com.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource.USER;
import static com.kraken.tools.environment.KrakenEnvironmentKeys.*;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class JavaOptsPublisher implements EnvironmentPublisher {

  @Override
  public boolean test(final String taskType) {
    return true;
  }

  @Override
  public ExecutionContextBuilder apply(final ExecutionContextBuilder context) {
    final var userEntries = context.getEntries().stream()
        .filter(entry -> entry.getFrom() == USER)
        .collect(Collectors.toUnmodifiableList());

    Preconditions.checkArgument(userEntries.isEmpty() || userEntries
        .stream()
        .map(ExecutionEnvironmentEntry::getKey)
        .allMatch(key -> key.matches("[a-zA-Z_]+[a-zA-Z0-9_]*")), "Environment variable names must match the regular expression [a-zA-Z_]{1,}[a-zA-Z0-9_]{0,}");

    // Reason for this: https://stackoverflow.com/questions/38349325/throws-error-when-passing-argument-with-space-in-java-opts-in-linux-os
    Preconditions.checkArgument(userEntries.isEmpty() || userEntries
        .stream()
        .map(ExecutionEnvironmentEntry::getValue)
        .allMatch(value -> value.matches("\\S+")), "Environment variable values cannot contain spaces.");

    final var globalUserEntries = userEntries.stream()
        .filter(entry -> entry.getScope().isEmpty())
        .collect(Collectors.toUnmodifiableList());

    final Function<String, @NonNull List<ExecutionEnvironmentEntry>> hostEntries = (String hostId) -> userEntries.stream()
        .filter(entry -> entry.getScope().equals(hostId))
        .collect(Collectors.toUnmodifiableList());

    final var javaOptsEntries = context.getHostIds()
        .stream()
        .map(hostId -> newJavaOptsEntry(hostId, ImmutableList.<ExecutionEnvironmentEntry>builder().addAll(globalUserEntries).addAll(hostEntries.apply(hostId)).build()))
        .collect(Collectors.toUnmodifiableList());

    return context.addEntries(javaOptsEntries);
  }

  private ExecutionEnvironmentEntry newJavaOptsEntry(final String hostId, final List<ExecutionEnvironmentEntry> entries) {
    return ExecutionEnvironmentEntry.builder()
        .from(USER)
        .scope(hostId)
        .key(KRAKEN_GATLING_JAVA_OPTS)
        .value(entries
            .stream()
            .map(entry -> String.format("-D%s=%s", entry.getKey(), entry.getValue()))
            .collect(Collectors.joining(" ")))
        .build();
  }


//  SpringJavaOptsFactory factory;
//
//  @Before
//  public void before(){
//    factory = new SpringJavaOptsFactory();
//  }
//
//  @Test
//  public void shouldGenerateOpts1(){
//    assertThat(factory.apply(ImmutableMap.of("foo", "bar", "test", "someValue"))).isEqualTo("-Dfoo=bar -Dtest=someValue");
//  }
//
//  @Test
//  public void shouldGenerateOpts2(){
//    assertThat(factory.apply(ImmutableMap.of("KRAKEN_VERSION", "1.3.0", "MyValue", "someValue"))).isEqualTo("-DKRAKEN_VERSION=1.3.0 -DMyValue=someValue");
//  }
//
//  @Test
//  public void shouldGenerateOpts3(){
//    assertThat(factory.apply(ImmutableMap.of())).isEqualTo("");
//  }
//
//  @Test(expected = IllegalArgumentException.class)
//  public void shouldFail1(){
//    factory.apply(ImmutableMap.of("4foo", "bar"));
//  }
//
//  @Test(expected = IllegalArgumentException.class)
//  public void shouldFail2(){
//    factory.apply(ImmutableMap.of("fo o", "bar"));
//  }
//
//  @Test(expected = IllegalArgumentException.class)
//  public void shouldFail3(){
//    factory.apply(ImmutableMap.of("fo-o", "bar"));
//  }
//
//  @Test(expected = IllegalArgumentException.class)
//  public void shouldFail4(){
//    factory.apply(ImmutableMap.of("foo", "joe bar"));
//  }
}
