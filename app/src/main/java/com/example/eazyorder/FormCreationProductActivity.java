package com.example.eazyorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class FormCreationProductActivity extends AppCompatActivity {

    //Attributs

    //Champ texte nom
    TextInputLayout nomTF;
    TextInputEditText nomTEF;

    //Champ texte prix
    TextInputLayout prixTF;
    TextInputEditText prixTEF;

    //Bouton
    Button addProductButton;

    //Identifiant de la commande
    String id;

    //BD
    AppDataBase appDataBase;
    ProduitDAO produitDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_creation_product);

        //Récupération de l'id de la commande
        id = getIntent().getStringExtra("id");

        //Attributs

        //Nom
        nomTF = findViewById(R.id.nomTextField);
        nomTEF = findViewById(R.id.nomTextInputEditField);

        //Prix
        prixTF = findViewById(R.id.prixTextField);
        prixTEF = findViewById(R.id.prixTextInputEditField);

        //Bouton ajout produit
        addProductButton = findViewById(R.id.addProductBtn);

        //Listeneur du bouton d'ajout d'un produit à une commande
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nom = nomTEF.getText().toString();
                String prix = prixTEF.getText().toString();
                if(checkNom(nom) && checkPrix(prix)){

                    //Thread
                    new Thread(

                            //Expression lambda
                            ()->{
                                //Accès à la BD
                                accessDataBase();

                                // Objet/Entité à ajouter
                                Produit c = new Produit();

                                //Attributs qui constitue le produit
                                c.setNom(nomTEF.getText().toString());
                                c.setPrix(prixTEF.getText().toString());
                                c.setCommandeid(id);

                                //Ajout du produit dans la base de données
                                produitDAO.insertProduit(c);

                                //Préparation du lancement de l'activité détail commande
                                Intent i = new Intent(FormCreationProductActivity.this, DetailOrderActivity.class);

                                //Ajout de l'id comme information supplémentaire à faire passer à l'activité à lancer.
                                i.putExtra("id",id);

                                //Lancement de l'activité
                                startActivity(i);

                            }

                    ).start();

                }

            }
        });
    }

    /**
     * Vérifie si le nom d'un produit contient au moins 3 caractères
     * @return true si contient au moins 3 caractère, false sinon
     */
    public boolean checkNom(String s){

        if(s.length()>=3){
            return true;
        }else{
            nomTF.setError("Nombre de caractères inférieur à 3");
            return false;
        }
    }

    public boolean checkPrix(String s){

        if(s.length()>=1){
            return true;
        }else{
            prixTF.setError("Nombre de caractères inférieur à 1");
            return false;
        }
    }



    /**
     * Méhtode d'accès à la base de données
     */
    public void accessDataBase(){

        //Récupération de la BD
        appDataBase = AppDataBase.getAppDataBase(FormCreationProductActivity.this);
        //Data access object de l'entité/objet produit
        produitDAO = appDataBase.produitDAO();


    }
}