package com.example.eazyorder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class DetailOrderActivity extends AppCompatActivity {

    //Référence
    private TextInputLayout referenceTF;
    private TextInputEditText referenceTEF;

    //Date
    private DatePickerDialog datePickerDialog;
    private Button dateButton;

    //Description
    private TextInputLayout descriptionTF;
    private TextInputEditText descriptionTEF;

    //ListView
    private ListView ls;

    //Bouton confirmation et signature
    private Button modificationButton;
    private Button deleteButton;
    private Button addProductButton;

    //Base de données
    AppDataBase appDataBase;
    CommandeDAO commandeDAO;
    ProduitDAO produitDAO;

    //Informations commande
    String id;
    String date;
    Commande c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);

        //Référence
        referenceTF = findViewById(R.id.referenceTextField);
        referenceTEF = findViewById(R.id.referenceTextInputEditField);

        //Date
        dateButton = findViewById(R.id.datePickerButton);
        initDatePicker();
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker();
            }
        });

        //Description
        descriptionTF = findViewById(R.id.descriptionTextField);
        descriptionTEF = findViewById(R.id.descriptionTextInputEditField);

        //Bouton de modification et de suppression
        modificationButton = findViewById(R.id.modificationButton);
        deleteButton = findViewById(R.id.deleteButton);
        addProductButton = findViewById(R.id.addProductBtn);



        //Récupération de l'id de la commande
        id = getIntent().getStringExtra("id");
        ls = findViewById(R.id.list);



        //Thread qui récupère la commande que l'utilisateur a selectionné dans la listview
        new Thread(

                ()->{
                    //Accès à la BD
                    accessDataBase();
                    //Object commande
                    c = commandeDAO.getCommande(Integer.parseInt(id));

                    Cursor cur =  produitDAO.getProduit(id);

                    //Thread lancé sur l'interface utilisateur
                    runOnUiThread(

                            ()->{
                                //Associe les éléments de la commandes aux champs associés
                                referenceTEF.setText(c.getReference());
                                date = c.getDate();
                                dateButton.setText(date);
                                descriptionTEF.setText(c.getDescription());

                                SimpleCursorAdapter adapter = new SimpleCursorAdapter(DetailOrderActivity.this,R.layout.item_product,cur,
                                        new String[]{cur.getColumnName(0),cur.getColumnName(1), cur.getColumnName(2)},
                                        new int[]{R.id.idLV, R.id.referenceLV, R.id.dateLV});

                                ls.setAdapter(adapter);

                                ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                        //Thread
                                        new Thread(
                                                ()->{
                                                    TextView idProduitTV = findViewById(R.id.idLV);
                                                    String idProduit = idProduitTV.getText().toString();

                                                    Produit p = produitDAO.getProduitId(idProduit);

                                                    //Accès à la BD
                                                    accessDataBase();
                                                    //Appel à la requête de suppression de la commande sélectionnée
                                                    produitDAO.deleteProduit(p);

                                                    //Retour à l'activité principale : liste des commandes existantes dans la base de données
                                                    Intent intent = new Intent(getApplicationContext(), DetailOrderActivity.class);
                                                    intent.putExtra("id",id);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                    startActivity(intent);

                                                }

                                        ).start();

                                    }
                                });
                            }




                    );

                }

        ).start();

        //Listeneur du bouton de modification
        modificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Vérification du nombre de caractère de la référence
                if(checkRef()){

                    new Thread(
                            ()->{
                                //Création d'une nouvelle commande qui correspond à l'ancienne commande modifiée
                                Commande nc = new Commande();;

                                //Récupération des informations relatives à la commande
                                nc.set_id(Integer.parseInt(id));
                                nc.setReference(referenceTEF.getText().toString());
                                nc.setDate(dateButton.getText().toString());
                                nc.setDescription(descriptionTEF.getText().toString());

                                //Accès à la base de données
                                accessDataBase();
                                //appel à la requête
                                commandeDAO.updateCommande(nc);

                                //Retour à l'activité principale : liste des commandes
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }

                    ).start();
                }

            }
        });

        //Listeneur du bouton de suppression d'une commande
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Thread
                new Thread(
                        ()->{

                            //Accès à la BD
                            accessDataBase();
                            //Appel à la requête de suppression de la commande sélectionnée
                            commandeDAO.deleteCommande(c);

                            //Retour à l'activité principale : liste des commandes existantes dans la base de données
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }

                ).start();

            }
        });

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent de l'activité à lancer après avoir appuyé sur l'item correspondant à une commande
                Intent intent = new Intent(getApplicationContext(), FormCreationProductActivity.class);
                //Ajout de l'id comme information supplémentaire à faire passer à l'activité à lancer.
                intent.putExtra("id",id);
                //Lancement de l'activité
                startActivity(intent);
            }
        });


    }

    /**
     * Vérifie le nombre de caractère de la référence d'une commande
     * @return boolean false si nombre de caractère <8, true sinon
     */
    private boolean checkRef(){

        //Récupération du contenu du champ texte
        String ref = referenceTEF.getText().toString();

        //Vérifie que la taille de la référence contient bien 8 caractère
        if (ref.length() < 8) {
            //Si la référence contient un nombre inférieur à 8, on retourne false
            referenceTF.setError("Référence inférieure à 8 caractères");
            return false;
        } else {
            //Sinon on retourne true
            return true;
        }
    }

    /**
     * Initialisation du composant dialog DatePicker
     */
    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String datef = makeDateString(day, month, year);
                dateButton.setText(datef);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);

    }

    /**
     *
     * @param day Jour
     * @param month Mois
     * @param year Année
     * @return Cast la date d'int en String
     */
    private String makeDateString(int day, int month, int year) {
        return day + " " + getMonthFormat(month) + " " + year;
    }

    /**
     * Change le format du mois
     * @param month Mois
     * @return retourne au format String le mois donné en paramètre
     */
    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEV";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "AVR";
        if(month == 5)
            return "MAI";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUI";
        if(month == 8)
            return "AOU";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //Donnée par défaut, jamais utilisée
        return "JAN";
    }

    /**
     * Ouvre le dialog DatePicker
     */
    public void openDatePicker() {
        datePickerDialog.show();
    }



    /**
     * Méthode d'accès à la base de données
     */
    public void accessDataBase(){

        appDataBase = AppDataBase.getAppDataBase(DetailOrderActivity.this);
        commandeDAO = appDataBase.commandeDAO();
        produitDAO = appDataBase.produitDAO();


    }


}