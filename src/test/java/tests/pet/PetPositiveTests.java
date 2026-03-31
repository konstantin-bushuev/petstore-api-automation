package tests.pet;

import api.clients.PetClient;
import api.models.pet.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import testdata.pet.PetFactory;
import tests.BaseTest;

import java.util.ArrayList;
import java.util.List;

import static api.spec.ResponseSpecs.*;
import static org.hamcrest.Matchers.*;

public class PetPositiveTests extends BaseTest {

    private final PetClient petClient = new PetClient();
    private final List<Long> createdPetIds = new ArrayList<>();

    @AfterEach
    public void tearDown() {
        // Удаляем созданные сущности
        for (Long id : createdPetIds) {
            petClient.removePet(id);
        }
    }

    //HELPERS

    // Создаём объект питомца, отправляем запрос на создание и сохраняем полученный id
    private Long createPetAndSaveId(Pet pet) {
        Response response = petClient.createPet(pet);
        Long id = response.then().extract().body().path("id");
        createdPetIds.add(id);
        return id;
    }

    // Извлекаем объект питомца из ответа
    private Pet extractPet(Response response) {
        return response.then().extract().as(Pet.class);
    }

    // Приводим к единообразию поле tags
    private List<PetTag> normalizeTags(List<PetTag> tags) {
        return (tags == null || tags.isEmpty()) ? List.of() : tags;
    }

    // Сравниваем питомцев по полям
    private void assertPetFieldsEquals(Pet expected, Pet actual) {
        Assertions.assertAll(
                () -> Assertions.assertEquals(expected.getId(), actual.getId(), "id mismatch"),
                () -> Assertions.assertEquals(expected.getName(), actual.getName(), "name mismatch"),
                () -> Assertions.assertEquals(expected.getPhotoUrls(), actual.getPhotoUrls(), "photoUrls mismatch"),
                () -> Assertions.assertEquals(expected.getCategory(), actual.getCategory(), "category mismatch"),
                () -> Assertions.assertEquals(expected.getStatus(), actual.getStatus(), "status mismatch"),
                () -> Assertions.assertEquals(normalizeTags(expected.getTags()), normalizeTags(actual.getTags()), "tags mismatch")
        );
    }


    //CREATE

    @Test
    public void createPet_withFullData() {
        // Подготовка: создаём объект питомца с полными данными для проверки всех полей
        Pet pet = PetFactory.randomPetFullData();

        // Действие: отправляем запрос на создание питомца
        Response createResponse = petClient
                .createPet(pet);

        // Проверка ответа: API вернул 200 OK и создал id
        createResponse
                .then()
                .spec(okResponse)
                .body("id", greaterThan(0L));

        // Извлекаем объект питомца из ответа
        Pet responsePet = extractPet(createResponse);

        // Сохраняем id для последующего удаления сущности и присваиваем его исходному объекту
        Long id = responsePet.getId();
        createdPetIds.add(id);
        pet.setId(id);

        // Проверка соответствия данных в ответе
        assertPetFieldsEquals(pet, responsePet);

        // Проверка сохранённых данных: получаем питомца через API и сравниваем
        Pet getPet = extractPet(petClient.getPetById(id));
        assertPetFieldsEquals(pet, getPet);
    }

    @Test
    public void createPet_withRequiredFields() {
        // Подготовка: создаём объект питомца только с обязательными полями для проверки создания с неполными данными
        Pet pet = PetFactory.randomPet();

        // Действие: отправляем запрос на создание питомца
        Response createResponse = petClient
                .createPet(pet);

        // Проверка ответа: API вернул 200 OK и создал id
        createResponse
                .then()
                .spec(okResponse)
                .body("id", greaterThan(0L));

        // Извлекаем объект питомца из ответа
        Pet responsePet = extractPet(createResponse);

        // Сохраняем id и присваиваем его исходному объекту
        Long id = responsePet.getId();
        createdPetIds.add(id);
        pet.setId(id);

        // Проверка соответствия данных в ответе
        assertPetFieldsEquals(pet, responsePet);

        // Проверка сохранённых данных: получаем питомца через API и сравниваем
        Pet getPet = extractPet(petClient.getPetById(id));
        assertPetFieldsEquals(pet, getPet);
    }

    //GET

    @Test
    public void getPetById_existentPet() {
        // Подготовка: создаём объект питомца, отправляем запрос на создание, сохраняем id и присваиваем его объекту
        Pet pet = PetFactory.randomPetFullData();
        Long id = createPetAndSaveId(pet);
        pet.setId(id);

        // Действие: отправляем запрос на поиск питомца по id
        Response response = petClient
                .getPetById(id);

        // Проверка ответа: API вернул 200 OK, тело содержит данные
        response
                .then()
                .spec(okResponse)
                .body(notNullValue());

        // Проверка соответствия данных в ответе: извлекаем питомца из ответа и сравниваем с исходным
        Pet responsePet = extractPet(response);
        assertPetFieldsEquals(pet, responsePet);
    }

    //UPDATE

    @Test
    public void updatePet_existentPet() {

        // Подготовка: создаём объект питомца, отправляем запрос на создание, сохраняем id
        Pet initialPet = PetFactory.randomPetFullData();
        Long id = createPetAndSaveId(initialPet);
        // Создаём объект обновлённого питомца с новым набором данных и присваиваем ему существующий id
        Pet updatePet = PetFactory.randomPetFullData()
                .setId(id);

        // Действие: отправляем запрос на обновление
        Response response = petClient
                .updatePet(updatePet);

        // Проверка ответа: API вернул 200 OK, тело содержит данные
        response
                .then()
                .spec(okResponse)
                .body(notNullValue());

        // Проверка соответствия данных в ответе: извлекаем объект питомца из ответа и сравниваем с обновлённым
        Pet responsePet = extractPet(response);
        assertPetFieldsEquals(updatePet, responsePet);

        // Проверка сохранённых данных: получаем питомца через API и сравниваем
        Pet getPet = extractPet(petClient.getPetById(id));
        assertPetFieldsEquals(updatePet, getPet);
    }

    //REMOVE

    @Test
    public void removePet_existentPet() {

        // Подготовка: создаём объект питомца, отправляем запрос на создание, сохраняем id
        Pet pet = PetFactory.randomPetFullData();
        Long id = createPetAndSaveId(pet);

        // Действие: отправляем запрос на удаление питомца
        Response response = petClient
                .removePet(id);

        // Проверка ответа: API вернул 200 OK, в теле пришёл id удалённого питомца
        response
                .then()
                .spec(okResponse)
                .body("message", equalTo(String.valueOf(id)));

        // Проверка состояния данных: пытаемся получить питомца через API и проверяем, что он отсутствует
        petClient
                .getPetById(id)
                .then()
                .spec(notFoundResponse);
    }

    //FIND BY STATUS
    @ParameterizedTest
    @EnumSource(PetStatus.class)
    public void findPetsByStatus_existentStatus(PetStatus status) {
        // Подготовка: создаём объект питомца с заданным статусом, отправляем запрос на создание, сохраняем id
        Pet pet = PetFactory.randomPetFullData()
                .setStatus(status);
        createPetAndSaveId(pet);

        // Действие: отправляем запрос на получение питомца
        Response response = petClient
                .findPetsByStatus(status);

        // Проверка ответа: API вернул 200 OK, тело содержит непустой массив, статус элементов соответствует
        response
                .then()
                .spec(okResponse)
                .body("size()", greaterThan(0))
                .body("status", everyItem(equalTo(status.toString())));
    }

}
