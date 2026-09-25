/* =========================================================================
   MAILLON 3 — JAVASCRIPT : l'interface
   Les données arrivent du maillon Java, dans donnees.js :
     PILOTES = [{nom, ecurie, points, victoires}, ...]
     ECURIES = [{nom, points, victoires}, ...]
   Complétez les trois fonctions, puis ouvrez index.html dans le navigateur.
   ========================================================================= */

// 1. trierParPoints(liste) : renvoie une NOUVELLE liste triée par points
//    DÉCROISSANTS. La liste reçue ne doit pas être modifiée.
//    À points égaux, celui qui a le plus de victoires passe devant.
function trierParPoints(liste) {
  const copy = liste.toSorted(function(a, b) {
    return b.points - a.points || b.victoires - a.victoires;
  });
  return copy;
}

// 2. remplirTableau(idCorps, liste) : remplit le <tbody> dont l'id est fourni.
//    Une ligne <tr> par entrée, avec dans l'ordre les cellules <td> :
//      rang (1, 2, 3...) | nom | écurie (chaîne vide si absente) | points | victoires
//    Chaque <tr> porte l'attribut data-nom. Un nouvel appel REMPLACE le contenu.
function remplirTableau(idCorps, liste) {
  const corps = document.getElementById(idCorps);
  corps.innerHTML = "";
  for (const [i, entree] of liste.entries()) {
    const ligne = document.createElement("tr");
    ligne.setAttribute("data-nom", entree.nom);
    ligne.innerHTML = `<td>${i + 1}</td><td>${entree.nom}</td><td>${entree.ecurie ?? ""}</td><td>${entree.points}</td><td>${entree.victoires}</td>`;
    corps.appendChild(ligne);
  }
}

// 3. marquerPodium(idCorps) : ajoute la classe CSS "podium" aux TROIS PREMIÈRES
//    lignes du tableau, et la retire de toutes les autres.
function marquerPodium(idCorps) {
  const corps = document.getElementById(idCorps);
  const prems = corps.querySelectorAll("tr:nth-child(-n+3)");
  for (const tr of prems) {
    tr.classList.add("podium");
  }
}

/* --- FOURNI — NE PAS MODIFIER : affichage de la saison ------------------- */
function afficherSaison() {
  if (typeof PILOTES === "undefined") {
    return;
  }
  remplirTableau("corps-pilotes", trierParPoints(PILOTES));
  marquerPodium("corps-pilotes");
  remplirTableau("corps-ecuries", trierParPoints(ECURIES));
  marquerPodium("corps-ecuries");
}

// 1. filtrerParEcurie(liste, ecurie) : renvoie une NOUVELLE liste ne contenant
//    que les entrées de cette écurie. Une écurie vide ("") renvoie tout.
//    La liste reçue n'est pas modifiée.
function filtrerParEcurie(liste, ecurie) {
  return ecurie === "" ? liste : liste.filter(e => e.ecurie === ecurie);
}

// 2. activerTri(idTable, liste) : rend les en-têtes du tableau cliquables.
//    Chaque <th> porte un attribut data-colonne ("nom", "points" ou "victoires").
//    Au clic, le <tbody> du tableau est réaffiché (avec remplirTableau) trié :
//      - "points" et "victoires" : ordre DÉCROISSANT
//      - "nom" : ordre alphabétique CROISSANT
//    Le tri doit fonctionner à chaque clic, y compris après un réaffichage.
function activerTri(idTable, liste) {
  const table = document.getElementById(idTable);
  const corps = table.querySelector("tbody");
  table.querySelectorAll("th[data-colonne]").forEach(th => {
    th.addEventListener("click", () => {
      const colonne = th.dataset.colonne;
      const triee = liste.toSorted((a, b) =>
        colonne === "nom" ? a.nom.localeCompare(b.nom) : b[colonne] - a[colonne]);
      remplirTableau(corps.id, triee);
    });
  });
}
