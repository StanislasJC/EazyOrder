package com.example.eazyorder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Random;

public class FormCreationOrderActivity extends AppCompatActivity {

    //Initialisation des éléments

    //Référence
    private TextInputLayout referenceTF;
    private TextInputEditText referenceTEF;
    private ImageButton rollDButton;

    //Date
    private DatePickerDialog datePickerDialog;
    private Button dateButton;

    //Description
    private TextInputLayout descriptionTF;
    private TextInputEditText descriptionTEF;

    //Bouton confirmation et signature
    private Button confirmButton;
    private Button signatureButton;

    //Base de données
    AppDataBase appDataBase;
    CommandeDAO commandeDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_creation_order);

        //Référence
        referenceTF = findViewById(R.id.referenceTextField);
        referenceTEF = findViewById(R.id.referenceTextInputEditField);
        rollDButton = findViewById(R.id.rolldices);

        //Listener du bouton rollDButton onClick pour obtenir une référence aléatoire
        rollDButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRandomRef();
            }
        });

        //Date
        initDatePicker();
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodaysDate());

        //Listener bouton date Onclick pour ouvrir le dialog spinner
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker();
            }
        });

        //Description
        descriptionTF = findViewById(R.id.descriptionTextField);
        descriptionTEF = findViewById(R.id.descriptionTextInputEditField);

        //Boutons
        confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkRef()){
                    new Thread(
                            ()->{
                                accessDataBase();
                                Commande c = new Commande();

                                c.setReference(getReference());
                                c.setDate(getDate());
                                c.setDescription(getDescription());

                                commandeDAO.insertCommande(c);

                                Intent i = new Intent(FormCreationOrderActivity.this, MainActivity.class);
                                startActivity(i);

                            }

                    ).start();
                }
            }
        });




        signatureButton = findViewById(R.id.signatureButton);


    }

    //////// GESTION DE LA REFERENCE ////////

    /* On considère que le format de la référence est au format une lettre suivie de 5 chiffres */

    /**
     * Vérifie que la référence correspond à des contraintes de sécurité
     * @return booléan true pour ok false pour non respect des contraintes de sécurité
     */
    public boolean checkRef() {

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
     * Renvoie une référence aléatoire contenant des chiffres et des lettres.
     */
    private void getRandomRef(){

        //Génération d'un chiffre aléatoire inférieur à 8
        Random rand = new Random();
        int digitRdmRef = 8 + rand.nextInt(16-8);

        //Initialisation des éléments nécessaire à la construction de la référence aléatoire
        String theAlphaNum = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789";
        StringBuilder builder = new StringBuilder(digitRdmRef);

        //Boucle de création
        for(int i=0;i<digitRdmRef;i++){

            //Génération numérique
            int myIndex = (int)(theAlphaNum.length() * Math.random());

            //Ajouter les caractères
            builder.append(theAlphaNum.charAt(myIndex));
        }

        referenceTEF.setText(builder.toString());
    }

    /**
     * Assecceur get du champ référence
     * @return le texte du champ référence
     */
    public String getReference(){
        return referenceTEF.getText().toString();
    }



    //////// GESTION DE LA DATE ////////

    /**
     * Récupérer la date du jour
     * @return retourne la date du jour
     */
    private String getTodaysDate() {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return makeDateString(day,month,year);
    }

    /**
     * Initialisation du composant dialog DatePicker
     */
    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
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
     * Accesseur get du bouton date
     * @return le texte du bouton date
     */
    public String getDate(){
        return dateButton.getText().toString();
    }

    //////// GESTION DE LA DESCRIPTION ////////

    public String getDescription(){
        return descriptionTEF.getText().toString();
    }

    //////// GESTION DE LA DESCRIPTION ////////

    /**
     * Méthode d'accès à la base de données
     */
    public void accessDataBase(){

         appDataBase = AppDataBase.getAppDataBase(FormCreationOrderActivity.this);
         commandeDAO = appDataBase.commandeDAO();


    }




}