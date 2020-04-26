package com.example.geolocalisation;


/*
 * Exemple de gestion de base de données SQLite
 * BDD de test du musee
 *
 * Stockage des liens beacon => position => oeuvre
 *
 * La classe de gestion de BDD SQLite doit dériver de SQLiteOpenHelper (SDK android)
 * Elle permet de récupèrer une référence sur la base de données (si elle existe).
 * Et doit surcharger:
 * 		le constructeur initialisant l'objet de connexion à la BDD
 * 		la méthode public void onCreate(SQLiteDatabase db) pour créer la base (1 seule fois)
 * 			Cette méthode ne peut être appelée que si la BDD n'existe pas.
 * 			Si elle existe, l'appel de la méthode déclenchera une exception
 * 		la méthode public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) pour mise à jour de la BDD
 * 			Généralement, cette méthode doit coder
 * 				a) la destruction de la base
 * 				b) la reconstruction avec un numéro de version éventuellement upgradée
 */


import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;
/*
    Classe exemple de gestion d'une bdd.
    A adapter en fonction des choix réels de la structure de la base.
 */
public class gestionBDD extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "musee";


    /*
     * requête SQL pour la création de la base :
     * 1 table
     * 			oeuvre
     * 4 champs
     * 			ide		clé primaire auto-incremente
     * 			nom		nom de l'oeuvre
     * 			uuid	uuid du beacon correspondant
     * 			posx	position x sur le plan
     *          posy    position y sur le plan
     */

    //requete sql de création de la table qui contiendra
    //après mise à jopur les oeuvres associées à leur position et au tag Beacon
    private static final String sqlCreationTAble = "CREATE TABLE oeuvre ( " +
            "ide INTEGER PRIMARY KEY NOT NULL , " +
            "nom TEXT, " +
            "uuid TEXT, "+
            "posx INTEGER, " +
            "posy INTEGER " +
            ")";
    //requete pour les tests, elle insère 3 ouvres
    //INSERT INTO table(col1, col2...) VALUES(val1, val2...)
    private String sqlInsertionMonet = "Insert into oeuvre (nom , uuid , posx , posy) values (\"monet\" , \""+MainActivity.tabBeacon[0].getUuid()+"\" , 20 , 20)";
    private String sqlInsertionVangogh = "Insert into oeuvre (nom , uuid , posx , posy) values (\"vangogh\" , \""+MainActivity.tabBeacon[1].getUuid()+"\" , 60 , 60)";
    private String sqlInsertionCarpeaux = "Insert into oeuvre (nom , uuid , posx , posy) values (\"carpeaux\" , \""+MainActivity.tabBeacon[2].getUuid()+"\" , 120 , 20)";
    public gestionBDD(Context context) {
        //this est l'objet de connexion à la BDD
        //appel au constructeur de la classe mère
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        // TODO Auto-generated constructor stub
        Log.i("SQLITE", "creation de BDD " +sqlCreationTAble );
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        try{
            if(db == null){
                Log.e("SQLITE","Erreur BDD null");return;}
            db.execSQL(sqlCreationTAble);
            db.execSQL(sqlInsertionMonet);
            db.execSQL(sqlInsertionVangogh);
            db.execSQL(sqlInsertionCarpeaux);
            Log.i("SQLITE", "ON creation de BDD ");
        }catch(SQLiteException ie){Log.e("SQLITE",ie.getMessage());
            Log.e("SQLITE", sqlCreationTAble);}
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*
         * mise à jour de la base: destruction puis re-création
         */
        // TODO Auto-generated method stub
        try{
            if(db == null){Log.e("SQLITE","Erreur bdd null");return;}
            //destruction de la base
            db.execSQL("DROP TABLE IF EXISTS articles");

            Log.i("SQLITE", "ON upgrade de BDD ");
            //appel de la méthode onCreate(...)
            //=> re-création de la base
            this.onCreate(db);
        }catch(SQLiteException ie){Log.e("SQLITE",ie.getMessage());
            Log.e("SQLITE", sqlCreationTAble);}
        //db.close();
    }
    //methode qui retourne l'oeuvre en fonction du uuid
    public String getOeuvre(String uuid){
        String nom=null;
        //curseur permettant de se déplacer dans une table virtuelle SQL
        Cursor c = null;
        //db = référence à la BDD
        SQLiteDatabase db = this.getReadableDatabase();
        if(db == null){Log.e("SQLITE","Erreur BDD null");return null ;}
        try{
            String req = "SELECT nom FROM oeuvre where uuid=\"" + uuid + "\"";
            Log.i("SQLITE" , "Requete getArticle :"+ req );
            c = db.rawQuery(req, null);
            if(c == null){
                Log.e("SQLITE","Erreur curseur null");
                return null;//}
            }
            //test : la table est-elle vide?
            if(c.getCount() == 0) {
                return null;
            }
            c.moveToFirst();
            nom = c.getString(0);
            Log.e("SQLITE","Nom de l'oeuvre: " + nom);
            c.close();
        }catch(SQLiteException ie){
            Log.e("SQLITE",ie.getMessage());
            if(c != null)c.close();
            //db.close();
        }
        return nom;
    }
    //methode qui retourne la position x et y (tableau de 2 entiers) en fonction de uuid
    public int[] getPosXY(String uuid){
        int pos[]={0,0};
        //curseur permettant de se déplacer dans une table virtuelle SQL
        Cursor c = null;
        //db = référence à la BDD
        SQLiteDatabase db = this.getReadableDatabase();
        if(db == null){Log.e("SQLITE","Erreur BDD null");return null ;}
        try{
            String req = "SELECT posx , posy FROM oeuvre where uuid=\"" + uuid + "\"";
            Log.i("SQLITE" , "Requete get position :"+ req );
            c = db.rawQuery(req, null);
            if(c == null){
                Log.e("SQLITE","Erreur curseur null");
                return null;//}
            }
            //test : la table est-elle vide?
            if(c.getCount() == 0) {
                return null;
            }
            c.moveToFirst();
            pos[0] = c.getInt(0);
            pos[1] = c.getInt(1);
            Log.e("SQLITE","pos x= " + pos[0] + "\ty=" + pos[1]);
            c.close();
        }catch(SQLiteException ie){
            Log.e("SQLITE",ie.getMessage());
            if(c != null)c.close();
        }
        return pos;
    }


}

