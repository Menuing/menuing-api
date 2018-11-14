package menuing.entity;

import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "nutritionist")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQuery(name = Nutritionist.FIND_ALL, query = "select g from Nutritionist g")
public class Nutritionist extends User{
    
    public static final String FIND_ALL = "findAllNutritionists";

    private float averagePuntuation = 0;
    
    @NotNull
    private String speciallity;
    
    @NotNull
    private String dni;
    
    @NotNull
    private String phoneNumber;

    
    public float getAveragePuntuation() {
        return averagePuntuation;
    }

    public void setAveragePuntuation(float averagePuntuation) {
        this.averagePuntuation = averagePuntuation;
    }

    public String getSpeciallity() {
        return speciallity;
    }

    public void setSpeciallity(String speciallity) {
        this.speciallity = speciallity;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    @Override
    public String toString() {
        return new StringBuilder("Nutritionist [")
                .append(id).append(", ")
                .append(username).append(", ")
                .append(password).append(", ")
                .append(isPremium).append(", ")
                .append(averagePuntuation).append(", ")
                .append(speciallity).append(", ")
                .append(dni).append(", ")
                .append(phoneNumber).append("]").toString();
    }

    @Override
    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("id", this.id)
                .add("username", this.username)
                .add("password", this.password)
                .add("isPremium", String.valueOf(this.isPremium))
                .add("averagePuntuation", String.valueOf(this.averagePuntuation))
                .add("speciallity", this.speciallity)
                .add("dni", this.dni)
                .add("phoneNumber", this.phoneNumber)
                .build();
    }
}
