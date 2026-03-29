package api.models.pet;

import java.util.Objects;

public class PetCategory {

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public PetCategory setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public PetCategory setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "PetCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }


}
