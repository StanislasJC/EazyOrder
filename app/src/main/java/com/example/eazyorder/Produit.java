package com.example.eazyorder;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//Annotation pour la table produit de la base de donnée. Nom de la table différent du nom de la classe. Modifiable avec tableName
@Entity(tableName = "produit")
public class Produit {

    //Attributs

    //Annotations pour préciser que l'attribut id est la clé primaire, qu'elle s'au incrémente et qu'elle est non nulle
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int _id;

    private String nom;

    private String prix;

    private String commandeid;

    //Accesseurs

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public String getCommandeid() {
        return commandeid;
    }

    public void setCommandeid(String commandeid) {
        this.commandeid = commandeid;
    }
}
