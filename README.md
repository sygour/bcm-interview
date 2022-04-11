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

# Lancer le programme

## Configurer l'environnement

Il faut d'abord configurer les variables d'environnement `JAVA_HOME` et `M2_HOME`:
- `JAVA_HOME` représente le dossier racine de la version 17 du JDK (contenant `/bin`)
- `M2_HOME` représente de dossier `.m2` où se trouve les dépendances gérées par maven

## Compiler le projet

Avec maven:
```shell
mvn clean package
```

## Lancer le programme

```shell
${JAVA_HOME}/bin/java \
  -Dfile.encoding=UTF-8 \
  -classpath target/classes:${M2_HOME}/repository/com/fasterxml/jackson/core/jackson-core/2.13.2/jackson-core-2.13.2.jar:${M2_HOME}/repository/com/fasterxml/jackson/core/jackson-databind/2.13.2.2/jackson-databind-2.13.2.2.jar:${M2_HOME}/repository/com/fasterxml/jackson/core/jackson-annotations/2.13.2/jackson-annotations-2.13.2.jar:${M2_HOME}/repository/com/fasterxml/jackson/datatype/jackson-datatype-jsr310/2.13.2/jackson-datatype-jsr310-2.13.2.jar:${M2_HOME}/repository/org/assertj/assertj-core/3.21.0/assertj-core-3.21.0.jar \
  Main \
  01-01-2020 02-01-2020 json
```
