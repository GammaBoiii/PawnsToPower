package de.hsmittweida.pawnstopower;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

/**
 * Diese Klasse dient zum Speichern und Laden des Spielfortschrittes.
 * Was gespeichert wird:
 * <ul>
 * <li>Pawns,
 * <li>Inventar
 * <li>Gold, Reputation und sonstige Spielerwerte
 * <li>Tagebuch
 * </ul>
 */
public class Save {
    /**
     * Speichert alle notwendigen Daten
     *
     * @param path Wo die Datei mit dem Spielstand gespeichert werden soll.
     * @return {@code true}, wenn alles fehlerfrei gespeichert wurde.
     */
    public static boolean saveAll(String path) {
        boolean errorTemp = false;

        /* Da die XP der Pawns mit SimpleInt gespeichert werden, sind diese nicht serialisierbar
         * und müssen daher manuell als int (serialisierbar) abgespeichert werden. */
        ArrayList<Integer> pawnXP = new ArrayList<Integer>();
        for(Pawn p : Inventory.getPawns()) {
            pawnXP.add(p.getXpAsInt());

            /* Da ein Pawn nach dem Laden nicht mehr das selbe Objekt wie vorher ist (Objektrefernz),
             * müssen alle Waffen und RÜstungen entrüstet werden, da die getOwner() Methode jedes
             * dieser Items, die alte Pawn-Referenz zurückgibt. */
//            p.removeWeapon(p.getWeapon((byte) 0));
//            p.removeWeapon(p.getWeapon((byte) 1));
//            p.removeArmor(p.getArmor((byte) 0));
//            p.removeArmor(p.getArmor((byte) 1));
//            p.removeArmor(p.getArmor((byte) 2));
//            p.removeArmor(p.getArmor((byte) 3));
        }

        /* Die folgenden zwei Schleifen generieren eine Vorlage für ausgerüstete Items,
        * sodass diese später beim Laden korrekt an die passenden Pawns gegeben werden können.
        * Siehe dazu auch Item.pushEquipLocation() und Item.generateEquipLocation().
        * In einem weiteren Schritt werden die jeweiligen Items aber zunächst noch entfernt,
        * da sich herausgestellt hat, dass die Pawns dann beim Laden teilweise noch die Referenz zu
        * den "alten" Items haben, welche dann nicht korrekt geladen werden können.
        * */
        for(Weapon w : Inventory.getWeapons()) {
            if(w.getOwner() != null) {
                w.generateEquipLocation(w.getOwner().getId(), Item.getSlotOfItem(w));
                w.getOwner().removeWeapon(w);
            }
        }
        for(Armor a : Inventory.getArmor()) {
            if(a.getOwner() != null) {
                a.generateEquipLocation(a.getOwner().getId(), Item.getSlotOfItem(a));
                a.getOwner().removeArmor(a);
            }
        }


        /* Nutzung des PrintWriter Objects, mit Hilfe der serialize() Methode.
        * Erlaubt einfaches schreiben in eine Datei, sofern die einzelnen Objekte
        * ersteinmal serialisiert sind.
        * Einzelne Werte, Gold, Reputation und Tage, werden mit einem Präfix gespeichert,
        * damit diese später einfacher ausgelesen werden können.
        * */
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(path);
            writer.println(serialize(Inventory.getPawns()));
            writer.println(serialize(pawnXP));
            writer.println(serialize(Inventory.getWeapons()));
            writer.println(serialize(Inventory.getArmor()));
            writer.println(serialize(Diary.getDiaryEntriesAsSerializable()));
            writer.println("GOLD=" + Inventory.getMoney());
            writer.println("REP=" + Inventory.getReputation().get());
            writer.println("TAG=" + Game.getDay());
        } catch (IOException e) {
            // e.printStackTrace();
            Tools.popup("Fehler beim Speichern des Spielstands!", "Fehler", "Das Spiel kann nicht gespeichert werden.\nPrüfe die Eingaben!");
            errorTemp = true;
        }
        finally {
            if(writer != null) writer.close();
        }
        return !errorTemp;
    }

    /**
     * Lädt alle Daten aus der Datei aus dem Path.
     * @param path Pfad, an dem die Datei liegen soll. Nur der Pfad in den Ordner der Datei.
     * @return {@code true}, wenn alles fehlerfrei geladen wurde.
     */
    public static boolean loadAll(String path) {
        boolean errorTemp = false;

        /* Falls noch nicht vorhanden, wird zunächst ein frisches
        * Inventar initiiert.
        * */
        if(Inventory.isInitialized()) {
            Inventory.clear();
        } else {
            Inventory.setup();
        }


        ArrayList<Pawn> pawns = null;
        ArrayList<Integer> pawnXP = null;
        ArrayList<Weapon> weapons = null;
        ArrayList<Armor> armors = null;
        HashMap<Integer, ArrayList<String>> diaryEntries = null;
        int gold = 0;
        int rep = 0;
        int day = 0;

        /* Mithilfe eines BufferedReaders werdn die Serialisierungen der Objekte aus der Datei gelesen
        * und anschließend mit der deserialize Methode wieder in ihre entsprechenden Objekte umgewandelt.
        * Die einzelnen Werte des Goldes, der Reputation und der Taganzahl können mithilfe
        * der Präfixe einfach ausgelesen werden. Der Rest wird per Reihenfolge (jeweils ein Objekt pro Zeile)
        * ausgelesen.
        * */
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(path));

            pawns = (ArrayList<Pawn>) deserialze(reader.readLine());
            pawnXP = (ArrayList<Integer>) deserialze(reader.readLine());
            weapons = (ArrayList<Weapon>) deserialze(reader.readLine());
            armors = (ArrayList<Armor>) deserialze(reader.readLine());
            diaryEntries = (HashMap<Integer, ArrayList<String>>) deserialze(reader.readLine());

            String goldLine = reader.readLine();
            if (goldLine != null && goldLine.startsWith("GOLD="))
                gold = Integer.parseInt(goldLine.substring(5));

            String repLine = reader.readLine();
            if (repLine != null && repLine.startsWith("REP="))
                rep = Integer.parseInt(repLine.substring(4));

            String tagLine = reader.readLine();
            if (tagLine != null && tagLine.startsWith("TAG="))
                day = Integer.parseInt(tagLine.substring(4));

        } catch (IOException e) {
            // e.printStackTrace();
            Tools.popup("Fehler beim Laden des Spielstands!", "Fehler", "Das Spiel kann nicht gestartet werden.\nPrüfe die Eingaben!");
            errorTemp = true;
        } catch (ClassNotFoundException e) {
            // e.printStackTrace();
            Tools.popup("Fehler beim Laden des Spielstands!", "Fehler", "Das Spiel kann nicht gestartet werden.\nDer Spielstand scheint korrupt zu sein.");
            errorTemp = true;
        }
        finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if(errorTemp) return false;

        /* Alles in das Spiel hineinladen */
        if(pawns != null && !pawns.isEmpty()) {
            for(int i = 0; i < pawns.size(); i++) {
                Inventory.addPawn(pawns.get(i));
                pawns.get(i).addXp(pawnXP.get(i));
                System.out.println("pawn hier: " + pawns.get(i).getName() + " hash: " + pawns.get(i));
            }
        }

        /* Die Items, insbesondere die, die beim Speichern an einem Pawn ausgerüstet waren,
        * werden mit den folgenden Anweisungsblöcken an ihren entsprechenden Pawns zurück
        * ausgerüstet.
        * */
        if(weapons != null && !weapons.isEmpty()) {
            for(Weapon w : weapons) {
                Inventory.addItem(w);
                if(w.getEquipLocation() != null) {
//                    System.out.println("firstowner:" + w.getOwner());
                    w.pushEquipLocation(w);
//                    System.out.println("Name of weapon: " + w.getName() + " Owner: " + w.getOwner());
                }
            }
        }

        if(armors != null && !armors.isEmpty()) {
            for(Armor a : armors) {
                Inventory.addItem(a);
                if(a.getEquipLocation() != null) {
                    System.out.println("firstowner:" + a.getOwner());
                    a.pushEquipLocation(a);
                    System.out.println("Name of weapon: " + a.getName() + " Owner: " + a.getOwner());
                }
            }
        }

        /* Einzelne Werte hinzufügen */
        Inventory.addMoney(gold);
        Inventory.addReputation(rep);
        Game.setDay(day);
        Diary.setDiaryEntriesFromSeriliazable(diaryEntries);

        /* Test-Ausgabe */
        System.out.println("Gold: " + gold);
        System.out.println("Reputation: " + rep);
        System.out.println("Day: " + day);
        System.out.println("Pawns: " + (pawns != null ? pawns.size() : 0));
        System.out.println("Weapons: " + (weapons != null ? weapons.size() : 0));
        System.out.println("Amors: " + (armors != null ? armors.size() : 0));
        return true;
    }

    /**
     * Methode zum Serialisieren eines Objekts.
     * Nutzt ein Base64 Enkodierer auf den ObjectOutputStream.
     * @param obj Objekt, welches serialisiert werden soll.
     * @return {@code String} - Serialisierte String-Version des Objekts.
     * @throws IOException, wenn etwas beim Serialisieren schiefgeht.
     */
    public static String serialize(Object obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(obj);
        out.close();
        System.out.println("save: " + Base64.getEncoder().encodeToString(bos.toByteArray()));
        return Base64.getEncoder().encodeToString(bos.toByteArray());
    }

    /**
     * Methode zum Deserialisieren eines Objekts.
     * Umgekehrtes Verfahren zur Serialisierung, hier mit ObjectInputStream.
     * @param code Serialisierte String-Version des Objekts.
     * @return {@code Object} - Deserialisierte Version des Objekts.
     * @throws IOException, wenn etwas beim Deserialisieren schiefgeht.
     * @throws ClassNotFoundException, wenn das Objekt nicht gefunden werden konnte.
     */
    public static Object deserialze(String code) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(code);
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data));
        Object obj = in.readObject();
        in.close();
        return obj;
    }
}

