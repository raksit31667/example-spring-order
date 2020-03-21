package com.raksit.example.order;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import java.util.Collections;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.given;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@AutoConfigureTestDatabase(replace = Replace.NONE)
@AutoConfigureEmbeddedDatabase
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class IntegrationTest {

  @LocalServerPort
  private int port;

  @Value("${security.oauth2.resource.issuer}")
  private String issuer;

  @Value("${security.oauth2.resource.id}")
  private String resourceId;

  @Autowired
  private TokenStore tokenStore;

  @BeforeEach
  void setUp() {
    RestAssured.port = port;
  }

  @AfterEach
  void tearDown() {
    RestAssured.reset();
  }

  protected RequestSpecification givenRequestWithValidReadToken() {
    return givenRequestWithToken(JWT.create()
        .withAudience(resourceId)
        .withIssuer(issuer)
        .withArrayClaim("roles", new String[]{"Read"})
        .sign(Algorithm.none()));
  }

  protected RequestSpecification givenRequestWithValidWriteToken() {
    return givenRequestWithToken(JWT.create()
        .withAudience(resourceId)
        .withIssuer(issuer)
        .withArrayClaim("roles", new String[]{"Write"})
        .sign(Algorithm.none()));
  }

  protected RequestSpecification givenRequestWithInvalidToken() {
    return given()
        .auth()
        .oauth2("invalidToken");
  }

  private RequestSpecification givenRequestWithToken(String accessToken) {
    setUpTokenStore(accessToken);
    return given()
        .auth()
        .oauth2(accessToken);
  }

  private void setUpTokenStore(String accessToken) {
    tokenStore.storeAccessToken(new DefaultOAuth2AccessToken(accessToken),
        new OAuth2Authentication(
            new TokenRequest(null, "client", null, "client_credentials")
                .createOAuth2Request(new BaseClientDetails("client", null, "read",
                    "client_credentials", "")),
            new TestingAuthenticationToken("writer", "writer", Collections.emptyList())));
  }
}
