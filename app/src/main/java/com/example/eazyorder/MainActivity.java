package com.example.eazyorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //Attributs
    ListView ls;
    Button addOrderBtn;

    //BD
    AppDataBase appDataBase;
    CommandeDAO commandeDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialisation des attributs
        ls = findViewById(R.id.list);
        addOrderBtn = findViewById(R.id.addOrderBtn);
        addOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openForm();
            }
        });

        //Création d'un thread pour gérer la relation avec la BD
        new Thread(
                ()->{
                    //Accès à la base de données
                    accessDataBase();
                    //Création d'un curseur qui contient les commandes existantes dans la base de données
                    Cursor c = commandeDAO.getAllCommandes();

                    //Thread qui est lancé sur l'interface utilisateur
                    runOnUiThread(()->{

                        //Adaptateur qui permet de créer un item de la listview contenant l'id (caché), la référence et la date de la commande
                        SimpleCursorAdapter adapter = new SimpleCursorAdapter(MainActivity.this,R.layout.item_order,c,
                                new String[]{c.getColumnName(0),c.getColumnName(1),c.getColumnName(2)},
                                new int[]{R.id.idLV,R.id.referenceLV,R.id.dateLV},1);

                        //ajout de l'adaptateur dans la listview
                        ls.setAdapter(adapter);

                        //Listeneur sur les items de la listview.
                        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                //Récupération de l'id de la commande
                                TextView t = view.findViewById(R.id.idLV);

                                //Intent de l'activité à lancer après avoir appuyé sur l'item correspondant à une commande
                                Intent intent = new Intent(getApplicationContext(), DetailOrderActivity.class);
                                //Ajout de l'id comme information supplémentaire à faire passer à l'activité à lancer.
                                intent.putExtra("id",t.getText().toString());
                                //Lancement de l'activité
                                startActivity(intent);
                            }
                        });

                    });

                }

        ).start();




    }

    /**
     * Méthode d'accès à la base de données
     */
    public void accessDataBase(){

         appDataBase = AppDataBase.getAppDataBase(MainActivity.this);
         commandeDAO = appDataBase.commandeDAO();


    }

    /**
     * Méthode d'accès à l'activité formulaire de création d'une commande
     */
    public void openForm() {
        Intent intent = new Intent(this,FormCreationOrderActivity.class);
        startActivity(intent);
    }


}