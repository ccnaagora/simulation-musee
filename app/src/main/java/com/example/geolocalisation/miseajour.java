package com.example.geolocalisation;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

public class miseajour extends AppCompatActivity implements Runnable{

    //variable qui contient le nom du fichier à télécharger.
    //ne sert qu'à titre exemple.
    String nf = "carpeaux.pdf";
    Runnable r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miseajour);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //instanciation des 2 boutons pour démarrer la mise à jour puis l'arreter
        //et rvenir à l'appli principale
        Button bdemarrer = findViewById(R.id.demarrer);
        Button bterminer = findViewById(R.id.terminer);
        //gestionnaire d'événements
        r = this;
        bdemarrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //coder ici le démarrage de la mise à jour:
                //client ftp, téléchargement des infos et fichiers nécessaires
                //mise à jours de la bdd SQlite
                Thread th = new Thread(r);
                th.start();
            }
        });
        bterminer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }
    //methode de téléchargements d'un fichier : thread
    public void run(){
        //pour faire un essai de téléchargement de fichier:
                FTPClient client = new FTPClient();
                client.setAutoNoopTimeout( 30000);
                //les fichiers à télécharger doivent tous être dans download
                String chem = Environment.getExternalStorageDirectory().toString() + "/Download/";
                try {
                    //changer l'ip avec celle du serveur ftp
                    client.connect("192.168.1.10",21);
                    //changer le login en fonction des users du serveur ftp
                    client.login("musee", "museemusee");
                    client.setType(FTPClient.TYPE_AUTO);
                    //download: nom du fichier distant et nom du fichier local
                    client.download(nf, new File(chem + nf));//-1 , null
                } catch (Exception e) {
                    Log.e("FTP", "exception 1=" + e.getMessage());
                    try {
                        client.disconnect(true);
                    } catch (Exception e2) {
                        Log.e("FTP", "exception 2=" + e2.getMessage());
                    }
                }
    }
}
