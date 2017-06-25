package com.salas.javiert.magicmirror.Resources.ExpandableChild;

import android.content.Context;
import android.util.Log;

import com.salas.javiert.magicmirror.Objects.assignment_class;
import com.salas.javiert.magicmirror.Objects.myQueueClasses.myQueueTask;
import com.salas.javiert.magicmirror.Resources.Adapters.RecylerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by javi6 on 6/4/2017.
 */

public class TitleCreator {
    static TitleCreator _titleCreator;
    private static List<?> ListOfObjects, OldList;
    List<ParentViewClass> ClassNameArrayList;

    public TitleCreator(Context context) {
        ClassNameArrayList = new ArrayList<>();
        OldList = ListOfObjects;
        if (DetermineType() == RecylerAdapter.typeOfObjectsList.ASSIGN_CLASS) {
            for (int i = 0; i < ListOfObjects.size(); i++) {
                ParentViewClass ParentView = new ParentViewClass(((assignment_class) ListOfObjects.get(i)).ass_name.toString());
                ClassNameArrayList.add(ParentView);
            }
        }
        if (DetermineType() == RecylerAdapter.typeOfObjectsList.QUEUETASK) {
            for (int i = 0; i < ListOfObjects.size(); i++) {
                ParentViewClass ParentView = new ParentViewClass((ListOfObjects.get(i)).toString());
                ClassNameArrayList.add(ParentView);
            }
        }
    }

    public static TitleCreator get(Context context, List<?> classList) {
        ListOfObjects = classList;
        if (_titleCreator == null || OldList != classList)
            _titleCreator = new TitleCreator(context);

        return _titleCreator;
    }

    //This lets us template RecyclerAdapter so
    public RecylerAdapter.typeOfObjectsList DetermineType() {
        //Since we don't know the type, we have to deduce it. This is done by comparing the 0th element to expected types
        if (!ListOfObjects.isEmpty()) {
            if (ListOfObjects.get(0) instanceof myQueueTask)
                return RecylerAdapter.typeOfObjectsList.QUEUETASK;
            if (ListOfObjects.get(0) instanceof assignment_class)
                return RecylerAdapter.typeOfObjectsList.ASSIGN_CLASS;
        }
        if (!ListOfObjects.isEmpty()) {
            Log.d("ClassType", ListOfObjects.get(0).getClass().toString() + " has not been defined in TitleCreator.java");
        } else {
            Log.d("TitleCreator", "Encountered a unknown class type. You're probably not seeing anything so go to TitleCreator.java to define what you want to view ");
        }
        return RecylerAdapter.typeOfObjectsList.NAN;
    }

    public List<ParentViewClass> getAll() {
        return ClassNameArrayList;
    }


    public enum typeOfObjectsList {
        ASSIGN_CLASS, QUEUETASK, NAN
    }

}
