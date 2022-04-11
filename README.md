# Agrégation des données de production de centrales

## Architecture

### Main

Ce fichier est la classe point d'entrée du programme, son unique but est de parser les paramètres, de fournir les usages (non implémenté) et d'appeler le point d'entrée (appelé ici `Resource`.

### Domain

Ce package contient les règles métier liées à l'agrégation:

- définition du `use case`: l'agrégation entre 2 dates
- la représentation d'une liste agrégée :
  - somme des différentes valeurs (non implémenté)
  - création de valeurs moyennes pour les données manquantes

### Infra primaire

Ce package contient les points d'entrées pour l'extérieur, dans notre cas il n'y en a qu'un seul: `PowerPlantProductionResource` dont le but est d'appeler le `UseCase` et de sérialiser son résultat.

Le seul format de sortie implémenté est `json`, pour implémenter `csv` deux choix peuvent s'offrir:
- ajouter un serializer csv dans la resource existante
- créer une nouvelle resource par format

Si le nombre de format est voué à augmenter, la seconde serait à privilégier.

### Infra secondaire

Ce package gère la communication avec les API des différentes centrales. Pour chaque centrale, on trouve :
- XxxPowerPlant qui gère l'appel de la resource externe
- XxxProduction représentant les données attendues et utilisé pour la déserialization
- une fonction dans `ProductionConverter` qui convertit en `PowerPlantProduction`, la représentation unique du domain

Limitations :
- Seul Hawes est actuellement disponible
- Il n'y a aucune gestion au niveau des exceptions renvoyées par l'appel externe : il faudrait mettre en place une politique de rejeu (avec par exemple: [retry4j](https://github.com/elennick/retry4j))
