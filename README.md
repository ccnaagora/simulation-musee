# simulation-musee
Les bases du projet de géolocalisation

Activité principal qui switch entre l'affichage du plan et de la position ou l'oeuvre correspondant à la position.
L'association pos <=> ouvre <=> TAG beacon est stockée dans une base SQlite (à redéfinir).

Cette base est gérée par une classe gestionBDD (incomplète et inadaptée pour le projet final)
La détection des beacon est simuilée par un click sur le bouton en bas de la vue.
En effet, 3 beacon sont simulés et à chaque click, on passe de l'un à l'autre (3 objets de type SimulBeacon)
Cette activité permet grace à un menu d'appeler une 2ème activité qui gère la mise à jour des documents (à compléter).

Pour que cet exemple fonctionne, il faut:
    les fichiers déja présents dans le répertoire download du smartphone
        orsay.png, carpeaux.png et pdf, vangogh.png et pdf, monet.png et pdf
    sinon, l'application lève une exception au démarrage et se ferme.
    
Il est parfois difficile selon les modèles de smartphones et notemment les émulateurs de copier les fichiers dans le répertoire download.
Dans ce cas, on peut faire:
//*******************************************************************<br>
/**Essai sur émulateur: nexus4 api25
/**Chemin reel du Download:			/storage/emulated/0/Download/
/**obtenu par le code java:			Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/"

/**!Le répertoire Download visible n'est pas le bon, != /storage/emulated/0/Download/

/**Pour transférer les fichiers:
/**1) ouvrir une console windows
/**2) se déplacer dans le répertoire du sdk android: 	c:\chemin_Local_Vers_Android\sdk\platform-tools
/** 	(le chemin du sdk est visible dans le fichier local.properties des gradle scripts. attention aux doubles \\)
/** 	cd c:\chemin_Local_Vers_Android\sdk\platform-tools
/**3) copier le fichier depuis le répertoire local vers le répertoire download du smartphone en tappant la commande ci-dessous.
/** 	adb push chemin_vers_fichier_local\orsay.png  /storage/emulated/0/Download/orsay.png  
/**	    ex: 	adb push D:\wamp64\www\musee\orsay.png /storage/emulated/0/Download/orsay.png
/**	
/**4) Vérifier sur le smartphone que l'appli a les droits sur le stockage
/**	    settings => apps => choisir l'appli 	=> vérifier et/ou modifier les permissions.
/**	
/**
/**5) relancer l'appli, ça devrait fonctionner.
//*****************************************************************************
