package api.models.pet;

import java.util.Objects;

public class PetTag {
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public PetTag setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public PetTag setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "PetTag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }


}