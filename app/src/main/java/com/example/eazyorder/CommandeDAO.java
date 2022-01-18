package com.example.eazyorder;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

/**
 * Interface représentant les requêtes utilisable sur la base de données
 */

//Annotation pour indiquer que l'interface est un DAO (Data Access Objects)
@Dao
public interface CommandeDAO {

    //Requête d'ajout d'une commande dans la bd
    @Insert
    public void insertCommande(Commande commande);

    //Requête de modification d'une commande existante dans la bd
    @Update
    public void updateCommande(Commande commande);

    //Requête de suppression d'une commande existante dans la bd
    @Delete
    public void deleteCommande(Commande commande);

    //Requête qui récupère l'ensemble des commandes existante dans la base de données
    @Query("SELECT * FROM commande")
    public Cursor getAllCommandes();

    //Requête qui récupère une commande existante selon son identifiant
    @Query("SELECT * FROM commande WHERE _id =:id")
    public Commande getCommande(int id);


}
