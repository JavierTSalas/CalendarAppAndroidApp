package com.salas.javiert.magicmirror.Resources.ExpandableChild.ViewHolder;

import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup;

import java.util.List;

/**
 * Created by javi6 on 6/26/2017.
 */

public class myCheckedExpandableGroup extends CheckedExpandableGroup {
    public myCheckedExpandableGroup(String title, List items) {
        super(title, items);
    }

    @Override
    public void onChildClicked(int childIndex, boolean checked) {

    }
}
