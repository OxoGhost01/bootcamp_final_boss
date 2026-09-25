# Extension E4 — la chaîne complète

Jusqu'ici, chaque maillon a été lancé à la main. Objectif : **une seule commande** qui rejoue toute
la chaîne, du CSV brut jusqu'à la page web, et un contrôle automatique du résultat.

## 1. Vérifier la chaîne (fourni)

Depuis la racine du pack :

```bash
python3 extensions/E4-chaine-complete/verifier_chaine.py
```

Le script contrôle le contrat 1, compile le Java, exécute `Main` et compare `donnees.js` à la
référence. Il s'arrête au premier maillon fautif et dit lequel.

## 2. Automatiser l'ingestion (à faire)

Le maillon Python vit dans un notebook : il faut d'abord l'exporter en script.

```bash
cd 01-python
jupyter nbconvert --to script ingestion.ipynb      # produit ingestion.py
```

Écrivez ensuite `chaine.py` à la racine du pack, qui enchaîne :

1. l'exécution de l'ingestion (module `subprocess`, ou import direct de vos fonctions) ;
2. la compilation et l'exécution du maillon Java ;
3. l'appel au script de vérification.

Le tout doit se lancer par :

```bash
python3 chaine.py
```

## 3. Aller plus loin (facultatif)

Remplacer le pivot CSV par du **JSON** entre Python et Java. Côté Python, `json.dump` suffit. Côté
Java, il faut soit écrire un mini-analyseur, soit ajouter la bibliothèque Gson en `.jar` local et
compiler avec `-cp gson.jar`. C'est exactement le genre de décision qu'on prend en projet réel :
un format lisible par tous contre une dépendance de plus.
