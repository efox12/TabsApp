package com.foxbrajcich.tabs;

/**
 * Created by Robert on 11/28/2017.
 */

public class Debt {

    private Double amount;
    private User debtor;

    public Debt(){
        amount = 0D;
        debtor = null;
    }

    public Debt(User debtor){
        this.debtor = debtor;
        amount = 0D;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public User getDebtor() {
        return debtor;
    }

    public void setDebtor(User debtor) {
        this.debtor = debtor;
    }

    @Override
    public String toString() {
        return amount + " owed to " + debtor.getName() + ".";
    }
}
