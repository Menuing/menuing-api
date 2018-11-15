/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menuing.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Embeddable
public class TasteAllergyKey {
    
    @Column(name = "user_id")
    private User user;

    @Column(name = "ingredient_id")
    private Ingredient ingredient;
}
