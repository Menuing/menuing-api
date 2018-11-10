package menuing.entity;

import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@Entity
@Table(name = "recipe")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQuery(name = Recipe.FIND_ALL, query = "select g from Recipe g")
public class Recipe {
    
    public static final String FIND_ALL = "findAllRecipe";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlTransient
    private Long id;
    
    @NotNull
    private String name;
    
    @NotNull
    private String instructions;
    
    @NotNull
    private String ingredients;
    
    private String urlPhoto = "";
    
    private float averagePuntuation = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
    
    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }
    
    public float getAveragePuntuation() {
        return averagePuntuation;
    }

    public void setAveragePuntuation(float averagePuntuation) {
        this.averagePuntuation = averagePuntuation;
    }


    @Override
    public String toString() {
        return new StringBuilder("Recipe [")
                .append(id).append(", ")
                .append(name).append(", ")
                .append(instructions).append(", ")
                .append(ingredients).append(", ")
                .append(urlPhoto).append(", ")
                .append(averagePuntuation).append("]").toString();
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("id", this.id)
                .add("name", this.name)
                .add("instructions", this.instructions)
                .add("ingredients", this.ingredients)
                .add("url", this.urlPhoto)
                .add("average_puntuation", String.valueOf(this.averagePuntuation)
                )
                .build();
    }
}
