package tests.pet;

import api.clients.PetClient;
import api.models.pet.*;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import testdata.pet.PetFactory;
import tests.BaseTest;

import java.util.ArrayList;
import java.util.List;

import static api.spec.PetResponseSpecs.*;
import static org.hamcrest.Matchers.*;

public class PetPositiveTests extends BaseTest {

    private final PetClient petClient = new PetClient();
    private final List<Long> createdPetIds = new ArrayList<>();

    @AfterEach
    public void tearDown() {
        Allure.step("Очистка данных после теста: удаление созданных сущностей", () -> {
            for (Long id : createdPetIds) {
                petClient.removePet(id);
            }
            createdPetIds.clear();
        });
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
    @DisplayName("Успешное создание питомца с полным набором данных")
    public void createPet_withFullData() {

        Pet pet = Allure.step("Подготовка: создание объекта питомца с полными набором полей", () ->
                PetFactory.randomPetFullData());

        Response createResponse = Allure.step("Отправка запроса: создание питомца", () ->
                petClient.createPet(pet)
        );

        Allure.step("Проверка ответа: API вернул 200 OK и id создан", () -> {
            createResponse
                    .then()
                    .spec(okResponse)
                    .body("id", greaterThan(0L));
        });

        Pet responsePet = extractPet(createResponse);
        Long id = responsePet.getId();
        createdPetIds.add(id); //сохранение id для последующего удаления
        pet.setId(id);

        Allure.step("Проверка содержания ответа: данные совпадают с отправленными", () -> {
            assertPetFieldsEquals(pet, responsePet);
        });

        Allure.step("Проверка состояния данных: данные сохранены в системе", () -> {
            Pet getPet = extractPet(petClient.getPetById(id));
            assertPetFieldsEquals(pet, getPet);
        });
    }

    @Test
    @DisplayName("Успешное создание питомца только с обязательными полями")
    public void createPet_withRequiredFields() {
        Pet pet = Allure.step("Подготовка: создание объекта питомца с обязательными полями", () ->
                PetFactory.randomPet());

        Response createResponse = Allure.step("Отправка запроса: создание питомца", () ->
                petClient.createPet(pet)
        );

        Allure.step("Проверка ответа: API вернул 200 OK и id создан", () -> {
            createResponse
                    .then()
                    .spec(okResponse)
                    .body("id", greaterThan(0L));
        });

        Pet responsePet = extractPet(createResponse);
        Long id = responsePet.getId();
        createdPetIds.add(id); //сохранение id для последующего удаления
        pet.setId(id);

        Allure.step("Проверка содержания ответа: данные совпадают с отправленными", () -> {
            assertPetFieldsEquals(pet, responsePet);
        });

        Allure.step("Проверка состояния данных: данные сохранены в системе", () -> {
            Pet getPet = extractPet(petClient.getPetById(id));
            assertPetFieldsEquals(pet, getPet);
        });
    }

    //GET

    @Test
    @DisplayName("Успешное получение по id существующего питомца")
    public void getPetById_existentPet() {
        Pet pet = Allure.step("Подготовка: создание объекта питомца", () ->
                PetFactory.randomPetFullData());
        Long id = Allure.step("Подготовка: отправка запроса - создание питомца", () ->
                        createPetAndSaveId(pet)
                );

        pet.setId(id);

        Response response = Allure.step("Отправка запроса: получение питомца по id", () ->
                        petClient.getPetById(id)
                );

        Allure.step("Проверка ответа: API вернул 200 OK и body содержит данные", () -> {
            response
                    .then()
                    .spec(okResponse)
                    .body(notNullValue());
        });

        Allure.step("Проверка содержания ответа: данные совпадают с исходными", () -> {
            Pet responsePet = extractPet(response);
            assertPetFieldsEquals(pet, responsePet);
        });
    }

    //UPDATE

    @Test
    @DisplayName("Успешное обновление существующего питомца")
    public void updatePet_existentPet() {

        Pet initialPet = Allure.step("Подготовка: создание объекта питомца", () ->
                PetFactory.randomPetFullData()
        );
        Long id = Allure.step("Подготовка: отправка запроса - создание питомца", () ->
                createPetAndSaveId(initialPet)
        );
        Pet updatePet = Allure.step("Подготовка: создание объекта для обновлённого питомца", () ->
                PetFactory.randomPetFullData()
                        .setId(id)
        );

        Response response = Allure.step("Отправка запроса: обновление питомца", () ->
                petClient.updatePet(updatePet)
        );

        Allure.step("Проверка ответа: API вернул 200 OK и body содержит данные", () -> {
            response
                    .then()
                    .spec(okResponse)
                    .body(notNullValue());
        });


        Allure.step("Проверка содержания ответа: данные совпадают с обновлёнными", () -> {
            Pet responsePet = extractPet(response);
            assertPetFieldsEquals(updatePet, responsePet);
        });


        // Проверка сохранённых данных: получаем питомца через API и сравниваем
        Allure.step("Проверка состояния данных: обновлённые данные сохранены в системе", () -> {
            Pet getPet = extractPet(petClient.getPetById(id));
            assertPetFieldsEquals(updatePet, getPet);
        });
    }

    //REMOVE

    @Test
    @DisplayName("Успешное удаление существующего питомца")
    public void removePet_existentPet() {

        Pet initialPet = Allure.step("Подготовка: создание объекта питомца", () ->
                PetFactory.randomPetFullData()
        );
        Long id = Allure.step("Подготовка: отправка запроса - создание питомца", () ->
                createPetAndSaveId(initialPet)
        );

        Response response = Allure.step("Отправка запроса: удаление питомца", () ->
                petClient.removePet(id)
        );

        Allure.step("Проверка ответа: API вернул 200 OK, body содержит id удалённого питомца", () -> {
            response
                    .then()
                    .spec(okResponse)
                    .body("message", equalTo(String.valueOf(id)));
        });

        Allure.step("Проверка состояния данных: питомец отсутствует в системе", () -> {
            petClient
                    .getPetById(id)
                    .then()
                    .spec(notFoundResponse);
        });
    }

    //FIND BY STATUS
    @ParameterizedTest
    @EnumSource(PetStatus.class)
    @DisplayName("Успешное получение питомцев по статусу")
    public void findPetsByStatus_existentStatus(PetStatus status) {

        Pet pet = Allure.step("Подготовка: создание объекта питомца с заданным статусом", () ->
                PetFactory.randomPetFullData()
                        .setStatus(status)
        );

        Allure.step("Подготовка: отправка запроса - создание питомца", () -> {
            createPetAndSaveId(pet);
        });

        Response response = Allure.step("Отправка запроса: поиск питомцев по статусу", () ->
                petClient.findPetsByStatus(status)
        );

        Allure.step("Проверка ответа: API вернул 200 OK, тело содержит непустой массив, " +
                "все элементы имеют искомый статус", () -> {
            response
                    .then()
                    .spec(okResponse)
                    .body("size()", greaterThan(0))
                    .body("status", everyItem(equalTo(status.toString())));
        });
    }

}
