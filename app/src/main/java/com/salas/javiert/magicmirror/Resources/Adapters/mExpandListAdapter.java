package com.salas.javiert.magicmirror.Resources.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.salas.javiert.magicmirror.Objects.helperObjects.assignment_class;
import com.salas.javiert.magicmirror.R;

import java.util.List;
import java.util.Map;

/**
 * Created by javi6 on 5/28/2017.
 */

public class mExpandListAdapter extends BaseExpandableListAdapter {
    Context context;
    List<String> lString;
    Map<String, List<assignment_class>> myMap;


    public mExpandListAdapter(Context context, List<String> lString, Map<String, List<assignment_class>> myMap) {
        this.context = context;
        this.lString = lString;
        this.myMap = myMap;
    }

    @Override
    public int getGroupCount() {
        return lString.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return myMap.get(lString.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return lString.get(groupPosition);
    }

    @Override
    public assignment_class getChild(int groupPosition, int childPosition) {
        return myMap.get(lString.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String parentText = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.recyclerview_list_parent, null);
        }

        TextView tvParent = (TextView) convertView.findViewById(R.id.tvParent);
        tvParent.setText(parentText);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        assignment_class ChildAssignment = getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.recyclerview_list_child, null);
        }

        final TextView tvChild = (TextView) convertView.findViewById(R.id.tvChild);
        final TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        tvChild.setText(ChildAssignment.ass_name);
        tvTime.setText("TBD");

/*

        if (!(tvTime.getText().length() > 0)) {
            Random r = new Random();
            int i1 = r.nextInt(30 - 10);
            new CountDownTimer(i1 * 10000, 1000) {

                public void onTick(long millisUntilFinished) {
                    tvTime.setText("seconds remaining: " + millisUntilFinished / 1000);
                }

                public void onFinish() {
                    tvTime.setText("done!");
                }
            }.start();
        }


*/

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return true;
    }
}
