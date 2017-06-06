package com.salas.javiert.magicmirror.Resources.ExpandableChild;

import android.content.Context;

import com.salas.javiert.magicmirror.Objects.assignment_class;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by javi6 on 6/4/2017.
 */

public class TitleCreator {
    static TitleCreator _titleCreator;
    private static List<assignment_class> myAssignmentList;
    List<ParentViewClass> ClassNameArrayList;

    public TitleCreator(Context context) {
        ClassNameArrayList = new ArrayList<>();
        for (int i = 0; i < myAssignmentList.size(); i++) {
            ParentViewClass ParentView = new ParentViewClass(myAssignmentList.get(i).ass_name.toString());
            ClassNameArrayList.add(ParentView);
        }
    }

    public static TitleCreator get(Context context, List<assignment_class> classList) {
        myAssignmentList = classList;
        if (_titleCreator == null)
            _titleCreator = new TitleCreator(context);


        return _titleCreator;
    }

    public List<ParentViewClass> getAll() {
        return ClassNameArrayList;
    }

}
