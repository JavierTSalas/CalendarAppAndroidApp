/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Views.ExpandableRecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import android.widget.Toast;

import com.salas.javiert.magicmirror.Objects.SingletonObjects.myQueueClasses.myQueue;
import com.salas.javiert.magicmirror.Objects.SingletonObjects.myQueueClasses.myQueueItem;
import com.salas.javiert.magicmirror.Objects.SingletonObjects.myQueueClasses.myQueueTask;
import com.salas.javiert.magicmirror.Objects.helperObjects.assignment_class;
import com.salas.javiert.magicmirror.Objects.helperObjects.sendToServerObject;
import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Views.ExpandableRecyclerView.DependentViews.TitleChildViewHolder;
import com.salas.javiert.magicmirror.Views.ExpandableRecyclerView.DependentViews.TitleParentViewHolder;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.view.LayoutInflater.from;
import static android.widget.AdapterView.OnItemSelectedListener;

//import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
//import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

/**
 * Created by javi6 on 6/4/2017.
 */

public class myExpandRecyclerAdapter extends ExpandableRecyclerViewAdapter<TitleParentViewHolder, TitleChildViewHolder> {

    assignment_class myAssignment;
    myQueueTask myQueueTask;
    Dialog dialog;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    boolean DoneFlag = false;


    public myExpandRecyclerAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public TitleParentViewHolder onCreateGroupViewHolder(ViewGroup viewGroup, int viewType) {
        View view = from(viewGroup.getContext()).inflate(R.layout.recyclerview_list_parent, viewGroup, false);
        return new TitleParentViewHolder(view);
    }

    @Override
    public TitleChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup, int viewType) {
        View view = from(viewGroup.getContext()).inflate(R.layout.recyclerview_list_child, viewGroup, false);
        return new TitleChildViewHolder(view);
    }


    @Override
    public void onBindChildViewHolder(final TitleChildViewHolder titleChildViewHolder, int flatPosition, ExpandableGroup group, int childIndex) {
        // o instanceof class can be used to determine the class type and template this adapter
        final Object o = (group).getItems().get(childIndex);

        if (o instanceof assignment_class) {

            Toast.makeText(titleChildViewHolder.itemView.getContext(), "assignment_class selected", Toast.LENGTH_SHORT)
                    .show();

            Log.d("instanceof", "assignment_class");
            myAssignment = (assignment_class) o;
            titleChildViewHolder.tvTitle.setText(myAssignment.ass_name);
            titleChildViewHolder.tvTime.setText(myAssignment.TimeLeft());

            titleChildViewHolder.itemView.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    final assignment_class ModifiedAssignment = myAssignment;


                    //Create a new dialog
                    dialog = new Dialog(titleChildViewHolder.itemView.getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_assignment);
                    dialog.show();
                    Log.d("onClick", "Child: " + myAssignment.toString());


                    // Set the textView at the top to the ass_name to indicate which assignment is being modified
                    TextView Name = (TextView) dialog.findViewById(R.id.etName);
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


                    final Button Done = (Button) dialog.findViewById(R.id.bDone);

                    Done.setText("NOT DONE");
                    Done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!DoneFlag) {
                                Done.setText("DONE");
                                DoneFlag = true;
                            } else {
                                Done.setText("NOT DONE");
                                DoneFlag = true;
                            }
                        }
                    });

                    Button Cancel = (Button) dialog.findViewById(R.id.bCancel);
                    Button Confirm = (Button) dialog.findViewById(R.id.bConfirm);
                    Confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ModifiedAssignment.ass_id = myAssignment.ass_id;
                            ModifiedAssignment.ass_name = ((TextView) dialog.findViewById(R.id.etName)).getText();
                            ModifiedAssignment.class_id = 0; //TODO: This

                            // We have to convert etDate to Date
                            SimpleDateFormat parser_date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            try {
                                ModifiedAssignment.due = parser_date.parse(etDate.getText() + " " + etTime.getText() + ":00");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            ModifiedAssignment.date_assigned = myAssignment.date_assigned;
                            ModifiedAssignment.done = ((((Button) dialog.findViewById(R.id.bDone)).getText() == "DONE"));
                            ModifiedAssignment.weight = 99; //TODO: This aswell

                            myQueueItem item = null;
                            try {
                                item = new myQueueItem(new assignment_class(myAssignment, ModifiedAssignment));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            item.setMode("EDIT");
                            myQueue.getInstance().addQueueItem(item);
                            dialog.dismiss();

                            //Save the queue that we just created
                            myQueue.getInstance().saveMyQueue(dialog.getContext());


                        }
                    });

                    Cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                }
            });

        }


        if (o instanceof myQueueItem) {
            Toast.makeText(titleChildViewHolder.itemView.getContext(), "myQueueItem selected", Toast.LENGTH_SHORT)
                    .show();
            titleChildViewHolder.tvTitle.setText(o.toString());
            titleChildViewHolder.tvTime.setText("SEND");

            titleChildViewHolder.tvTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(titleChildViewHolder.itemView.getContext())
                            .setTitle("Title")
                            .setMessage("Do you really want to whatever?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    sendToServerObject sendToServerObject = new sendToServerObject((myQueueItem) o);
                                    sendToServerObject.send(titleChildViewHolder.itemView.getContext());
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                }
            });

        }
    }

    @Override
    public void onBindGroupViewHolder(TitleParentViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setText(group);

    }
}

