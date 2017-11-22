package com.foxbrajcich.tabs;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by erikfox on 11/15/17.
 */

public class Tab {
    private double amount;
    private boolean completed;
    private List<User> members;
    private Hashtable<Integer, List<Expense>> transactionsTable;

    public Tab(){

    }

    public List<Double> getAmountsOwed(){
        List<Double> amounts = new ArrayList<>();
        return amounts;
    }

    public double getWeightedAmountOwed(){
        double amountOwed = 0.00;
        return amountOwed;
    }

    public double getAmount(){
        return amount;
    }

    public double setAmount(){
        return amount;
    }
}
