package com.salas.javiert.magicmirror.Resources.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.salas.javiert.magicmirror.Objects.assignment_class;
import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Resources.ExpandableChild.ParentViewClass;
import com.salas.javiert.magicmirror.Resources.ExpandableChild.ViewHolder.TitleChildViewHolder;
import com.salas.javiert.magicmirror.Resources.ExpandableChild.ViewHolder.TitleParentViewHolder;

import java.util.List;

import static android.widget.AdapterView.OnItemSelectedListener;

/**
 * Created by javi6 on 6/4/2017.
 */

public class myExpandRecylerAdapter extends ExpandableRecyclerAdapter<TitleParentViewHolder, TitleChildViewHolder> {

    LayoutInflater mInflater;
    assignment_class myAssignment;
    Dialog dialog;

    public myExpandRecylerAdapter(Context context, List<ParentObject> parentItemList) {
        super(context, parentItemList);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public TitleParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.list_parent, viewGroup, false);
        return new TitleParentViewHolder(view);
    }

    @Override
    public TitleChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.list_child, viewGroup, false);
        return new TitleChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(TitleParentViewHolder titleParentViewHolder, int i, Object o) {
        ParentViewClass title = (ParentViewClass) o;
        titleParentViewHolder.tvParent.setText(title.getTitle());

    }

    @Override
    public void onBindChildViewHolder(final TitleChildViewHolder titleChildViewHolder, int i, Object o) {

        myAssignment = (assignment_class) o;
        titleChildViewHolder.tvTitle.setText(myAssignment.ass_name);
        titleChildViewHolder.tvTime.setText(myAssignment.TimeLeft());

        titleChildViewHolder.itemView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                dialog = new Dialog(titleChildViewHolder.itemView.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.assignment_dialog);
                dialog.show();
                Log.d("onClick", "Child: " + myAssignment.toString());


                // Set the textView at the top to the ass_name to indicate which assignment is being modified
                TextView Name = (TextView) dialog.findViewById(R.id.tvName);
                Name.setText(myAssignment.ass_name);


                Spinner DropDown = (Spinner) dialog.findViewById(R.id.spDropDown);

                // Create an ArrayAdapter using the string array and a default spinner
                ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(titleChildViewHolder.itemView.getContext(), R.array.class_list, android.R.layout.simple_spinner_item);
                // Specify the layout to use when the list of choices appears
                staticAdapter
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Apply the adapter to the spinner
                DropDown.setAdapter(staticAdapter);

                DropDown.setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.v("item", (String) parent.getItemAtPosition(position));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                EditText Date = (EditText) dialog.findViewById(R.id.etDate);
                EditText Time = (EditText) dialog.findViewById(R.id.etTime);
                Button Done = (Button) dialog.findViewById(R.id.bDone);
                Button Cancel = (Button) dialog.findViewById(R.id.bCancel);
                Button Confirm = (Button) dialog.findViewById(R.id.bConfirm);


                Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
            }
        });

    }
}
