"""Vérification bout-en-bout de la chaîne — FOURNI, NE PAS MODIFIER.
   Exécution depuis la racine du pack :  python3 extensions/E4-chaine-complete/verifier_chaine.py
   Contrôle les trois maillons dans l'ordre et s'arrête au premier échec."""
import os
import subprocess
import sys

RACINE = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
CONTRAT1 = os.path.join(RACINE, "02-java", "courses_propres.csv")
DONNEES_JS = os.path.join(RACINE, "03-js", "donnees.js")
REFERENCE_JS = os.path.join(RACINE, "extensions", "E4-chaine-complete", "reference", "donnees.js")

ok_total = True


def etape(nom, controle):
    global ok_total
    try:
        controle()
        print(f"✅ {nom}")
    except Exception as err:
        ok_total = False
        print(f"❌ {nom}\n     → {err}")


def verifier_contrat1():
    if not os.path.exists(CONTRAT1):
        raise AssertionError("02-java/courses_propres.csv est absent : le maillon Python n'a pas produit sa sortie")
    with open(CONTRAT1, encoding="utf-8") as f:
        lignes = f.read().splitlines()
    assert lignes[0] == "course;pilote;ecurie;position;temps_tour", f"en-tête inattendue : {lignes[0]!r}"
    assert len(lignes) == 51, f"51 lignes attendues (en-tête comprise), {len(lignes)} trouvées"
    for numero, ligne in enumerate(lignes[1:], start=2):
        champs = ligne.split(";")
        assert len(champs) == 5, f"ligne {numero} : 5 champs attendus, {len(champs)} trouvés"
        int(champs[3])


def compiler_java():
    dossier = os.path.join(RACINE, "02-java")
    sources = [os.path.join("src", f) for f in sorted(os.listdir(os.path.join(dossier, "src"))) if f.endswith(".java")]
    resultat = subprocess.run(["javac", "-encoding", "UTF-8", "-d", "out"] + sources,
                              cwd=dossier, capture_output=True, text=True)
    assert resultat.returncode == 0, "la compilation Java échoue :\n" + resultat.stderr.strip()


def executer_java():
    dossier = os.path.join(RACINE, "02-java")
    resultat = subprocess.run(["java", "-Dstdout.encoding=UTF-8", "-cp", "out", "Main"],
                              cwd=dossier, capture_output=True, text=True)
    assert resultat.returncode == 0, "l'exécution de Main échoue :\n" + (resultat.stderr.strip() or resultat.stdout.strip())


def verifier_donnees_js():
    if not os.path.exists(DONNEES_JS):
        raise AssertionError("03-js/donnees.js est absent : le maillon Java n'a pas produit sa sortie")
    with open(DONNEES_JS, encoding="utf-8") as f:
        obtenu = f.read().strip()
    with open(REFERENCE_JS, encoding="utf-8") as f:
        attendu = f.read().strip()
    assert obtenu.startswith("const PILOTES"), "le fichier doit commencer par const PILOTES = [...]"
    if obtenu != attendu:
        for ligne_o, ligne_a in zip(obtenu.splitlines(), attendu.splitlines()):
            if ligne_o != ligne_a:
                raise AssertionError(f"contenu différent de la référence :\n     attendu : {ligne_a[:110]}...\n     obtenu  : {ligne_o[:110]}...")
        raise AssertionError("contenu différent de la référence")


if __name__ == "__main__":
    print("Vérification de la chaîne Python → Java → JS\n")
    etape("1. Contrat 1 : 02-java/courses_propres.csv", verifier_contrat1)
    etape("2. Compilation du maillon Java", compiler_java)
    etape("3. Exécution de Main (production de donnees.js)", executer_java)
    etape("4. Contrat 2 : 03-js/donnees.js conforme à la référence", verifier_donnees_js)
    print()
    if ok_total:
        print("🏁 Chaîne complète validée de bout en bout.")
    else:
        print("La chaîne n'est pas encore complète — corrigez le premier ❌ puis relancez.")
        sys.exit(1)
