package com.raksit.example.order;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@AutoConfigureTestDatabase(replace = Replace.NONE)
@AutoConfigureEmbeddedDatabase
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class IntegrationTest {

}
