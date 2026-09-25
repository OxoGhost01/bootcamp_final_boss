# ✅ Tests EXTENSION E1 — collez cette cellule à la fin de votre notebook, puis exécutez-la
import os, unicodedata

DOSSIER_E1 = "../extensions/E1-donnees-sales"
ENTREE_SALE = f"{DOSSIER_E1}/donnees/resultats.csv"
REFERENCE = f"{DOSSIER_E1}/reference/courses_propres.csv"
SORTIE_E1 = f"{DOSSIER_E1}/ma_sortie.csv"

def _test_e1():
    lignes = lire_resultats(ENTREE_SALE)
    ecrire_courses_propres(SORTIE_E1, lignes)
    with open(SORTIE_E1, encoding="utf-8") as f:
        obtenu = f.read().splitlines()
    with open(REFERENCE, encoding="utf-8") as f:
        attendu = f.read().splitlines()
    if len(obtenu) != len(attendu):
        raise AssertionError(f"{len(attendu)} lignes attendues (en-tête comprise), {len(obtenu)} obtenues")
    for numero, (o, a) in enumerate(zip(obtenu, attendu), start=1):
        if o != a:
            raise AssertionError(f"ligne {numero} : attendu {a!r}, obtenu {o!r}")

verifier("E1. données sales", _test_e1)
