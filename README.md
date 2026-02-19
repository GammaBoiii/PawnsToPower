# ⚔️ Pawns to Power
### Ein JavaFX-basiertes Gladiator-RPG
**Projektkontext:** Erstellt im Rahmen des Java-Programmierbelegs an der **Hochschule Mittweida**.

---

## 📖 Projektübersicht
In **Pawns to Power** schlüpft der Spieler in die Rolle eines Managers in der antiken Arena. Ziel ist es, eine schlagkräftige Truppe von Kämpfern aufzubauen, sie strategisch auszurüsten und durch Siege in der Arena zu Ruhm und Reichtum zu gelangen.



---

## 🕹️ Kernmechaniken

### 1. Kämpfer-Management & Progression
* **Individuelle Entwicklung:** Kämpfer sammeln in der Arena Erfahrungspunkte (**XP**) und steigen im Level auf.
* **Skill-System:** Bei einem Level-Up können Skillpunkte frei verteilt werden, um die Kämpfer spezialisiert auszubilden.
* **Ausrüstung:** Ein dynamisches Item-System erlaubt es, Kämpfer individuell auszustatten. Die Ausrüstung hat dabei massiven Einfluss auf die Kampfstärke.

### 2. Dynamisches Kampfsystem
* **Smart Balancing:** Das System generiert Gegner basierend auf der aktuellen Entwicklungsstufe des eigenen Kämpfers. Leichte Abweichungen sorgen für Abwechslung.
* **Strategie vs. Zufall:** Während Level und Gear die Basis bilden, sorgen Randomisierungs-Algorithmen (wie Crit-Chancen) für Spannung bis zum letzten Schlag.
* **Rekrutierung:** Nach einem Sieg besteht die Chance, den unterlegenen Gegner direkt in das eigene Team aufzunehmen.

### 3. Ökonomie & Reputation
* **Belohnungen:** Siege bringen XP, Geld und **Reputation**.
* **Reputations-Effekt:** Eine hohe Reputation erleichtert den Zugang zu schwächeren Gegnern (Vorteil durch Bekanntheit).
* **Konsequenzen:** Niederlagen kosten Geld und Ruf. Werden Tage "sinnlos" übersprungen, folgt ein Reputationsabzug – Aktivität wird belohnt!

---

## 📝 Das Tagebuch-System
Das integrierte Tagebuch dient als zentrales Feedback-Tool:
* **Tageszusammenfassungen:** Alle wichtigen Ereignisse werden am Ende eines Tages protokolliert.
* **Historie:** Über Navigationsbuttons am unteren Rand können auch ältere Einträge jederzeit eingelesen werden.
* **Storytelling:** Kleine Informationen im Spielverlauf vertiefen die Atmosphäre der Spielwelt.

---

## 💾 Technische Highlights
* **JavaFX UI:** Eine intuitive Benutzeroberfläche, die Spielmechaniken und Statistiken sauber trennt (MVC-Ansatz).
* **Vollständige Persistenz:** Ein robustes Speichersystem sichert alle relevanten Daten:
    * Spieler-Statistiken & Kontostand.
    * Kämpfer-Attribute & deren Ausrüstung.
    * Die komplette Historie des Tagebuchs.
* **Lade-Funktion:** Spielstände können jederzeit in neue Sessions geladen werden.

---

## 🛠️ Ausblick & Erweiterungsmöglichkeiten
* **Permadeath:** Ein Modus, in dem Kämpfer bei einer Niederlage endgültig verloren gehen.
* **Event-System:** Zufällige Ereignisse (z.B. Verletzungen oder Sonderangebote beim Händler).
* **Turnier-Modus:** Größere Events mit mehreren Kämpfen hintereinander für massive Reputationssprünge.
