package com.foxbrajcich.tabs;

import java.io.Serializable;
import java.util.List;

/**
 * Created by erikfox on 11/15/17.
 */

public class Group implements Serializable{
    private Tab currentTab;
    private String groupTitle;
    private List<User> members;
    private boolean isActive;

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public String getGroupTitle() {
        return groupTitle;
    }
}
