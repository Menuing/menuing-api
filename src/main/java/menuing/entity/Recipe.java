package menuing.entity;

import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
    
    public static final String FIND_ALL = "findAllRecipes";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlTransient
    private Long id;
    
    @NotNull
    private String name;
    
    @Column(length=10000)
    private String instructions;
    
    @Column(length=10000)
    private String proportions;
    
    private float calories;
    
    private float fat;
    
    private float protein;
    
    private float sodium;
    
    private String urlPhoto = "";
    
    private float averagePuntuation = 0;
    
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="recipe")
    private List<RecipeIngredient> recipeIngredient;

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
    
    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public float getFat() {
        return fat;
    }

    public void setFat(float fat) {
        this.fat = fat;
    }

    public float getProtein() {
        return protein;
    }

    public void setProtein(float protein) {
        this.protein = protein;
    }

    public float getSodium() {
        return sodium;
    }

    public void setSodium(float sodium) {
        this.sodium = sodium;
    }


    @Override
    public String toString() {
        return new StringBuilder("Recipe [")
                .append(id).append(", ")
                .append(name).append(", ")
                .append(instructions).append(", ")
                .append(proportions).append(", ")
                .append(calories).append(", ")
                .append(sodium).append(", ")
                .append(fat).append(", ")
                .append(protein).append(", ")
                .append(urlPhoto).append(", ")
                .append(averagePuntuation).append("]").toString();
    }

    public JsonObject toJson() {
        System.out.println(this.instructions);
        return Json.createObjectBuilder()
                .add("id", this.id)
                .add("name", this.name)
                .add("instructions", this.instructions)
                .add("proportions", this.proportions)
                .add("calories", String.valueOf(this.calories))
                .add("sodium", String.valueOf(this.sodium))
                .add("fat", String.valueOf(this.fat))
                .add("protein", String.valueOf(this.protein))
                .add("urlPhoto", this.urlPhoto)
                .add("averagePuntuation", String.valueOf(this.averagePuntuation)
                )
                .build();
    }
}
