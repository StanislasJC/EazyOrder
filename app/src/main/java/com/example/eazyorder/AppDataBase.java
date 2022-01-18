package com.example.eazyorder;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Classe abstraite qui permet de créer et de se connecter à la base de données.
 */

//Annotation pour les différentes tables de la base de données. ////////// NE PAS OUBLIER D'AJOUTER LES TABLES PRODUIT ET CONTIENT \\\\\\\\\\\
@Database(entities = {Commande.class,Produit.class}, version = 5)
public abstract class AppDataBase extends RoomDatabase {

    //Instance de la base de données
    private static AppDataBase INSTANCE;

    //Instance commandeDAO
    public abstract CommandeDAO commandeDAO();

    //Instance produitDAO
    public abstract ProduitDAO produitDAO();


    //Récupération de la base de données
    public static AppDataBase getAppDataBase(Context context){

        //Si aucune instance de la base de données existe, Nous en créeons une avec databaseBuilder
        if(INSTANCE==null){

            synchronized (AppDataBase.class){

                if(INSTANCE==null){

                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),AppDataBase.class,"OrderManager").build();
                }
            }
        }
        //Nous retournons l'instance de la base de données
        return INSTANCE;
    }
}
