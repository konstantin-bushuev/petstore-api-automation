package api.clients;

import api.http.RequestHelper;
import api.models.pet.Pet;
import api.models.pet.PetStatus;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static api.spec.RequestSpecs.baseSpec;
import static io.restassured.RestAssured.given;

public class PetClient {

    private static final String PET_PATH = "/pet";
    private static final String PET_BY_ID = "/pet/{petId}";
    private static final String PET_BY_STATUS = "/pet/findByStatus";

    public Response createPet(Pet pet) {
        return given()
                .spec(baseSpec)
                .body(pet)
                .when()
                .post(PET_PATH);
    }

    public Response getPetById(Long id) {
        return given()
                .spec(baseSpec)
                .pathParam("petId", id)
                .when()
                .get(PET_BY_ID);
    }

    public Response updatePet(Pet pet) {
        return given()
                .spec(baseSpec)
                .body(pet)
                .when()
                .put(PET_PATH);
    }

        public Response removePet(Long id) {
        return given()
                .spec(baseSpec)
                .pathParam("petId", id)
                .when()
                .delete(PET_BY_ID);
    }

    public Response findPetsByStatus(PetStatus status) {
        return given()
                .spec(baseSpec)
                .queryParam("status", status.toString())
                .when()
                .get(PET_BY_STATUS);
    }

    //  Методы для нестандартных, негативных кейсов

    // Создание питомца с произвольным телом
    public Response createPetRaw(Object body, ContentType contentType) {
        return RequestHelper.post(PET_PATH, body, contentType);
    }

    // Получение питомца по id (raw)
    public Response getPetByIdRaw(Object petId, ContentType contentType) {
        return RequestHelper.getWithPathParam(PET_BY_ID, "petId", petId, contentType);
    }

    // Обновление питомца с произвольным телом
    public Response updatePetRaw(Object body, ContentType contentType) {
        return RequestHelper.put(PET_PATH, body, contentType);
    }

    // Удаление питомца по id (raw)
    public Response removePetRaw(Object petId, ContentType contentType) {
        return RequestHelper.delete(PET_BY_ID, "petId", petId, contentType);
    }

    // Поиск питомцев по статусу (raw)
    public Response findPetsByStatusRaw(Object status, ContentType contentType) {
        return RequestHelper.getWithQueryParam(PET_BY_STATUS, "status", status, contentType);
    }
}
