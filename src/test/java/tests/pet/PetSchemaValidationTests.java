package tests.pet;

import api.clients.PetClient;
import api.models.pet.Pet;
import api.models.pet.PetStatus;
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

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class PetSchemaValidationTests extends BaseTest {

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

    @Test
    @DisplayName("Проверка соответствия ответа схеме json при создании питомца")
    public void createPetResponseSchemaTest() {
        Pet pet = PetFactory.randomPetFullData();
        Response response =  petClient.createPet(pet);
        response
                .then()
                .contentType(ContentType.JSON)
                .body(matchesJsonSchemaInClasspath("schemas/pet/createPetResponse.json"));

        Long id = response.jsonPath().getLong("id");
        createdPetIds.add(id);
    }

    @Test
    @DisplayName("Проверка соответствия ответа схеме json при получении питомца по id")
    public void getPetResponseSchemaTest() {
        Pet pet = PetFactory.randomPetFullData();
        Response createResponse = petClient.createPet(pet);

        Long id = createResponse.jsonPath().getLong("id");
        createdPetIds.add(id);

        petClient
                .getPetById(id)
                .then()
                .contentType(ContentType.JSON)
                .body(matchesJsonSchemaInClasspath("schemas/pet/getPetResponse.json"));
    }

    @Test
    @DisplayName("Проверка соответствия ответа схеме json при обновлении питомца")
    public void updatePetResponseSchemaTest() {
        Pet pet = PetFactory.randomPetFullData();
        Response createResponse = petClient.createPet(pet);

        Long id = createResponse.jsonPath().getLong("id");
        createdPetIds.add(id);

        Pet updatePet = PetFactory.randomPetFullData()
                .setId(id);

        petClient
                .updatePet(updatePet)
                .then()
                .contentType(ContentType.JSON)
                .body(matchesJsonSchemaInClasspath("schemas/pet/updatePetResponse.json"));
    }

    @Test
    @DisplayName("Проверка соответствия ответа схеме json при поиске питомцев по статусу")
    public void findPetByStatusResponseSchemaTest() {
        Pet pet = PetFactory.randomPetFullData()
                .setStatus(PetStatus.available);

        Response createResponse = petClient.createPet(pet);

        Long id = createResponse.jsonPath().getLong("id");
        createdPetIds.add(id);

        petClient
                .findPetsByStatus(PetStatus.available)
                .then()
                .contentType(ContentType.JSON)
                .body(matchesJsonSchemaInClasspath("schemas/pet/findPetByStatusResponse.json"));
    }
}