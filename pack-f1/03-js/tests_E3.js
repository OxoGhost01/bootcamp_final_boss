/* Tests EXTENSION E3 — NE PAS MODIFIER. À copier dans 03-js/ et à inclure dans index.html. */

(function () {
  const JEU_E3 = [
    { nom: "AAA", ecurie: "Red Bull", points: 9, victoires: 0 },
    { nom: "BBB", ecurie: "Ferrari", points: 100, victoires: 3 },
    { nom: "CCC", ecurie: "Red Bull", points: 25, victoires: 1 },
    { nom: "DDD", ecurie: "Ferrari", points: 1, victoires: 0 },
  ];

  const GABARIT_E3 = `
    <table id="table-e3">
      <thead><tr>
        <th>#</th>
        <th data-colonne="nom">Pilote</th>
        <th>Écurie</th>
        <th data-colonne="points">Pts</th>
        <th data-colonne="victoires">V</th>
      </tr></thead>
      <tbody id="corps-e3"></tbody>
    </table>`;

  function preparer() {
    document.getElementById("bac").innerHTML = GABARIT_E3;
  }

  function nomsAffiches() {
    return [...document.querySelectorAll("#corps-e3 tr")].map((tr) => tr.dataset.nom);
  }

  function clicSur(colonne) {
    document.querySelector(`#table-e3 th[data-colonne="${colonne}"]`)
        .dispatchEvent(new MouseEvent("click", { bubbles: true, cancelable: true }));
  }

  test("E3-1. filtrerParEcurie", () => {
    egal(filtrerParEcurie(JEU_E3, "Red Bull").map((e) => e.nom), ["AAA", "CCC"]);
    egal(filtrerParEcurie(JEU_E3, "Ferrari").length, 2);
    egal(filtrerParEcurie(JEU_E3, "Mercedes"), []);
    egal(filtrerParEcurie(JEU_E3, "").length, 4);
    const avant = JEU_E3.map((e) => e.nom);
    filtrerParEcurie(JEU_E3, "Ferrari");
    egal(JEU_E3.map((e) => e.nom), avant);
  });

  test("E3-2. activerTri : colonnes numériques", () => {
    preparer();
    remplirTableau("corps-e3", JEU_E3);
    activerTri("table-e3", JEU_E3);
    clicSur("points");
    egal(nomsAffiches(), ["BBB", "CCC", "AAA", "DDD"]);
    clicSur("victoires");
    egal(nomsAffiches(), ["BBB", "CCC", "AAA", "DDD"]);
  });

  test("E3-3. activerTri : colonne alphabétique et clics répétés", () => {
    preparer();
    remplirTableau("corps-e3", JEU_E3);
    activerTri("table-e3", JEU_E3);
    clicSur("points");
    clicSur("nom");
    egal(nomsAffiches(), ["AAA", "BBB", "CCC", "DDD"]);
    clicSur("points");
    egal(nomsAffiches(), ["BBB", "CCC", "AAA", "DDD"]);
    egal(JEU_E3.map((e) => e.nom), ["AAA", "BBB", "CCC", "DDD"]);
  });
})();
