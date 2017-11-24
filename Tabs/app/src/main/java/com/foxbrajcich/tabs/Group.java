package com.foxbrajcich.tabs;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by erikfox on 11/15/17.
 */

public class Group implements Serializable{
    private String groupTitle;
    private int groupId;
    private List<User> members;
    private Hashtable<Integer, List<Expense>> transactionsTable;
    private boolean isOnline;

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public String getGroupTitle() {
        return groupTitle;
    }
}
