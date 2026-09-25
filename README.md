# Small and fun project made in class

Works well to practice python, java and javascript, i liked this one.

A three-step pipeline that turns a raw Formula 1 season export (5 races, 10 drivers, 5 teams) into a web page with driver and team standings. Each step is written in a different language and hands a file to the next one:

```
donnees/resultats.csv
        ↓  Python      cleaning
02-java/courses_propres.csv
        ↓  Java        standings computation
03-js/donnees.js
        ↓  JavaScript  web page
```

## 1. Python: ingestion (`01-python/ingestion.ipynb`)

| Function | What it does |
|---|---|
| `temps_en_secondes(texte)` | Converts a lap time like `1:33.614` into seconds (`93.614`), rounded to the millisecond. Returns `None` for an empty string. |
| `lire_resultats(chemin)` | Reads the raw CSV and returns one dict per driver per race. A retirement becomes position `0` with no lap time. |
| `ecrire_courses_propres(chemin, lignes)` | Writes the clean CSV for the Java step. Lap times are written with 3 decimals, or left empty when missing. |

### Extension E1: dirty data (`extensions/E1-donnees-sales/teeeeeeeeeeestt.ipynb`)

A version of `lire_resultats` that cleans up the real timing export. It puts driver names in upper case and strips accents (`pérez` → `PEREZ`). It normalises team names through a lookup table (`"  RED BULL "` → `Red Bull`). It accepts `,` as the decimal separator. It drops exact duplicate lines, lines with no driver and lines with an unknown team.

## 2. Java: standings engine (`02-java/src/Classement.java`)

| Method | What it does |
|---|---|
| `pointsPourPosition(position)` | Points for a finishing position (25, 18, 15… down to 1). A finish outside the top 10 or a retirement scores 0. |
| `classementPilotes(lignes)` | Groups race results by driver and adds up points, wins and second places. The list is sorted by points, then wins, then second places, then name. |
| `classementEcuries(pilotes)` | Adds up the driver totals per team, with the same sort order. |
| `positionMoyenne(lignes, pilote)` | A driver's average finishing position, excluding retirements, rounded to 2 decimals. |

The sort order is defined once in a shared `Comparator` (`ORDRE`), which both standings use.

### Extension E2: fastest lap point

| Method | What it does |
|---|---|
| `auteurMeilleurTour(lignes, course)` | Returns the driver with the fastest lap of a race. Retired drivers count and missing times are ignored. |
| `classementAvecMeilleurTour(lignes)` | Driver standings plus 1 bonus point for the fastest lap, awarded only when that driver finished in the top 10. |

## 3. JavaScript: web page (`03-js/app.js`)

| Function | What it does |
|---|---|
| `trierParPoints(liste)` | Returns a new list sorted by points (descending), with wins as the tie-breaker. The input list is left untouched. |
| `remplirTableau(idCorps, liste)` | Fills a `<tbody>` with one row per entry: rank, name, team, points, wins. Each call replaces the previous content. |
| `marquerPodium(idCorps)` | Highlights the first three rows of a table. |

### Extension E3: interactive table

| Function | What it does |
|---|---|
| `filtrerParEcurie(liste, ecurie)` | Keeps only the entries of one team. An empty team name returns everything. |
| `activerTri(idTable, liste)` | Makes the column headers clickable. Clicking sorts by name (A→Z), points or wins (descending). |

## Running

```sh
# require running on a Linux system with basic packages and tools (for example Kira Linux xD)

./runall # basic script
./runallbutbeautiful # animated version by Claude
```
