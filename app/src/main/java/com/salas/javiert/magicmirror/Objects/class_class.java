package com.salas.javiert.magicmirror.Objects;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by javi6 on 5/27/2017.
 */

public class class_class extends assignment_class {

    public List<assignment_class> AssignmentList = new ArrayList<assignment_class>();

    public class_class(int i) {
        AssignmentList.add(new assignment_class("No assignments due for this class", i));
        Log.d("Classes", "class_class: built");
    }

    public class_class() {
        AssignmentList.add(new assignment_class("No assignments due for this class"));
        Log.d("Classes", "class_class: built");
    }

    public Integer getClassID() {
        return AssignmentList.get(0).class_id;
    }

    public void Append(assignment_class AppendClass) {
        if (AssignmentList.get(0).ass_name == "No assignments due for this class")
            AssignmentList.clear();
        AssignmentList.add(AppendClass);
    }


    public List<assignment_class> getList() {
        return AssignmentList;
    }
}
