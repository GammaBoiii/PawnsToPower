package de.hsmittweida.pawnstopower;

import javafx.scene.text.Text;

import java.util.Random;

public class Diary {
    private static int oldMoney, oldPawnNum;

    private static void writeDiaryEntry(String s) {
        Game.getDiary().getChildren().add(new Text("» \t " + s + "\n"));
        System.out.println("» " + s);
    }

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

    public static void newDay() {
        String msg = "Ein neuer Tag beginnt.";
  
        if (Math.abs(Inventory.getMoney() - oldMoney) != 0) {
            if(Inventory.getMoney() > oldMoney) {
                msg += "Wir haben am letzten Tag " + (Inventory.getMoney() - oldMoney) + " Gold verdient.\n";
            } else {
                msg += "Wir haben am letzten Tag " + (oldMoney - Inventory.getMoney()) + " Gold verloren.\n";
            }
        }
        if(Math.abs(Inventory.getPawnsNum().getValue() - oldPawnNum) != 0) {
            msg += (Inventory.getPawnsNum().getValue() - oldPawnNum) + " neue Krieger begleiten uns: \n\t";
            for(int i = 1; i <= (Inventory.getPawnsNum().getValue() - oldPawnNum); i++) {
                if(i == Inventory.getPawnsNum().getValue() - oldPawnNum) { msg += " und ";}
                msg += Inventory.getPawns().get(Inventory.getPawns().size()-i).getName()+", ";
            }
        }

        oldPawnNum = Inventory.getPawnsNum().getValue();
        oldMoney = Inventory.getMoney();
        writeDiaryEntry(msg);
    }

}
