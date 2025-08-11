package de.hsmittweida.pawnstopower;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

/**
 * Diese Klasse dient zum Speichern des Spielfortschrittes.
 * Was gespeichert wird:
 * - Pawns, deren Fortschritt und Ausrüstung,
 * - Inventar
 * - Gold, Reputation und sonstige Spielerwerte
 */
public class Save {
    /**
     * Speichert alle notwendigen Daten
     *
     * @param path Wo die Datei mit dem Spielstand gespeichert werden soll.
     */
    public static void saveAll(String path) {
        /* Da die XP der Pawns mit SimpleInt gespeichert werden, sind diese nicht serialisierbar
         * und müssen daher manuell als int (serialisierbar) abgespeichert werden. */
        ArrayList<Integer> pawnXP = new ArrayList<Integer>();
        for(Pawn p : Inventory.getPawns()) {
            pawnXP.add(p.getXpAsInt());

            /* Da ein Pawn nach dem Laden nicht mehr das selbe Objekt wie vorher ist (Objektrefernz),
             * müssen alle Waffen und RÜstungen entrüstet werden, da die getOwner() Methode jedes
             * dieser Items, die alte Pawn-Referenz zurückgibt. */
            p.removeWeapon(p.getWeapon((byte) 0));
            p.removeWeapon(p.getWeapon((byte) 1));
            p.removeArmor(p.getArmor((byte) 0));
            p.removeArmor(p.getArmor((byte) 1));
            p.removeArmor(p.getArmor((byte) 2));
            p.removeArmor(p.getArmor((byte) 3));
        }

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
            e.printStackTrace();
        }
        finally {
            if(writer != null) writer.close();
        }

    }

    public static boolean loadAll(String path) {
        boolean errorTemp = false;
        Inventory.clear();

        ArrayList<Pawn> pawns = null;
        ArrayList<Integer> pawnXP = null;
        ArrayList<Weapon> weapons = null;
        ArrayList<Armor> armors = null;
        HashMap<Integer, ArrayList<String>> diaryEntries = null;
        int gold = 0;
        int rep = 0;
        int day = 0;

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
            e.printStackTrace();
            Tools.popup("Fehler beim Laden des Spielstands!", "Fehler", "Das Spiel kann nicht gestartet werden.");
            errorTemp = true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
            }
        }

        if(weapons != null && !weapons.isEmpty()) {
            for(Weapon w : weapons) {
                Inventory.addItem(w);
            }
        }

        if(armors != null && !armors.isEmpty()) {
            for(Armor r : armors) {
                Inventory.addItem(r);
            }
        }


        Inventory.addMoney(gold);
        Inventory.addReputation(rep);
        Game.setDay(day);
        Diary.setDiaryEntriesFromSeriliazable(diaryEntries);
        // Es fehlt noch: Tag, Tagebuch...


        /* Test-Ausgabe */
        System.out.println("Gold: " + gold);
        System.out.println("Reputation: " + rep);
        System.out.println("Day: " + day);
        System.out.println("Pawns: " + (pawns != null ? pawns.size() : 0));
        System.out.println("Weapons: " + (weapons != null ? weapons.size() : 0));
        System.out.println("Amors: " + (armors != null ? armors.size() : 0));
        return true;
    }

    public static String serialize(Object obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(obj);
        out.close();
        System.out.println("save: " + Base64.getEncoder().encodeToString(bos.toByteArray()));
        return Base64.getEncoder().encodeToString(bos.toByteArray());
    }

    public static Object deserialze(String code) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(code);
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data));
        Object obj = in.readObject();
        in.close();
        return obj;
    }
}

