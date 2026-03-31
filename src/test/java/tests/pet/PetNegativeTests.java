package tests.pet;

import api.clients.pet.PetClient;
import api.models.pet.Pet;
import api.utils.RequestHelper;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import testdata.PetFactory;
import tests.BaseTest;

import java.util.ArrayList;
import java.util.List;

import static api.spec.ResponseSpecs.*;
import static testdata.PetNegativeData.*;

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
        RequestHelper.post("/pet", null, ContentType.JSON)
                .then()
                .spec(invalidBodyResponse);
    }

    // Передан пустой JSON >> ошибка валидации >> ожидается 400 или 405
    @Test
    public void createPet_withEmptyBody() {
        RequestHelper.post("/pet", "", ContentType.JSON)
                .then()
                .spec(invalidBodyResponse);
    }

    // Передан некорректный JSON >> ошибка парсинга >> ожидается 400 или 405
    @Test
    public void createPet_withBadJsonBody() {
        RequestHelper.post("/pet", BAD_JSON, ContentType.JSON)
                .then()
                .spec(invalidBodyResponse);
    }

    // Отсутствуют обязательные поля >> ошибка валидации >> ожидается 400 или 405
    @Test
    public void createPet_withEmptyJsonBody() {
        RequestHelper.post("/pet", "{}", ContentType.JSON)
                .then()
                .spec(invalidBodyResponse);
    }

    // Неверный Content-Type >> неподдерживаемый формат >> ожидается 405 или 415
    @Test
    public void createPet_notJsonContentType() {
        RequestHelper.post("/pet", VALID_JSON, ContentType.TEXT)
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
        RequestHelper.post("/pet", BAD_STRUCTURE_JSON, ContentType.JSON)
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

        Pet dublicatePet = PetFactory.randomPetFullData()
                .setId(id);

        petClient
                .createPet(dublicatePet)
                .then()
                .spec(duplicateIdResponse);
    }

    // GET

    // Отсутствует id в URL >> некорректный endpoint >> ожидается 400 или 405
    @Test
    public void getPetById_withoutId() {
        RequestHelper.get("/pet", ContentType.JSON)
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
        RequestHelper.getWithPathParam("/pet/{petId}", "petId", INVALID_ID, ContentType.JSON)
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
        RequestHelper.put("/pet", VALID_JSON_INVALID_ID, ContentType.JSON)
                .then()
                .spec(badRequestResponse);
    }

    // Передан запрос без тела >> ошибка валидации >> ожидается 400 или 405
    @Test
    public void updatePet_withoutBody() {
        RequestHelper.put("/pet", null, ContentType.JSON)
                .then()
                .spec(invalidBodyResponse);
    }

    // Передан пустой JSON >> ошибка валидации >> ожидается 400 или 405
    @Test
    public void updatePet_withEmptyBody() {
        RequestHelper.put("/pet", "", ContentType.JSON)
                .then()
                .spec(invalidBodyResponse);
    }

    // Некорректный JSON >> ошибка парсинга >> ожидается 400 или 405
    @Test
    public void updatePet_withBadStructureJsonBody() {
        RequestHelper.put("/pet", BAD_JSON, ContentType.JSON)
                .then()
                .spec(invalidBodyResponse);
    }

    // Нарушена структура JSON >> ошибка валидации структуры >> ожидается 400 или 405
    @Test
    public void updatePet_withStringPhotoUrls() {
        RequestHelper.put("/pet", BAD_STRUCTURE_JSON, ContentType.JSON)
                .then()
                .spec(invalidBodyResponse);
    }

    // Неверный Content-Type >> неподдерживаемый формат >> ожидается 405 или 415
    @Test
    public void updatePet_notJsonContentType() {
        RequestHelper.put("/pet", VALID_JSON, ContentType.TEXT)
                .then()
                .spec(contentTypeMismatchResponse);
    }

    // REMOVE

    // Отсутствует id в URL >> некорректный endpoint >> ожидается 400 или 405
    @Test
    public void removePet_withoutId() {
        RequestHelper.delete("/pet", null, null, ContentType.JSON)
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
        RequestHelper.delete("/pet/{petId}", "petId", INVALID_ID, ContentType.JSON)
                .then()
                .spec(invalidIdResponse);
    }
}
