/* Tests automatiques du maillon JS — NE PAS MODIFIER */

const GABARIT_DOM = `
  <table><tbody id="corps-test"></tbody></table>
`;

function reinitialiserDOM() {
  document.getElementById("bac").innerHTML = GABARIT_DOM;
}

const _tests = [];
function test(nom, fn) {
  _tests.push({ nom, fn });
}

function repr(v) {
  if (typeof v === "undefined") return "undefined";
  if (typeof v === "number" && Number.isNaN(v)) return "NaN";
  return JSON.stringify(v) ?? String(v);
}

function egal(obtenu, attendu) {
  if (repr(obtenu) !== repr(attendu)) {
    throw new Error(`attendu ${repr(attendu)}, obtenu ${repr(obtenu)}`);
  }
}

function vrai(condition, message) {
  if (!condition) throw new Error(message);
}

const JEU = [
  { nom: "A", ecurie: "E1", points: 9, victoires: 0 },
  { nom: "B", ecurie: "E2", points: 100, victoires: 3 },
  { nom: "C", ecurie: "E1", points: 25, victoires: 1 },
  { nom: "D", ecurie: "E2", points: 25, victoires: 2 },
  { nom: "E", ecurie: "E1", points: 1, victoires: 0 },
];

test("1. trierParPoints", () => {
  const trie = trierParPoints(JEU);
  egal(trie.map((e) => e.nom), ["B", "D", "C", "A", "E"]);
  vrai(trie !== JEU, "une NOUVELLE liste doit être renvoyée");
  egal(JEU.map((e) => e.nom), ["A", "B", "C", "D", "E"]);
  egal(trierParPoints([]).length, 0);
});

test("2. remplirTableau", () => {
  remplirTableau("corps-test", JEU.slice(0, 2));
  const lignes = document.querySelectorAll("#corps-test tr");
  egal(lignes.length, 2);
  const cellules = lignes[0].querySelectorAll("td");
  egal(cellules.length, 5);
  egal(cellules[0].textContent.trim(), "1");
  egal(cellules[1].textContent.trim(), "A");
  egal(cellules[2].textContent.trim(), "E1");
  egal(cellules[3].textContent.trim(), "9");
  egal(cellules[4].textContent.trim(), "0");
  egal(lignes[1].dataset.nom, "B");
  egal(lignes[1].querySelectorAll("td")[0].textContent.trim(), "2");
});

test("3. remplirTableau : remplace l'affichage et gère les écuries", () => {
  remplirTableau("corps-test", JEU);
  remplirTableau("corps-test", [{ nom: "Red Bull", points: 139, victoires: 2 }]);
  const lignes = document.querySelectorAll("#corps-test tr");
  egal(lignes.length, 1);
  egal(lignes[0].querySelectorAll("td")[2].textContent.trim(), "");
  egal(lignes[0].querySelectorAll("td")[3].textContent.trim(), "139");
});

test("4. marquerPodium", () => {
  remplirTableau("corps-test", trierParPoints(JEU));
  marquerPodium("corps-test");
  const lignes = document.querySelectorAll("#corps-test tr");
  egal(document.querySelectorAll("#corps-test tr.podium").length, 3);
  vrai(lignes[0].classList.contains("podium"), "la 1re ligne est sur le podium");
  vrai(!lignes[3].classList.contains("podium"), "la 4e ligne ne l'est pas");
  // un nouveau classement : le podium doit suivre
  remplirTableau("corps-test", JEU.slice(0, 1));
  marquerPodium("corps-test");
  egal(document.querySelectorAll("#corps-test tr.podium").length, 1);
});

test("5. saison complète (donnees.js)", () => {
  vrai(typeof PILOTES !== "undefined",
       "donnees.js est absent : le maillon Java a-t-il produit le fichier ?");
  egal(PILOTES.length, 10);
  const trie = trierParPoints(PILOTES);
  egal(trie[0].nom, "VERSTAPPEN");
  egal(trie[0].points, 101);
  egal(trie.map((p) => p.nom).slice(0, 3), ["VERSTAPPEN", "LECLERC", "NORRIS"]);
  const ecuries = trierParPoints(ECURIES);
  egal(ecuries[0].nom, "Red Bull");
  egal(ecuries[0].points, 139);
  remplirTableau("corps-test", trie);
  egal(document.querySelectorAll("#corps-test tr").length, 10);
});

async function lancerTests() {
  const sortie = document.getElementById("resultats");
  sortie.innerHTML = "";
  let reussis = 0;
  for (const t of _tests) {
    reinitialiserDOM();
    let ok = true;
    let detail = "";
    try {
      await t.fn();
    } catch (erreur) {
      ok = false;
      detail = erreur && erreur.message ? erreur.message : String(erreur);
    }
    if (ok) reussis += 1;
    const ligne = document.createElement("li");
    ligne.className = ok ? "ok" : "ko";
    ligne.textContent = (ok ? "✅ " : "❌ ") + t.nom + (ok ? "" : "  →  " + detail);
    sortie.appendChild(ligne);
    console.log((ok ? "✅ " : "❌ ") + t.nom + (ok ? "" : "  →  " + detail));
  }
  const score = document.getElementById("score");
  score.textContent = `${reussis} / ${_tests.length} tests réussis`
      + (reussis === _tests.length ? " — maillon JS validé 🎉" : "");
  score.className = reussis === _tests.length ? "ok" : "ko";
  reinitialiserDOM();
  try {
    afficherSaison();
  } catch (erreur) {
    console.log("L'affichage de la saison ne fonctionne pas encore :", erreur.message);
  }
  return reussis;
}

if (typeof document !== "undefined" && document.getElementById("bac")) {
  if (document.readyState === "loading") {
    window.addEventListener("DOMContentLoaded", lancerTests);
  } else {
    lancerTests();
  }
}
