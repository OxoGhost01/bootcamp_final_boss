/* CORRIGÉ E3 — les deux fonctions à ajouter à app.js */

function filtrerParEcurie(liste, ecurie) {
  if (!ecurie) {
    return [...liste];
  }
  return liste.filter((entree) => entree.ecurie === ecurie);
}

function activerTri(idTable, liste) {
  const table = document.getElementById(idTable);
  const corps = table.querySelector("tbody");
  table.querySelectorAll("th[data-colonne]").forEach((entete) => {
    entete.addEventListener("click", () => {
      const colonne = entete.dataset.colonne;
      const triee = [...liste].sort((a, b) => {
        if (colonne === "nom") {
          return a.nom.localeCompare(b.nom);
        }
        return b[colonne] - a[colonne];
      });
      remplirTableau(corps.id, triee);
    });
  });
}
