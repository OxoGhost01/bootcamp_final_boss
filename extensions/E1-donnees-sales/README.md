# Extension E1 — les données sales (Python)

Le fichier `donnees/resultats.csv` de cette extension est l'export **réel** du chronométreur :
même format, mêmes courses, mais tel qu'il sort du système.

Votre mission : votre fonction `lire_resultats` doit produire **exactement** le même fichier
propre qu'avec les données soignées du socle. Rien d'autre ne change : mêmes fonctions, même contrat.

## Ce que contient le fichier

| Anomalie | Exemple | Règle à appliquer |
|----------|---------|-------------------|
| Casse incohérente des pilotes | `verstappen`, `Stroll` | tout en MAJUSCULES |
| Accents | `PÉREZ` | à supprimer (`PEREZ`) |
| Écuries en majuscules et espaces parasites | `"  RED BULL "` | à retrouver dans le dictionnaire `ECURIES_CONNUES` ci-dessous (attention à `McLaren`, que `.title()` écrirait `Mclaren`) |
| Virgule décimale | `1:33,614` | la virgule vaut le point |
| Doublon exact | une ligne répétée à l'identique | à supprimer |
| Ligne sans pilote | champ `pilote` vide | à ignorer |
| Écurie inconnue | `Williams` | ligne à ignorer |

```python
ECURIES_CONNUES = {
    "RED BULL": "Red Bull", "FERRARI": "Ferrari", "MERCEDES": "Mercedes",
    "MCLAREN": "McLaren", "ASTON MARTIN": "Aston Martin",
}
```

*Indice pour les accents :*
```python
import unicodedata
sans_accents = "".join(c for c in unicodedata.normalize("NFD", texte)
                       if unicodedata.category(c) != "Mn")
```

## Vérification

Collez le contenu de `cellule_tests_E1.py` à la fin de votre notebook et exécutez-la.
Le test compare votre sortie, ligne à ligne, au fichier `reference/courses_propres.csv`.
