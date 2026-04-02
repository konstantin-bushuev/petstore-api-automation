package api.spec;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

public class PetResponseSpecs {

    public static ResponseSpecification okResponse =
            new ResponseSpecBuilder()
                    .expectStatusCode(200)
                    .expectContentType(ContentType.JSON)
                    .build();

    public static ResponseSpecification notFoundResponse =
            new ResponseSpecBuilder()
                    .expectStatusCode(404)
                    .build();

    public static ResponseSpecification badRequestResponse =
            new ResponseSpecBuilder()
                    .expectStatusCode(400)
                    .build();

    // Невалидный id (тип/формат)
    public static ResponseSpecification invalidIdResponse =
            new ResponseSpecBuilder()
                    .expectStatusCode(anyOf(is(400), is(404)))
                    .build();

    // Отсутствует id (неправильный endpoint)
    public static ResponseSpecification missingIdOrWrongPathResponse =
            new ResponseSpecBuilder()
                    .expectStatusCode(anyOf(is(400), is(405)))
                    .build();

    // Некорректный body (пустой, синтаксическая ошибка, отсутствуют поля)
    public static ResponseSpecification invalidBodyResponse =
            new ResponseSpecBuilder()
                    .expectStatusCode(anyOf(is(400), is(405)))
                    .build();

    // Неверный Content-Type
    public static ResponseSpecification contentTypeMismatchResponse =
            new ResponseSpecBuilder()
                    .expectStatusCode(anyOf(is(405), is(415)))
                    .build();

    // Дублирование id
    public static ResponseSpecification duplicateIdResponse =
            new ResponseSpecBuilder()
                    .expectStatusCode(anyOf(is(400), is(409)))
                    .build();
}
