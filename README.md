# Catch-game
Hra obsahující canvas a zvuky. Cílem hry je chytat padající objekty (dobré/špatné, za které se získávají/ztrácejí body). Hra bude mít menu s gesty, persistent storage pro nastavení a SQLite pro high score.

Shrnutí konečné verze:
1. Advanced GUI - Listy a adaptéry, fragmenty
2. Concurrency - Thread (hlavní thread na kterém běží hra)
3. Database - SQLite (ukládání skóre)
4. Multimedia - Audio (hudba, zvuky) ... spojené s využítím SERVICE
5. 2D Game Canvas - SurfaceView spojen s Canvasem (herní plocha)
6. Persistent storage - SharedPreferences (ukládání hlasitosti, minule hraných obtížností)
