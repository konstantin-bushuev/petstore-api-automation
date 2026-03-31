package tests.pet;

import api.clients.PetClient;
import api.models.pet.Pet;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
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
        // Удаляем созданные сущности
        for (Long id : createdPetIds) {
            petClient.removePet(id);
        }
    }


    // CREATE

    // Передан запрос без тела >> ошибка валидации >> ожидается 400 или 405
    @Test
    public void createPet_withoutBody() {
        petClient.createPetRaw(null, ContentType.JSON)
                .then()
                .spec(invalidBodyResponse);
    }

    // Передан пустой JSON >> ошибка валидации >> ожидается 400 или 405
    @Test
    public void createPet_withEmptyBody() {
        petClient.createPetRaw("", ContentType.JSON)
                .then()
                .spec(invalidBodyResponse);
    }

    // Передан некорректный JSON >> ошибка парсинга >> ожидается 400 или 405
    @Test
    public void createPet_withBadJsonBody() {
        petClient.createPetRaw(BAD_JSON, ContentType.JSON)
                .then()
                .spec(invalidBodyResponse);
    }

    // Отсутствуют обязательные поля >> ошибка валидации >> ожидается 400 или 405
    @Test
    public void createPet_withEmptyJsonBody() {
        petClient.createPetRaw("{}", ContentType.JSON)
                .then()
                .spec(invalidBodyResponse);
    }

    // Неверный Content-Type >> неподдерживаемый формат >> ожидается 405 или 415
    @Test
    public void createPet_notJsonContentType() {
        petClient.createPetRaw(VALID_JSON, ContentType.TEXT)
                .then()
                .spec(contentTypeMismatchResponse);
    }

    // Отсутствует обязательное поле name >> ошибка валидации >> ожидается 400 или 405
    @Test
    public void createPet_withoutName() {
        Pet pet = PetFactory.randomPetFullData()
                .setName(null);
        petClient
                .createPet(pet)
                .then()
                .spec(invalidBodyResponse);
    }

    // Отсутствует обязательное поле photoUrls >> ошибка валидации >> ожидается 400 или 405
    @Test
    public void createPet_withoutPhotoUrls() {
        Pet pet = PetFactory.randomPetFullData()
                .setPhotoUrls(null);
        petClient
                .createPet(pet)
                .then()
                .spec(invalidBodyResponse);
    }

    // Нарушена структура JSON >> ошибка валидации структуры >> ожидается 400 или 405
    @Test
    public void createPet_withStringPhotoUrls() {
        petClient.createPetRaw(BAD_STRUCTURE_JSON, ContentType.JSON)
                .then()
                .spec(invalidBodyResponse);
    }

    // Дублирующийся id >> конфликт ресурса >> ожидается 400 или 409
    @Test
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

    // Отсутствует id в URL >> некорректный endpoint >> ожидается 400 или 405
    @Test
    public void getPetById_withoutId() {
        petClient.getPetByIdRaw(null, ContentType.JSON)
                .then()
                .spec(missingIdOrWrongPathResponse);
    }

    // Валидный, но несуществующий id >> ресурс не найден >> ожидается 404
    @Test
    public void getPetById_unexistentPet() {
        petClient
                .getPetById(UNEXISTENT_ID)
                .then()
                .spec(notFoundResponse);
    }

    // Невалидный id (строка вместо числа) >> ошибка формата >> ожидается 400 или 404
    @Test
    public void getPetById_invalidIdString() {
        petClient.getPetByIdRaw(INVALID_ID, ContentType.JSON)
                .then()
                .spec(invalidIdResponse);
    }

    // UPDATE

    // Отсутствует id в теле >> ошибка валидации >> ожидается 400
    @Test
    public void updatePet_withoutId() {
        Pet pet = PetFactory.randomPetFullData();
        petClient
                .updatePet(pet)
                .then()
                .spec(badRequestResponse);
    }

    // Валидный, но несуществующий id >> ресурс не найден >> ожидается 404
    @Test
    public void updatePet_unexistentId() {
        Pet pet = PetFactory.randomPetFullData()
                .setId(UNEXISTENT_ID);
        petClient
                .updatePet(pet)
                .then()
                .spec(notFoundResponse);
    }

    // Невалидный id (строка вместо числа) >> ошибка формата >> ожидается 400 или 404
    @Test
    public void updatePet_invalidId() {
        petClient.updatePetRaw(VALID_JSON_INVALID_ID, ContentType.JSON)
                .then()
                .spec(badRequestResponse);
    }

    // Передан запрос без тела >> ошибка валидации >> ожидается 400 или 405
    @Test
    public void updatePet_withoutBody() {
        petClient.updatePetRaw(null, ContentType.JSON)
                .then()
                .spec(invalidBodyResponse);
    }

    // Передан пустой JSON >> ошибка валидации >> ожидается 400 или 405
    @Test
    public void updatePet_withEmptyBody() {
        petClient.updatePetRaw("", ContentType.JSON)
                .then()
                .spec(invalidBodyResponse);
    }

    // Некорректный JSON >> ошибка парсинга >> ожидается 400 или 405
    @Test
    public void updatePet_withBadStructureJsonBody() {
        petClient.updatePetRaw(BAD_JSON, ContentType.JSON)
                .then()
                .spec(invalidBodyResponse);
    }

    // Нарушена структура JSON >> ошибка валидации структуры >> ожидается 400 или 405
    @Test
    public void updatePet_withStringPhotoUrls() {
        petClient.updatePetRaw(BAD_STRUCTURE_JSON, ContentType.JSON)
                .then()
                .spec(invalidBodyResponse);
    }

    // Неверный Content-Type >> неподдерживаемый формат >> ожидается 405 или 415
    @Test
    public void updatePet_notJsonContentType() {
        petClient.updatePetRaw(VALID_JSON, ContentType.TEXT)
                .then()
                .spec(contentTypeMismatchResponse);
    }

    // REMOVE

    // Отсутствует id в URL >> некорректный endpoint >> ожидается 400 или 405
    @Test
    public void removePet_withoutId() {
        petClient.removePetRaw(null, ContentType.JSON)
                .then()
                .spec(missingIdOrWrongPathResponse);
    }

    // Валидный, но несуществующий id >> ресурс не найден >> ожидается 404
    @Test
    public void removePet_unexistentId() {
        petClient
                .removePet(UNEXISTENT_ID)
                .then()
                .spec(notFoundResponse);
    }

    // Невалидный id (строка вместо числа) >> ошибка формата >> ожидается 400 или 404
    @Test
    public void removePet_invalidId() {
        petClient.removePetRaw(INVALID_ID, ContentType.JSON)
                .then()
                .spec(invalidIdResponse);
    }
}
