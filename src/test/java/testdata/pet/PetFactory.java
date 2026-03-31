package testdata.pet;

import api.models.pet.*;
import net.datafaker.Faker;

import java.util.List;

public class PetFactory {

    private static final Faker faker = new Faker();

    public static Pet randomPet() {
        return new Pet()
                .setName(faker.funnyName().name())
                .setPhotoUrls(List.of(faker.internet().url()));
    }

    public static Pet randomPetFullData() {
        return new Pet()
                .setName(faker.funnyName().name())
                .setPhotoUrls(List.of(
                        faker.internet().url(),
                        faker.internet().url()
                ))
                .setCategory(new PetCategory()
                        .setName(faker.lorem().word())
                        .setId((long) faker.number().positive())
                )
                .setStatus(PetStatus.available)
                .setTags(List.of(
                        new PetTag()
                                .setName(faker.lorem().word())
                                .setId((long) faker.number().positive()),
                        new PetTag()
                                .setName(faker.lorem().word())
                                .setId((long) faker.number().positive())
                ));
    }
}
