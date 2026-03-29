package api.spec;

import api.config.RestConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RequestSpecs {
    public static final RequestSpecification baseSpec =
            new RequestSpecBuilder()
                    .setBaseUri(RestConfig.BASE_URL)
                    .setContentType(ContentType.JSON)
                    .build();
}
