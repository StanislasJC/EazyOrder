package com.example.eazyorder;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Classe Objet/Entité Commande. Contient les attributs et les accesseurs définissant une commande.
 */

//Annotation pour la table commande de la base de donnée. Nom de la table différent du nom de la classe. Modifiable avec tableName
@Entity(tableName = "commande")
public class Commande {

    //Attributs

    //Annotations pour préciser que l'attribut id est la clé primaire, qu'elle s'au incrémente et qu'elle est non nulle
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int _id;

    private String reference;

    private String date;

    private String description;

    //Accesseurs

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
