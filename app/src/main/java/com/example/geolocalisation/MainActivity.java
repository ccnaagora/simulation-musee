package com.example.geolocalisation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Switch;

import java.io.File;
/*
Activité principal qui switch entre l'affichage du plan et la position
et l'oeuvre correspondant à la position.
L'association pos <=> ouvre <=> TAG beacon est stockée dans une base SQlite (à redéfinir)
Cette base est gérée par une classe gestionBDD (incomplète et inadaptée pour le projet final)

La détection des beacon est simuilée par un click sur le bouton en bas de la vue.
En effet, 3 beacon sont simulés et à chaque click, on passe de l'un à l'autre (3 objets de type SimulBeacon)

Cette activité permet grace à un menu d'appeler une 2ème activité qui gère la mise à jour
des documents (à compléter).

Pour que cet exemple fonctionne, il faut:
    les fichiers déja présents dans le répertoire download du smartphone
        orsay.png, carpeaux.png et pdf, vangogh.png et pdf, monet.png et pdf
    sinon, l'application lève une exception au démarrage et se ferme.
 */
public class MainActivity extends AppCompatActivity {

    //membres statics pour la simulation de 3 beacon
    public static SimulBeacon tabBeacon[] = {
            new SimulBeacon("AAAAAAA" , 1 , 1),
            new SimulBeacon("BBBBBBB" , 1 , 2),
            new SimulBeacon("CCCCCCC" , 1 , 3)
    };
    //représente le dernier beacon qui a été détecté
    public SimulBeacon leBeaconDetecte = tabBeacon[0];

    //membres widgets pour gérer le graphisme
    ImageView im;
    Switch sw;
    //membre base de données musse
    gestionBDD bd ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //simulation du changement de beacon
                if(leBeaconDetecte.getNumMin() == 1)leBeaconDetecte = tabBeacon[1];
                else if(leBeaconDetecte.getNumMin() == 2)leBeaconDetecte = tabBeacon[2];
                else if(leBeaconDetecte.getNumMin() == 3)leBeaconDetecte = tabBeacon[0];
                Log.i("MUSEE" , "Beacon détecté=" + leBeaconDetecte.toString());
                //fin de la simulation
                //declenchement du dessin de l'oeuvre ou position
                dessine(leBeaconDetecte.getUuid());
            }
        });
        //creer la bdd si elle n'existe pas
        bd = new gestionBDD(getApplicationContext());
        //verification de son contenu
        bd.getOeuvre("AAAAAAA");
        bd.getPosXY("AAAAAAA");
        //instanciation des widgets et gestion su switch
        //pour passer du plan à l'oeuvre
        sw = findViewById(R.id.sw);
        im = findViewById(R.id.im);
        sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sw.isChecked()) {
                    //on affiche le pdf
                    dessinerPdf("monet");
                }
                else {
                    //on dessine le plan
                    dessinerImage(10 , 10);
                }
            }
        });
        //initialement, on dessine le plan
        dessinerImage(10 , 10);
    }
//********************************************************************
//dessine : en fonction de uuid et switch, appelle la methode de dessin du pdf ou du plan
//ce code est conçu pour dessiner lpdf et png préalablement téléchargés et stockés dans
//le répéertoire download du smartphone.
//si ce n'est pas le cas, l'appli s'arrête
public void dessine(String uuid){
    if(sw.isChecked()) {
        //on affiche le pdf
        String nom = bd.getOeuvre(uuid);
        Log.i("MUSEE" , "nom oeuvre avant dessin: " + nom);
        dessinerPdf(nom);
    }
    else {
        //on dessine le plan
        int xy[] = bd.getPosXY(uuid);
        Log.i("MUSEE" , "Position: " + xy[0] + "\t" + xy[1]);
        dessinerImage(xy[0] , xy[1]);
    }
}
//****méthode de dessin d'une image (plan) ou affichage d'un pdf (oeuvre
public void dessinerPdf(String x) {
        String n = x+".pdf";
    //analyse de l'arborescence du système de fichier pour vérifier l'emplacement des fichiers pdf
    //String racine = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download";
    //File rac = new File(racine);
    //Log.d("MUSEE", "datadirectory=" + rac.getAbsolutePath()+"/"+n);
    File f = null;
    try {
        f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/" + n);
        ParcelFileDescriptor desc = ParcelFileDescriptor.open(f , ParcelFileDescriptor.MODE_READ_ONLY );
        //pdf = fichier pdf
        PdfRenderer pdf = new PdfRenderer(desc);
        int nbp = pdf.getPageCount();
        Log.d("MUSEE", "nb pages=" + nbp);
        //page = page[index] du pdf
        PdfRenderer.Page page = pdf.openPage(0);
        //on calcul la taille pour l'adapter à la taille de l'imageView si nécessaire
        int h = page.getHeight();
        int w = page.getWidth();
        //on crée un bitmap (tableau de pixel)
        Bitmap  bt = Bitmap.createBitmap(w , h , Bitmap.Config.ARGB_8888);
        //on le rempli avec la page du pdf convertie en image
        page.render(bt,null,null,PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        //on affecte le bitmap à l'ImageView
        im.setImageBitmap(bt);
        Log.d("MUSEE", "x=" + w + "   h=" + h);

    }
    catch(Exception e){
        Log.d("MUSEE", "Fichier=" + f.getAbsolutePath()+"       exception io: " + e.getMessage());
    }
}
    public void dessinerImage(int x , int y ){
        //on crée un bitmap à partir  du plan préalablement téléchargé et socké dans le répertoire download du smartphone
        String cheminPlanMusee = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/orsay.png";
        Bitmap bitmap2 = BitmapFactory.decodeFile(cheminPlanMusee);
        // = BitmapFactory.decodeResource(getResources(),R.drawable.orsay) ;
        int w = bitmap2.getWidth();
        int h = bitmap2.getHeight();
        //on crée une copie du bitmap précédent (read only) pour pouvoir dessiner dessus
        Bitmap bitmap = Bitmap.createBitmap(w , h , Bitmap.Config.ARGB_8888);
        bitmap = bitmap2.copy(Bitmap.Config.ARGB_8888, true);
        //on en extrait une feuille de dessin (un calque)
        Canvas c = new Canvas(bitmap);
        //dessine l'image
        Paint p = new Paint();
        c.drawBitmap(bitmap,0,0,null);
        //dessine par dessus le bitmap un rectangle bleu
        Rect r = new Rect(x  , y ,x+100 , y+100);
        p.setColor(Color.BLUE);
        c.drawRect(r , p);
        //ecrire du texte sur le canva
        p.setTextSize(50.2f);
        p.setColor(Color.BLACK);
        c.drawText("Musée d'Orsay" , 10 , 30,p);
        //on affecte le bitmap à l'ImageView
        im.setImageBitmap(bitmap);
    }
//*****************************************************************************
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //démarrage de l'activité de maintenance : mise à jour
            Intent it = new Intent(getApplicationContext() , miseajour.class);
            startActivity(it);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
