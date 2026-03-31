package api.clients.pet;

import api.models.pet.Pet;
import api.models.pet.PetStatus;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static api.config.RestConfig.BASE_URL;
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
}
