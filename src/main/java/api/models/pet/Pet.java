package api.models.pet;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Pet {
    private Long id;
    private PetCategory category;
    private String name;
    private List<String> photoUrls;
    private List<PetTag> tags;
    private PetStatus status;

    public Long getId() {
        return id;
    }

    public Pet setId(Long id) {
        this.id = id;
        return this;
    }

    public PetCategory getCategory() {
        return category;
    }

    public Pet setCategory(PetCategory category) {
        this.category = category;
        return this;
    }

    public String getName() {
        return name;
    }

    public Pet setName(String name) {
        this.name = name;
        return this;
    }

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public Pet setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
        return this;
    }

    public List<PetTag> getTags() {
        return tags;
    }

    public Pet setTags(List<PetTag> tags) {
        this.tags = tags;
        return this;
    }

    public PetStatus getStatus() {
        return status;
    }

    public Pet setStatus(PetStatus status) {
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", category=" + category +
                ", name='" + name + '\'' +
                ", photoUrls=" + photoUrls +
                ", tags=" + tags +
                ", status=" + status +
                '}';
    }

}