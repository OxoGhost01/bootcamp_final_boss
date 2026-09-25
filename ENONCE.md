# 🏁 Projet transversal — Championnat de F1

Vous disposez de l'export brut d'un championnat de Formule 1 : 5 courses, 10 pilotes, 5 écuries.
Votre mission est de construire une **chaîne de traitement en trois maillons**, un par langage.

```
donnees/resultats.csv
        ↓  maillon 1 — PYTHON (notebook)      ingestion et nettoyage
02-java/courses_propres.csv                   ← CONTRAT 1
        ↓  maillon 2 — JAVA                   moteur de calcul
03-js/donnees.js                              ← CONTRAT 2
        ↓  maillon 3 — JAVASCRIPT             interface web
page de classements
```

Chaque maillon a ses propres tests automatiques. **Un maillon n'est terminé que lorsque ses tests
sont tous au vert.**

---

## Les deux contrats

Ils sont figés : c'est ce qui permet aux trois maillons de fonctionner ensemble.

**CONTRAT 1 — `02-java/courses_propres.csv`**

```
course;pilote;ecurie;position;temps_tour
Bahrein;VERSTAPPEN;Red Bull;1;93.614
Bahrein;HAMILTON;Mercedes;0;
```
La position 0 signale un abandon ; le temps du meilleur tour est exprimé en secondes, ou vide.

**CONTRAT 2 — `03-js/donnees.js`**

```js
const PILOTES = [{"nom": "VERSTAPPEN", "ecurie": "Red Bull", "points": 101, "victoires": 2}, ...];
const ECURIES = [{"nom": "Red Bull", "points": 139, "victoires": 2}, ...];
```

---

## Les règles du championnat

- Barème des dix premiers : **25, 18, 15, 12, 10, 8, 6, 4, 2, 1**. Au-delà de la 10e place, 0 point.
- Un abandon ne rapporte rien.
- Le classement des écuries additionne les points de ses deux pilotes.
- En cas d'égalité : d'abord le nombre de victoires, puis le nombre de 2e places, puis l'ordre
  alphabétique.

---

## Maillon 1 — Python (notebook)

Ouvrez `01-python/ingestion.ipynb`. Trois fonctions à compléter : conversion du chronomètre en
secondes, lecture du fichier brut, écriture du contrat 1. Objectif **4/4** aux tests, puis exécution
de la cellule de production qui écrit le fichier destiné au maillon Java.

## Maillon 2 — Java

Dans `02-java/`, complétez les quatre méthodes de `src/Classement.java` : barème, classement des
pilotes, classement des écuries, position moyenne. Les classes `Ligne`, `Resultat`, `Chargeur` et
`Main` sont fournies et ne doivent pas être modifiées.

```bash
cd 02-java
javac -encoding UTF-8 -d out src/*.java
java -Dstdout.encoding=UTF-8 -cp out Tests      # les tests (objectif 5/5)
java -Dstdout.encoding=UTF-8 -cp out Main       # produit ../03-js/donnees.js
```

## Maillon 3 — JavaScript

Dans `03-js/`, complétez les trois fonctions de `app.js` : tri par points, remplissage du tableau,
mise en valeur du podium. Ouvrez `index.html` dans le navigateur : les tests se lancent seuls, et
les classements s'affichent dès que vos fonctions marchent. Objectif **5/5**.

---

## En cas de blocage

Le dossier `secours/` contient le résultat attendu de chaque étape. Si un maillon vous résiste,
copiez le fichier correspondant et **passez au maillon suivant** : vous perdez les points de cette
étape, pas ceux des suivantes.

| Vous êtes bloqué sur | Copiez | Vers |
|---|---|---|
| Python | `secours/courses_propres.csv` | `02-java/` |
| Java | `secours/donnees.js` | `03-js/` |

---

## Extensions

Une fois les trois maillons au vert, **figez votre travail** dans un dossier `socle_valide/`
(copiez tout le pack), puis attaquez les extensions dans l'ordre. Chacune a son README.

| Extension | Langage | Sujet |
|-----------|---------|-------|
| E1 | Python | les données sales : casse, accents, doublons, virgules décimales |
| E2 | Java | le point du meilleur tour, réservé aux pilotes classés dans les dix |
| E3 | JS | filtre par écurie et tri par colonne |
| E4 | — | la chaîne complète en une commande |

---

## Barème

| Élément | Points |
|---------|--------|
| Maillon Python (tests 4/4) | 5 |
| Maillon Java (tests 5/5) | 6 |
| Maillon JavaScript (tests 5/5) | 4 |
| Chaîne complète qui tourne de bout en bout | 1 |
| Extensions (E1 à E4) | 4 |
| **Total** | **20** |
