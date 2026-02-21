package de.hsmittweida.pawnstopower;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Die Diary-Klasse dient dem Loggen von Spielgeschehnissen im Tagebuch an der
 * rechten Seite.
 */
public class Diary {
    private static int oldMoney, oldPawnNum, oldReputation;
    private static HashMap<Integer, ArrayList<Text>> diaryEntries;
    private static boolean skipDailyGains = false;

    /**
     * Schreibt einen String in das Tagebuch.
     * @param s String, der im Tagebuch hinterlegt werden soll.
     */
    public static void writeDiaryEntry(String s) {
        Text t = new Text(s + "\n");
        t.setStyle("-fx-font-size: 36px;");
        Game.getDiary().getChildren().add(t);
        saveContext();
    }

    /**
     * Gibt einen kurzen Log zum letzten Arena-Kampf aus. Je nachdem, ob der Spieler
     * siegreich war oder nicht, gibt es einen anderen, zufälligen Text.
     * @param won {@code true}, wenn der Spieler den Arena-Kampf gewonnen hat.
     * @param params Parameter für den Text
     * @deprecated Ersetzt durch {@code ArenaWinScreen.generateArenaDiaryMsg(boolean won)}. Weiterhin
     *              enthalten für Erweiterungszwecke.
     * @see ArenaWinScreen#generateArenaDiaryMsg(boolean)
     */
    public static void logArenaFight(boolean won, String... params) {
        String[] msg;
        if (won) {
            msg = new String[]{
                    "Der Kampf in der Arena war erfolgreich. Unser Kämpfer konnte sich durchsetzen und brachte Ruhm und Gold mit zurück.",
                    "Wir haben in der Arena einen triumphalen Sieg errungen. Der Gegner hatte keine Chance.",
                    "In der Arena wurde heute Geschichte geschrieben. Unser Kämpfer verließ sie als Sieger.",
                    "Die Arena erbebte vor Jubel, als unser Held seinen Gegner bezwang.",
                    "Mit diesem Sieg in der Arena haben wir Stärke bewiesen, die allen im Gedächtnis bleiben wird.",
                    "Unser Krieger hat die Arena als wahrer Champion verlassen. Seine Klinge war schneller und sein Wille stärker.",
                    "Der Schweiß und das Training haben sich ausgezahlt. Die Arena gehört heute uns.",
                    "Kein Zweifel bleibt zurück. Dieser Sieg in der Arena war verdient und eindrucksvoll.",
                    "Selbst die Zuschauer waren sprachlos angesichts unseres triumphierenden Kämpfers.",
                    "So ein dominanter Sieg in der Arena stärkt unser Ansehen im ganzen Reich."
            };
        } else {
            msg = new String[]{
                    "Wir haben in der Arena eine bittere Niederlage erlitten. Unser Kämpfer wurde zu Boden gezwungen.",
                    "Der heutige Kampf in der Arena ging nicht zu unseren Gunsten aus.",
                    "Unser Gegner war in der Arena überlegen. Wir mussten uns geschlagen geben.",
                    "Die Arena hat kein Erbarmen gezeigt. Unser Kämpfer verlor den Kampf.",
                    "Diese Niederlage in der Arena schmerzt. Doch wir werden zurückkehren.",
                    "Unser Kämpfer kämpfte tapfer. Aber die Arena war heute gnadenlos.",
                    "Die Zuschauer sahen unser Scheitern. In der Arena zählt nur der Sieg.",
                    "Wir haben in der Arena alles gegeben. Doch es reichte nicht.",
                    "Unsere Niederlage in der Arena wird uns eine Lehre sein.",
                    "Beim nächsten Mal werden wir besser vorbereitet sein. Die Arena wird uns wiedersehen."
            };
        }
        writeDiaryEntry(msg[new Random().nextInt(msg.length)]);
    }

    /**
     * Diese Methode wird immer ausgeführt, wenn das Spiel einen Tag vorranschreitet.
     */
    public static void newDay() {
        /* Countervariable, die letztendlich prüfen soll, ob am Ende des Tages etwas passiert ist oder nicht. */
        byte counter = 0;
        String msg = "Ein neuer Tag (" + Game.getDay() + ") beginnt.";

        /* Da der der Spieler quasi am ersten Tag (vor Spielbeginnn) sein Inventar gefüllt bekommt,
         * darf dies nicht geloggt werden. */
        if (Game.getDay() == 1 || skipDailyGains) {
            msg += "\n\n";
            oldPawnNum = Inventory.getPawnsNum().getValue();
            oldMoney = Inventory.getMoney();
            skipDailyGains = false;
            writeDiaryEntry(msg);
            return;
        }

        /* Geld dazuverdient oder verloren */
        if (Math.abs(Inventory.getMoney() - oldMoney) != 0) {
            String m1 = "Wir haben am letzten Tag " + (Inventory.getMoney() - oldMoney) + " Gold eingenommen.\n\n";
            String m2 = "Wir haben am letzten Tag " + (oldMoney - Inventory.getMoney()) + " Gold aus der Schatzkammer verbraucht.\n\n";

            msg += Inventory.getMoney() > oldMoney ? m1 : m2;
            counter++;
        }

        /* Neue Kämpfer dazugewonnen */
        if (Math.abs(Inventory.getPawnsNum().getValue() - oldPawnNum) == 1) {
            msg += "Ein neuer Krieger begleitet uns: \n\t";
            msg += Inventory.getPawns().get(Inventory.getPawns().size() - 1).getName() + ".\n";
            counter++;
        } else if (Math.abs(Inventory.getPawnsNum().getValue() - oldPawnNum) == 2) {
            msg += "Zwei neue Krieger begleiten uns: \n\t";
            for (int i = 1; i <= (Inventory.getPawnsNum().getValue() - oldPawnNum); i++) {
                if (i == Inventory.getPawnsNum().getValue() - oldPawnNum) {
                    msg += " und ";
                }
                msg += Inventory.getPawns().get(Inventory.getPawns().size() - i).getName();
            }
            counter++;
        } else if (Math.abs(Inventory.getPawnsNum().getValue() - oldPawnNum) > 2) {
            msg += (Inventory.getPawnsNum().getValue() - oldPawnNum) + " neue Krieger begleiten uns: \n\t";
            for (int i = 1; i <= (Inventory.getPawnsNum().getValue() - oldPawnNum); i++) {
                if (i == Inventory.getPawnsNum().getValue() - oldPawnNum - 1) {
                    msg += Inventory.getPawns().get(Inventory.getPawns().size() - (i)).getName() + " und " + Inventory.getPawns().get(Inventory.getPawns().size() - (i + 1)).getName();
                    break;
                }
                msg += Inventory.getPawns().get(Inventory.getPawns().size() - i).getName() + ", ";

            }
            counter++;
        }

        /* Reputation/Ehre gewonnen oder verloren
        * Erklärung der extra Überprüfungen:
        *   day:        Geprüft werden soll erst nach dem ersten Tageswechsel, da sonst falsche Informationen
        *               durch die ursprüngliche Initialisierung geprinted werden.
        *   counter:    Es soll nicht diese Nachricht und die Nachricht des "Nichts-Tun's" (letztes if
        *               dieser Methode) gleichzeitig im Tagebuch erscheinen, da es sonst nicht schön aussieht.
         */
        if (Math.abs(Inventory.getReputation().get() - oldReputation) != 0 && Game.getDay() != 2 && counter > 0) {
            String m1 = "Wir haben am letzten Tag " + (Inventory.getReputation().get() - oldReputation) + " Reputation erhalten.\n\n";
            String m2 = "Wir haben am letzten Tag " + (oldReputation - Inventory.getReputation().get()) + " Reputation verloren.\n\n";

            msg += Inventory.getMoney() > oldMoney ? m1 : m2;
        }

        msg += "\n\n";
        oldPawnNum = Inventory.getPawnsNum().getValue();
        oldMoney = Inventory.getMoney();
        oldReputation = Inventory.getReputation().get();

        /* Falls man Tage nur sinnlos überspringt, geht Reputation verloren. */
        if(counter <= 0) {
            int minusRep = (int) (Math.ceil(Inventory.getReputation().get() * -0.1));
            Inventory.addReputation(minusRep);
            msg += "Wir haben am letzten Tag nichts erreicht und daher " + Math.abs(minusRep) + " Reputation verloren.\n\n";
        }

        writeDiaryEntry(msg);
    }

    /**
     * Speichert alle Tagebucheinträge, entsprechend der Tage ab,
     * sodass diese immer durchlaufen werden können.
     * Erlaubt dem Spieler also die Einträge der vergangenen Tage zu sehen.
     * @param day Tag, an dem ein Eintrag abgespeichert werden soll.
     * @param entry Der Eintrag, der an dem Tag abgespeichert werden soll.
     * @deprecated
     */
    public static void saveContext(int day, TextFlow entry) {
        if (Diary.diaryEntries == null) {
            diaryEntries = new HashMap<Integer, ArrayList<Text>>();
        }
        diaryEntries.remove(day - 1);
        for (Node n : entry.getChildren()) {
            diaryEntries.get(day - 1).add((Text) n);
        }
        entry.getChildren().clear();
    }

    /**
     * Speichert alle Tagebucheinträge, entsprechend der Tage ab,
     * sodass diese immer durchlaufen werden können.
     * Erlaubt dem Spieler also die Einträge der vergangenen Tage zu sehen.
     */
    public static void saveContext() {
        /* Initialisierung, um NullPointerExceptions zu vermeiden. */
        if (diaryEntries == null) {
            diaryEntries = new HashMap<Integer, ArrayList<Text>>();
        }
        if (diaryEntries.get(Game.getDay() - 1) == null) {
            diaryEntries.put(Game.getDay() - 1, new ArrayList<Text>());
        }
        diaryEntries.get(Game.getDay() - 1).clear();
        for (Node n : Game.getDiary().getChildren()) {
            diaryEntries.get(Game.getDay() - 1).add((Text) n);
        }
    }
    /**
     * Returned eine HashMap, die alle Tagebucheinträge entsprechend der Tage beinhaltet.
     * @return {@code HashMap<Integer, ArrayList<Text>>}
     */
    public static HashMap<Integer, ArrayList<Text>> getDiaryEntries() {
        return diaryEntries;
    }


    /**
     * Da die Tagebucheinträge gespeichert werden sollen, müssen diese in die passende Form
     * gebracht werden. Dazu werden zunächst die Text-Objekte in String Objekte umgewandelt, da
     * Strings sich besser serialisieren lassen.
     * Returned eine HashMap mit den Tagebucheinträgen, die serialisierbar sind.
     * @see Diary#setDiaryEntriesFromSeriliazable(HashMap)
     * @return {@code HashMap<Integer, ArrayList<String>>}
     */
    public static HashMap<Integer, ArrayList<String>> getDiaryEntriesAsSerializable() {
        HashMap<Integer, ArrayList<String>> entries = new HashMap<Integer, ArrayList<String>>();
        ArrayList<String> temp;
        for(int i = 0; i<diaryEntries.size(); i++) {
            temp = new ArrayList<>();
            for(int j = 0; j<diaryEntries.get(i).size(); j++) {
                String text = ((Text) diaryEntries.get(i).get(j)).getText();
                System.out.println("-: " + text);
                temp.add(text);
            }
            entries.put(i, temp);
        }
        return entries;
    }

    /**
     * Wenn die Tagebucheinträge wieder geladen werden sollen, müssen diese wieder in Text-Objekte umgewandelt werden.
     * Diese Aufgabe wird von der folgenden Methode übernommen.
     * @see Diary#getDiaryEntriesAsSerializable()
     * @param map {@code HashMap<Integer, ArrayList<String>>}  mit den Tagebucheinträgen in String-Form.
     */
    public static void setDiaryEntriesFromSeriliazable(HashMap<Integer, ArrayList<String>> map) {
        diaryEntries.clear();
        Game.getDiary().getChildren().clear();
        Game.refreshDiaryIndex();
        for(int i = 0; i<map.size(); i++) {
            ArrayList<String> temp = map.get(i);
            ArrayList<Text> list = new ArrayList<>();
            for(int j = 0; j<temp.size(); j++) {
                list.add(new Text(temp.get(j)));
            }
            diaryEntries.put(i, list);
        }

        for(String s : map.get(Game.getDay() - 1)) {
            writeDiaryEntry(s);
        }

        skipDailyGains = true;
    }
}
