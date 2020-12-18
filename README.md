# Installation

## Description des dossiers

**dataFiles** : Contient les fichiers csv qui vont remplir notre base de données (triplestore Jena)
**ontologie** : Contient un fichier .owl qui peut-être lu dans protégé et qui décrit notre ontologie
**requetes** : Contient un fichier texte qui représente les requêtes SPARQL que nous exécutons sur notre site web lorsque nous voulons afficher certaines informations.
**semweb** : Projet Maven que nous avons créé avec ECLIPSE, qui permet de charger les csv de **dataFiles** dans le triplestore JENA. Notez qu'il faut modifier les chemins d'accès à ces fichiers selon le chemin où vous avez cloné le repository.
Il est également nécessaire d'avoir une base de données créé sur Jena avec le nom **reunion_island**.
**Website** : Contient le html, css et javascript qui permet de faire tourner le site web.
Si vous rencontrez une erreur CORS, utilisez Apache et placez le contenu de ce dossier dans le dossier www d'Apache (/var/www/html/ pour Linux)

## Initialisation

Vous devez avoir Jena Apache qui tourne, sur le port 3030. Pour cela, se référer au site web de jena.
Sur Linux, il faut avoir lancé fuseki-server de Jena Apache. Le port par défaut est le 3030.
Créez ensuite une base de données "reunion_island" dans Jena.

Nous allons charger les données des fichiers csv sur cette base de données.
Pour cela, ouvrez le projet Maven avec un IDE java, et modifier dans chacun des fichiers le chemin vers le fichier csv.
Lancez simplement ensuite chaque programme, et les données devraient maintenant être disponible sur Jena, vous pouvez essayer.

Le serveur Jena doit être en train de tourner pour que le site fonctionne

## Utilisation

Vous pouvez ensuite vous rendre sur la page localhost si vous utilisez apache, et le site devrait être fonctionnel. Vous pouvez maintenant cliquer sur une commune pour zoomer dessus, et choisir les informations que vous voulez afficher.
