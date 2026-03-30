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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PetTag)) return false;
        PetTag that = (PetTag) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}