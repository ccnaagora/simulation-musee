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
 
