package com.salas.javiert.magicmirror.Resources.Adapters;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.salas.javiert.magicmirror.Objects.assignment_class;
import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Resources.ExpandableChild.ParentViewClass;
import com.salas.javiert.magicmirror.Resources.ExpandableChild.ViewHolder.TitleChildViewHolder;
import com.salas.javiert.magicmirror.Resources.ExpandableChild.ViewHolder.TitleParentViewHolder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.widget.AdapterView.OnItemSelectedListener;

/**
 * Created by javi6 on 6/4/2017.
 */

public class myExpandRecylerAdapter extends ExpandableRecyclerAdapter<TitleParentViewHolder, TitleChildViewHolder> {

    LayoutInflater mInflater;
    assignment_class myAssignment;
    Dialog dialog;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;


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
                final assignment_class BackUpAsssign = myAssignment;


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
                        myAssignment.class_id = position;
                        Log.d("Set", myAssignment.class_id.toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                // Initialize for DatePicker
                myCalendar = Calendar.getInstance();

                // Adapter
                date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        //Set textview
                        String myFormat = "MM/dd/yyyy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        ((TextView) dialog.findViewById(R.id.tvDate)).setText(sdf.format(myCalendar.getTime()));
                    }

                };


                final TextView etDate = (TextView) dialog.findViewById(R.id.tvDate);


                // Default to today
                String myFormat = "MM/dd/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                ((TextView) dialog.findViewById(R.id.tvDate)).setText(sdf.format(myCalendar.getTime()));


                // OnClick will open the dialogue for DatePicker
                etDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DatePickerDialog(dialog.getContext(), date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });


                final TextView etTime = (TextView) dialog.findViewById(R.id.etTime);
                //Default time
                etTime.setText("00:00");


                // OnClick will open the dialogue for TimePicker
                etTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog mTimePicker;

                        mTimePicker = new TimePickerDialog(dialog.getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                etTime.setText(selectedHour + ":" + selectedMinute);
                            }
                        }, Calendar.HOUR_OF_DAY, Calendar.MINUTE, false);
                        mTimePicker.setTitle("Select Time");
                        mTimePicker.show();
                    }
                });


                Button Done = (Button) dialog.findViewById(R.id.bDone);
                Button Cancel = (Button) dialog.findViewById(R.id.bCancel);
                Button Confirm = (Button) dialog.findViewById(R.id.bConfirm);


                Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myAssignment = BackUpAsssign;
                        dialog.cancel();
                    }
                });
            }
        });

    }


}

