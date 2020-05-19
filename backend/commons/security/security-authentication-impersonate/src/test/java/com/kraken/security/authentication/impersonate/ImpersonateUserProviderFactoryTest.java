package com.kraken.security.authentication.impersonate;

import com.kraken.config.security.client.api.SecurityClientProperties;
import com.kraken.security.client.api.SecurityClient;
import com.kraken.security.decoder.api.TokenDecoder;
import com.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.kraken.security.authentication.api.AuthenticationMode.IMPERSONATE;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ImpersonateUserProviderFactoryTest {

  @Mock
  SecurityClientProperties clientProperties;
  @Mock
  TokenDecoder decoder;
  @Mock
  SecurityClient client;

  ImpersonateUserProviderFactory factory;

  @BeforeEach
  public void setUp() {
    factory = new ImpersonateUserProviderFactory(clientProperties, decoder, client);
  }

  @Test
  public void shouldCreate() {
    final var userProvider = factory.create("userId");
    assertThat(userProvider).isNotNull();
  }

  @Test
  public void shouldGetMode() {
    assertThat(factory.getMode()).isEqualTo(IMPERSONATE);
  }

  @Test
  public void shouldPassNPE() {
    TestUtils.shouldPassNPE(ImpersonateUserProviderFactory.class);
  }

}