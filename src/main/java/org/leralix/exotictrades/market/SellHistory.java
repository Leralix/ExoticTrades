package org.leralix.exotictrades.market;

import java.util.HashMap;

public class SellHistory {


    private int cursor;
    private final HashMap<Integer,Integer> history;
    private final int timeLength;


    public SellHistory(int timeLength) {
        this.cursor = 0;
        this.history = new HashMap<>();
        this.timeLength = timeLength;
    }


    public void addSell(int quantity){
        history.put(cursor, history.getOrDefault(cursor,0) + quantity);
    }

    public void updateToNextCursor() {
        cursor = cursor + 1 % timeLength;
        history.put(cursor, 0);
    }

    public int getAmount() {
        int total = 0;
        for(int i = 0; i < timeLength; i++){
            total += history.getOrDefault(i,0);
        }
        return total;
    }
}
