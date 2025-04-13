package org.leralix.exotictrades.market;

import java.util.LinkedList;

public class SellHistory {

    private static class Sales {
        int number;

        Sales() {
            this.number = 0;
        }
    }

    private LinkedList<Sales> salesHistory;

    public SellHistory(int timeLength) {
        this.salesHistory = new LinkedList<>();

        for(int i = 0; i < timeLength; i++){
            salesHistory.add(new Sales());
        }

    }

    public void recordSale(int nbSold) {
        salesHistory.getLast().number += nbSold;
    }


    public void nextRecord() {
        salesHistory.addLast(new Sales());
        salesHistory.removeFirst();

    }

    public void updateTimeLength(int timeLength) {

        if(salesHistory == null){
            salesHistory = new LinkedList<>();
        }

        while (salesHistory.size() > timeLength){
            salesHistory.removeFirst();
        }
    }

    public int getTotalSales() {
        return salesHistory.stream().mapToInt(h -> h.number).sum();
    }

}
