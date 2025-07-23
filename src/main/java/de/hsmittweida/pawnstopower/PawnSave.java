package de.hsmittweida.pawnstopower;

import java.util.ArrayList;

public class PawnSave {
    public static ArrayList<PawnSave> getPawnSave() {
        ArrayList<PawnSave> pawns = new ArrayList<>();
        for(Pawn p : Inventory.getPawns()) {

        }
        return pawns;
    }
}
