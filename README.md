#Pawns to Power

##Ein JavaFX-Projekt

Dieses Projekt entstand im Rahmen des Java Programmierbelegs an der Hochschule Mittweida

### Spielidee
In diesem Spiel sammelt der Spieler neue Kämpfer, mit denen er in der Arena als Gladiator antreten kann. Die Kämpfer können individuell ausgerüstet und mit Skillpunkten versehen werden. Dabei erreichen sie, nach gesammelter Erfahrung in der Arena, neue Level.

### Arena & Kampfsystem
Das Kampfsystem in der Arena ist komplett dynamisch. Die Gegnergenerierung findet basierend auf der Entwicklungsstufe des eigenen Kämpfers statt. Dabei kann es mit leichten Abweichungen zu schwereren oder leichteren Kämpfen kommen. Allerdings gewinnt man nicht nur, indem man das bessere Level hat. Auch die Ausrüstung spielt eine große Rolle. Weiterhin wurden vereinzelte Randomisierungen eingebaut, sodass der Kampf bis zum Ende hin spannend bleibt.
Hat der Spieler in der Arena gewonnen, so besteht eine Möglichkeit den eben besiegten Gegner in sein Team aufzunehmen. Außerdem erhält der Kämpfer beim Sieg natürlich Erfahrungspunkte und der Spieler erhält Reputation und Geld für sein Konto. Die Reputation zu erhöhen kann durchaus von Vorteil sein, da man somit öfter an einfacherer Gegner kommt. Beim Verlieren erhält der Kämpfer natürlich dennoch Erfahrung, allerdings verliert der Spieler diesmal Geld und Reputation.

### Tage(-buch)
Hat man einen Tag mit vielen Aktionen verbracht, kann man diesen beenden, und erhält eine grobe Zusammenfassung im Tagebuch. Sollte man sinnlos Tage überspringen, so wird dies mit einem Reputationsabzug bestraft. Daher sollte man möglichst viel an einem Tag tun.
Das Tagebuch bringt weiterhin kleine Informationen im Spielverlauf mit ein. Mit den Knöpfen am unteren Rand, lassen sich auch ältere Einträge lesen.
Sowohl die Stats des Spielers, Kämpfer und ihre Ausrüstung, als auch das Tagebuch werden vollumfänglich mit vom Speichersystem abgespeichert, und lassen sich bei bedarf in ein neues Spiel hineinladen.

### Zukunftsideen
* **Trainigslager**, in dem Kämpfer unabhänig von der Arena trainieren können, und passive Erfahrungspunkte sammeln
* **Verletzungssystem**, bei dem Kämpfer nach Arena-Kämpfen Verletzungen erleiden, und von diesen im Kampf beeinträchtigt werden. Verletzungen könnten nach Körperregion und Verletzungstyp auftreten
* **Zufallevents**, die unerwartet auftreten und den Spielverlauf leicht beeinflussen können
