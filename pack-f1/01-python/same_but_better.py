# # 🏁 Maillon 1 — Python : ingestion des résultats
#
# Vous recevez `../donnees/resultats.csv`, l'export brut d'un championnat de F1 (5 courses, 10 pilotes) :
#
# ```
# course;pilote;ecurie;position;meilleur_tour;statut
# Bahrein;VERSTAPPEN;Red Bull;1;1:33.614;ARRIVE
# Bahrein;HAMILTON;Mercedes;;;ABANDON
# ```
#
# **Votre mission** : produire `../02-java/courses_propres.csv` au format du **CONTRAT 1**, que le maillon Java consommera :
#
# ```
# course;pilote;ecurie;position;temps_tour
# Bahrein;VERSTAPPEN;Red Bull;1;93.614
# Bahrein;HAMILTON;Mercedes;0;
# ```
#
# Deux transformations seulement :
# 1. le chronomètre `1:33.614` devient un nombre de secondes `93.614` ;
# 2. un abandon devient la **position 0** et un temps **vide** (la colonne `statut` disparaît).
#
# **Méthode** : complétez les trois fonctions, exécutez la cellule de tests jusqu'au 4/4, puis lancez la
# cellule de production qui écrit le vrai fichier pour le maillon Java.


# ## 1. Convertir un chronomètre en secondes


def temps_en_secondes(texte):
    """'1:33.996' -> 93.996 (float, arrondi à 3 décimales).
    Une chaîne vide ou ne contenant que des espaces -> None."""
    # Oui c peut-être trop compliqué, mais ca ne fait qu'une seule ligne :P
    return round(float(texte.split(':')[0]) * 60 + float(texte.split(':')[1]), 3) if texte.strip(" ") else None


# ## 2. Lire le fichier brut


def lire_resultats(chemin):
    """Lit le CSV brut et renvoie une liste de dictionnaires :
    {"course": str, "pilote": str, "ecurie": str, "position": int, "temps_tour": float|None}
    - position : l'entier du CSV, ou 0 si le statut est ABANDON
    - temps_tour : converti avec temps_en_secondes (None si absent)
    La ligne d'en-tête ne doit pas figurer dans le résultat."""
    with open(chemin) as f:
        lignes = f.readlines()
    resultats = []
    for ligne in lignes[1:]:
        ligne = ligne.strip()
        if not ligne:
            continue
        champs = ligne.split(";")
        resultats.append({
            "course": champs[0],
            "pilote": champs[1],
            "ecurie": champs[2],
            "position": int(champs[3]) if champs[3] else 0,
            "temps_tour": temps_en_secondes(champs[4]) if champs[4] else None,
        })
    return resultats


# ## 3. Écrire le fichier du contrat 1


def ecrire_courses_propres(chemin, lignes):
    """Écrit le CONTRAT 1 : en-tête course;pilote;ecurie;position;temps_tour
    - temps_tour est écrit avec 3 décimales, ou vide si None
    - les lignes sont écrites dans l'ordre reçu"""
    with open(chemin, "w", encoding="utf-8") as f:
        f.write("course;pilote;ecurie;position;temps_tour\n")
        for ligne in lignes:
            f.write(f"{ligne['course']};{ligne['pilote']};{ligne['ecurie']};{ligne['position']};{'' if ligne['temps_tour'] is None else f'{ligne['temps_tour']:.3f}'}\n")


# ## ✅ Tests


# ✅ Tests — exécutez cette cellule (ne pas modifier)
import os, tempfile

_resultats = {}

def _egal(obtenu, attendu):
    assert obtenu == attendu, f"attendu {attendu!r}, obtenu {obtenu!r}"

def verifier(nom, controle):
    try:
        controle()
        _resultats[nom] = True
        print(f"✅ {nom}")
    except Exception as err:
        _resultats[nom] = False
        print(f"❌ {nom} → {type(err).__name__} : {err}")

CSV_TEST = (
    "course;pilote;ecurie;position;meilleur_tour;statut\n"
    "Bahrein;VERSTAPPEN;Red Bull;1;1:33.996;ARRIVE\n"
    "Bahrein;LECLERC;Ferrari;2;1:34.211;ARRIVE\n"
    "Bahrein;HAMILTON;Mercedes;;;ABANDON\n"
)

def _fichier(contenu=""):
    chemin = os.path.join(tempfile.mkdtemp(), "f.csv")
    with open(chemin, "w", encoding="utf-8") as f:
        f.write(contenu)
    return chemin

def _test_temps():
    _egal(temps_en_secondes("1:33.996"), 93.996)
    _egal(temps_en_secondes("0:59.500"), 59.5)
    _egal(temps_en_secondes("2:00.000"), 120.0)
    _egal(temps_en_secondes(""), None)
    _egal(temps_en_secondes("   "), None)

def _test_lire():
    lignes = lire_resultats(_fichier(CSV_TEST))
    _egal(len(lignes), 3)
    _egal(lignes[0], {"course": "Bahrein", "pilote": "VERSTAPPEN", "ecurie": "Red Bull",
                      "position": 1, "temps_tour": 93.996})
    _egal(lignes[2]["position"], 0)
    _egal(lignes[2]["temps_tour"], None)
    _egal(type(lignes[1]["position"]).__name__, "int")

def _test_ecrire():
    chemin = _fichier("ancien contenu\n")
    ecrire_courses_propres(chemin, [
        {"course": "Bahrein", "pilote": "VERSTAPPEN", "ecurie": "Red Bull", "position": 1, "temps_tour": 93.996},
        {"course": "Bahrein", "pilote": "HAMILTON", "ecurie": "Mercedes", "position": 0, "temps_tour": None},
    ])
    with open(chemin, encoding="utf-8") as f:
        contenu = f.read()
    _egal(contenu,
          "course;pilote;ecurie;position;temps_tour\n"
          "Bahrein;VERSTAPPEN;Red Bull;1;93.996\n"
          "Bahrein;HAMILTON;Mercedes;0;\n")

def _test_chaine():
    entree = _fichier(CSV_TEST)
    sortie = _fichier()
    ecrire_courses_propres(sortie, lire_resultats(entree))
    with open(sortie, encoding="utf-8") as f:
        lignes = f.read().splitlines()
    _egal(len(lignes), 4)
    _egal(lignes[1], "Bahrein;VERSTAPPEN;Red Bull;1;93.996")
    _egal(lignes[3], "Bahrein;HAMILTON;Mercedes;0;")

verifier("1. temps_en_secondes", _test_temps)
verifier("2. lire_resultats", _test_lire)
verifier("3. ecrire_courses_propres", _test_ecrire)
verifier("4. chaîne complète", _test_chaine)

reussis = sum(1 for ok in _resultats.values() if ok)
print(f"\n{reussis} / {len(_resultats)} tests réussis" + (" — maillon Python validé 🎉" if reussis == 4 else ""))


# ## 🏁 Production du fichier pour le maillon Java


# 🏁 Production du fichier réel (à exécuter une fois les 4 tests au vert)
ENTREE = "../donnees/resultats.csv"
SORTIE = "../02-java/courses_propres.csv"

lignes = lire_resultats(ENTREE)
ecrire_courses_propres(SORTIE, lignes)

print(f"{len(lignes)} lignes lues, fichier écrit : {SORTIE}\n")
for ligne in lignes[:5]:
    print(ligne)
abandons = [l for l in lignes if l["position"] == 0]
print(f"\n{len(abandons)} abandons :", [l["pilote"] for l in abandons])
