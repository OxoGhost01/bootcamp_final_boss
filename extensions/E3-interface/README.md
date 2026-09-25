# Extension E3 — une interface vivante (JavaScript)

Deux fonctions à ajouter dans `app.js`.

```js
// 1. filtrerParEcurie(liste, ecurie) : renvoie une NOUVELLE liste ne contenant
//    que les entrées de cette écurie. Une écurie vide ("") renvoie tout.
//    La liste reçue n'est pas modifiée.
function filtrerParEcurie(liste, ecurie)

// 2. activerTri(idTable, liste) : rend les en-têtes du tableau cliquables.
//    Chaque <th> porte un attribut data-colonne ("nom", "points" ou "victoires").
//    Au clic, le <tbody> du tableau est réaffiché (avec remplirTableau) trié :
//      - "points" et "victoires" : ordre DÉCROISSANT
//      - "nom" : ordre alphabétique CROISSANT
//    Le tri doit fonctionner à chaque clic, y compris après un réaffichage.
function activerTri(idTable, liste)
```

## Vérification

Copiez `tests_E3.js` dans `03-js/`, puis ajoutez cette ligne dans `index.html`, juste après
`<script src="tests.js"></script>` :

```html
<script src="tests_E3.js"></script>
```

Rechargez la page : les tests de l'extension s'affichent à la suite des autres.

## Pièges

- `sort` trie **par ordre alphabétique** par défaut : `[9, 25, 100]` devient `[100, 25, 9]`.
  Un comparateur est indispensable sur les colonnes numériques.
- `sort` modifie le tableau **sur place** : la liste d'origine ne doit pas bouger.
- Les `<tr>` sont recréés à chaque réaffichage : mettez l'écouteur sur les `<th>`, qui, eux, restent.
