package de.hsmittweida.pawnstopower;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;

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
        ArrayList<Pawn> pawns = new ArrayList<>();
        pawns.add(new Pawn());
        try (PrintWriter writer = new PrintWriter(path)) {
            writer.println(serializing(Inventory.getPawns()));
            writer.println(serializing(Inventory.getWeapons()));
            writer.println(serializing(Inventory.getArmor()));
            writer.println("GOLD=" + Inventory.getMoney());
            writer.println("TAG=" + Game.getDay());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void loadAll(String path) {

        ArrayList<Pawn> pawns = null;
        ArrayList<Weapon> waffen = null;
        ArrayList<Armor> ruestungen = null;
        int gold = 0;
        int tag = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            pawns = (ArrayList<Pawn>) deserializing(reader.readLine());
            waffen = (ArrayList<Weapon>) deserializing(reader.readLine());
            ruestungen = (ArrayList<Armor>) deserializing(reader.readLine());

            String goldLine = reader.readLine();
            if (goldLine != null && goldLine.startsWith("GOLD="))
                gold = Integer.parseInt(goldLine.substring(5));

            String tagLine = reader.readLine();
            if (tagLine != null && tagLine.startsWith("TAG="))
                tag = Integer.parseInt(tagLine.substring(4));

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Test-Ausgabe
        System.out.println("Gold: " + gold);
        System.out.println("Tag: " + tag);
        System.out.println("Krieger geladen: " + (pawns != null ? pawns.size() : 0));
        System.out.println("Waffen geladen: " + (waffen != null ? waffen.size() : 0));
        System.out.println("Rüstungen geladen: " + (ruestungen != null ? ruestungen.size() : 0));
    }

    public static String serializing(Object obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(obj);
        out.close();
        return Base64.getEncoder().encodeToString(bos.toByteArray());
    }

    public static Object deserializing(String code) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(code);
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data));
        Object obj = in.readObject();
        in.close();
        return obj;
    }
}

