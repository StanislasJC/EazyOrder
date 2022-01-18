package com.example.eazyorder;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ProduitDAO {

    //Requête d'ajout d'un produit dans la bd
    @Insert
    public void insertProduit(Produit produit);

    //Requête de modification d'un existante dans la bd
    @Update
    public void updateProduit(Produit produit);

    //Requête de suppression d'un produit existant dans la bd
    @Delete
    public void deleteProduit(Produit produit);

    //Requête qui récupère l'ensemble des produits existants dans la base de données
    @Query("SELECT * FROM produit")
    public Cursor getAllProduct();

    //Requête qui récupère un produit existant selon son identifiant
    @Query("SELECT * FROM produit WHERE commandeid =:id")
    public Cursor getProduit(String id);

    //Requête qui récupère un produit existant selon son identifiant
    @Query("SELECT * FROM produit WHERE _id =:id")
    public Produit getProduitId(String id);

}
