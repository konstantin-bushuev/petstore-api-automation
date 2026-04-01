package tests.pet;

import api.clients.PetClient;
import api.models.pet.Pet;
import io.qameta.allure.Allure;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testdata.pet.PetFactory;
import tests.BaseTest;

import java.util.ArrayList;
import java.util.List;

import static api.spec.ResponseSpecs.*;
import static testdata.pet.PetNegativeData.*;

public class PetNegativeTests extends BaseTest {

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


    // CREATE

    @Test
    @DisplayName("Создание питомца без передачи body >> ошибка валидации >> ожидается 400 или 405")
    public void createPet_withoutBody() {
        petClient.createPetRaw(null, ContentType.JSON)
                .then()
                .spec(invalidBodyResponse);
    }

    @Test
    @DisplayName("Создание питомца с пустым json body >> ошибка валидации >> ожидается 400 или 405")
    public void createPet_withEmptyBody() {
        petClient.createPetRaw("", ContentType.JSON)
                .then()
                .spec(invalidBodyResponse);
    }

    @Test
    @DisplayName("Создание питомца с некорректным json body >> ошибка валидации >> ожидается 400 или 405")
    public void createPet_withBadJsonBody() {
        petClient.createPetRaw(BAD_JSON, ContentType.JSON)
                .then()
                .spec(invalidBodyResponse);
    }

    // Отсутствуют обязательные поля >> ошибка валидации >> ожидается 400 или 405
    @Test
    @DisplayName("Создание питомца с отсутствующими обязательными полями >> ошибка валидации >> ожидается 400 или 405")
    public void createPet_withEmptyJsonBody() {
        petClient.createPetRaw("{}", ContentType.JSON)
                .then()
                .spec(invalidBodyResponse);
    }

    @Test
    @DisplayName("Создание питомца с указанием неверного Content-Type >> ошибка валидации >> ожидается 400 или 405")
    public void createPet_notJsonContentType() {
        petClient.createPetRaw(VALID_JSON, ContentType.TEXT)
                .then()
                .spec(contentTypeMismatchResponse);
    }

    // Отсутствует обязательное поле name >> ошибка валидации >> ожидается 400 или 405
    @Test
    @DisplayName("Создание питомца без обязательного поля name >> ошибка валидации >> ожидается 400 или 405")
    public void createPet_withoutName() {
        Pet pet = PetFactory.randomPetFullData()
                .setName(null);
        petClient
                .createPet(pet)
                .then()
                .spec(invalidBodyResponse);
    }

    @Test
    @DisplayName("Создание питомца без обязательного поля photoUrls >> ошибка валидации >> ожидается 400 или 405")
    public void createPet_withoutPhotoUrls() {
        Pet pet = PetFactory.randomPetFullData()
                .setPhotoUrls(null);
        petClient
                .createPet(pet)
                .then()
                .spec(invalidBodyResponse);
    }

    @Test
    @DisplayName("Создание питомца с нарушенной структурой json >> ошибка валидации структуры >> ожидается 400 или 405")
    public void createPet_withStringPhotoUrls() {
        petClient.createPetRaw(BAD_STRUCTURE_JSON, ContentType.JSON)
                .then()
                .spec(invalidBodyResponse);
    }

    @Test
    @DisplayName("Создание питомца с дублирующим id >> конфликт ресурса >> ожидается 400 или 409")
    public void createPet_withDuplicateId() {
        Pet pet = PetFactory.randomPetFullData();
        Response response = petClient.createPet(pet);
        Long id = response.then().extract().body().path("id");
        createdPetIds.add(id);
        pet.setId(id);

        Pet duplicatePet = PetFactory.randomPetFullData()
                .setId(id);

        petClient
                .createPet(duplicatePet)
                .then()
                .spec(duplicateIdResponse);
    }

    // GET

    @Test
    @DisplayName("получение питомца без передачи id в URL >> некорректный endpoint >> ожидается 400 или 405")
    public void getPetById_withoutId() {
        petClient.getPetByIdRaw(null, ContentType.JSON)
                .then()
                .spec(missingIdOrWrongPathResponse);
    }

    @Test
    @DisplayName("Получение питомца по валидному, но несуществующему id >> ресурс не найден >> ожидается 404")
    public void getPetById_unexistentPet() {
        petClient
                .getPetById(UNEXISTENT_ID)
                .then()
                .spec(notFoundResponse);
    }

    @Test
    @DisplayName("Получение питомца по невалидному id (строка вместо числа) >> ошибка формата >> ожидается 400 или 404")
    public void getPetById_invalidIdString() {
        petClient.getPetByIdRaw(INVALID_ID, ContentType.JSON)
                .then()
                .spec(invalidIdResponse);
    }

    // UPDATE

    @Test
    @DisplayName("Обновление питомца без передачи id в body >> ошибка валидации >> ожидается 400")
    public void updatePet_withoutId() {
        Pet pet = PetFactory.randomPetFullData();
        petClient
                .updatePet(pet)
                .then()
                .spec(badRequestResponse);
    }

    @Test
    @DisplayName("Обновление питомца с передачей валидного, но несуществующего id >> ресурс не найден >> ожидается 404")
    public void updatePet_unexistentId() {
        Pet pet = PetFactory.randomPetFullData()
                .setId(UNEXISTENT_ID);
        petClient
                .updatePet(pet)
                .then()
                .spec(notFoundResponse);
    }

    @Test
    @DisplayName("Обновление питомца с передачей невалидного id (строка вместо числа) >> ошибка формата >> ожидается 400 или 404")
    public void updatePet_invalidId() {
        petClient.updatePetRaw(VALID_JSON_INVALID_ID, ContentType.JSON)
                .then()
                .spec(badRequestResponse);
    }

    @Test
    @DisplayName("Обновление питомца без передачи body >> ошибка валидации >> ожидается 400 или 405")
    public void updatePet_withoutBody() {
        petClient.updatePetRaw(null, ContentType.JSON)
                .then()
                .spec(invalidBodyResponse);
    }

    @Test
    @DisplayName("Обновление питомца с передачей пустого json body >> ошибка валидации >> ожидается 400 или 405")
    public void updatePet_withEmptyBody() {
        petClient.updatePetRaw("", ContentType.JSON)
                .then()
                .spec(invalidBodyResponse);
    }

    @Test
    @DisplayName("Обновление питомца с передачей некорректного json body >> ошибка парсинга >> ожидается 400 или 405")
    public void updatePet_withBadStructureJsonBody() {
        petClient.updatePetRaw(BAD_JSON, ContentType.JSON)
                .then()
                .spec(invalidBodyResponse);
    }

    @Test
    @DisplayName("Обновление питомца с нарушенной структурой json body >> ошибка валидации структуры >> ожидается 400 или 405")
    public void updatePet_withStringPhotoUrls() {
        petClient.updatePetRaw(BAD_STRUCTURE_JSON, ContentType.JSON)
                .then()
                .spec(invalidBodyResponse);
    }

    @Test
    @DisplayName("Обновление питомца с указанием неверного Content-Type >> неподдерживаемый формат >> ожидается 405 или 415")
    public void updatePet_notJsonContentType() {
        petClient.updatePetRaw(VALID_JSON, ContentType.TEXT)
                .then()
                .spec(contentTypeMismatchResponse);
    }

    // REMOVE

    @Test
    @DisplayName("Удаление питомца без передачи id в URL >> некорректный endpoint >> ожидается 400 или 405")
    public void removePet_withoutId() {
        petClient.removePetRaw(null, ContentType.JSON)
                .then()
                .spec(missingIdOrWrongPathResponse);
    }

    @Test
    @DisplayName("Удаление питомца с передачей валидного, но несуществующего id >> ресурс не найден >> ожидается 404")
    public void removePet_unexistentId() {
        petClient
                .removePet(UNEXISTENT_ID)
                .then()
                .spec(notFoundResponse);
    }

    @Test
    @DisplayName("Удаление питомца с передачей невалидного id (строка вместо числа) >> ошибка формата >> ожидается 400 или 404")
    public void removePet_invalidId() {
        petClient.removePetRaw(INVALID_ID, ContentType.JSON)
                .then()
                .spec(invalidIdResponse);
    }
}
