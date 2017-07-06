package com.salas.javiert.magicmirror.Resources.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.salas.javiert.magicmirror.Objects.SingletonObjects.myConnectionSingleton.connectionSettings;
import com.salas.javiert.magicmirror.Objects.SingletonObjects.myQueueClasses.myQueueTask;
import com.salas.javiert.magicmirror.Objects.helperObjects.assignment_class;
import com.salas.javiert.magicmirror.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by javi6 on 6/2/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    // Question mark to make this a template
    List<?> data = Collections.emptyList();
    View view;
    private LayoutInflater inflater;

    public RecyclerAdapter(Context context, List<?> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    //This lets us template RecyclerAdapter so
    public typeOfObjectsList DetermineType() {
        //Since we don't know the type, we have to deduce it. This is done by comparing the 0th element to expected types
        if (!data.isEmpty()) {
            if (data.get(0) instanceof myQueueTask)
                return typeOfObjectsList.QUEUETASK;
            if (data.get(0) instanceof assignment_class)
                return typeOfObjectsList.ASSIGN_CLASS;
            if (data.get(0) instanceof connectionSettings)
                return typeOfObjectsList.SETTINGS;
        }

        Log.d("RecyclerAdapter", "Encountered a unknown class type. You're probably not seeing anything so go to RecyclerAdapter.java to define what you want to view ");
        return typeOfObjectsList.NAN;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (DetermineType()) {
            case ASSIGN_CLASS:
                view = inflater.inflate(R.layout.recyclerview_row_assignclass, parent, false);
                Log.d("RecyclerAdapter", "Determined that the type of the list was assignment_class");
                break;
            case QUEUETASK:
                view = inflater.inflate(R.layout.recyclerview_row_assignclass, parent, false);
                Log.d("RecyclerAdapter", "Determined that the type of the list was assignment_class");
                break;
            case SETTINGS:
                view = inflater.inflate(R.layout.recyclerview_row_settings_class, parent, false);
                Log.d("RecyclerAdapter", "Determined that the type of the list was settings object");
                break;
            default:

        }
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Object object_from_list = data.get(position);
        if (!data.isEmpty())
            switch (DetermineType()) {
                case ASSIGN_CLASS:
                    assignment_class assignment_class = (assignment_class) object_from_list;
                    Log.d("OnBind", assignment_class.ass_name + " " + assignment_class.class_id);
                    holder.assignment_class_title.setText(assignment_class.ass_name);
                    holder.assignment_class_id.setText(assignment_class.class_id.toString());

                    break;
                case SETTINGS:
                    final connectionSettings connectionSettings = (connectionSettings) object_from_list;
                    Log.d("OnBind", connectionSettings.getTitle() + " " + connectionSettings.getSubtext());
                    holder.settings_object_title.setText(connectionSettings.getTitle());
                    holder.settings_object_subtitle.setText(connectionSettings.getSubtext());

                    // Give our connectionSettings the views it needs
                    connectionSettings.setTitleTextView(holder.settings_object_title);
                    connectionSettings.setIvConnectionStatus(holder.settings_connection_status);
                    connectionSettings.setIvLock(holder.settings_lock);
                    connectionSettings.setSubTextView(holder.settings_object_subtitle);
                    connectionSettings.setProgessBarView(holder.settings_loading);


                    // When the user longClicks the textview unlock the edittext
                    holder.settings_object_title.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            connectionSettings.toggleLockEditText();
                            return false;
                        }
                    });

                    TextWatcher myWatcher = new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            // Set the subtext
                            connectionSettings.setSubtext(holder.settings_object_subtitle.getText().toString());
                            connectionSettings.setConnectionSuccessful(false);

                            // Test our connection with the new text
                            connectionSettings.tryConnection(holder.settings_loading.getContext());
                        }
                    };

                    holder.settings_object_subtitle.addTextChangedListener(myWatcher);
                    holder.settings_object_subtitle.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

                    // Hide to use later
                    holder.settings_loading.setVisibility(View.GONE);
                    holder.settings_connection_status.setVisibility(View.GONE);

                    break;
                default:
                    Log.d("RecyclerAdapter", "Encountered a unknown class type. You're probably not seeing anything so go to RecyclerAdapter.java to define what you want to view ");
            }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    // Possible classes that will be used with this RecyclerView
    public enum typeOfObjectsList {
        ASSIGN_CLASS, QUEUETASK, SETTINGS, NAN
    }

    // Define our custom ViewHolder for the recycler view
    protected class MyViewHolder extends RecyclerView.ViewHolder {
        // For assignment_class
        TextView assignment_class_title;
        TextView assignment_class_id;

        // For connectionSettings
        TextView settings_object_title;
        ProgressBar settings_loading;
        EditText settings_object_subtitle;
        ImageView settings_connection_status, settings_lock;

        public MyViewHolder(View itemView) {
            super(itemView);
            //Check if the list isn't empty and that
            if (!data.isEmpty())
                switch (DetermineType()) {
                    case ASSIGN_CLASS:
                        assignment_class_title = (TextView) itemView.findViewById(R.id.tvListMessage);
                        assignment_class_id = (TextView) itemView.findViewById(R.id.tvListId);
                        break;
                    case SETTINGS:
                        settings_object_title = (TextView) itemView.findViewById(R.id.tvSettingsTitle);
                        settings_object_subtitle = (EditText) itemView.findViewById(R.id.etSettingsSubtext);
                        settings_loading = (ProgressBar) itemView.findViewById(R.id.tvprogressbar);
                        settings_connection_status = (ImageView) itemView.findViewById(R.id.ivconnectionstatus);
                        settings_lock = (ImageView) itemView.findViewById(R.id.ivlock);
                        break;


                }
        }
    }
}
