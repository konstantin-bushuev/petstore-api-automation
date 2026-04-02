package tests;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.LogConfig;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {

    @BeforeAll
    static void startUp() {

        RestAssured.config = RestAssured.config()
                .httpClient(
                        HttpClientConfig.httpClientConfig()
                                .setParam("http.connection.timeout", 5000)
                                .setParam("http.socket.timeout", 5000)
                                .setParam("http.connection-manager.timeout", 5000)
                );

        RestAssured.config = RestAssured
                .config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());
    }
}
