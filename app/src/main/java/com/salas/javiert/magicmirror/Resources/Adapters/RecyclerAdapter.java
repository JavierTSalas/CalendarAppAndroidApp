package com.salas.javiert.magicmirror.Resources.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.salas.javiert.magicmirror.Objects.SingletonObjects.myConnectionSingleton.connectionSettings;
import com.salas.javiert.magicmirror.Objects.SingletonObjects.myConnectionSingleton.myConnectionSingleton;
import com.salas.javiert.magicmirror.Objects.SingletonObjects.myQueueClasses.myQueueTask;
import com.salas.javiert.magicmirror.Objects.helperObjects.assignment_class;
import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Resources.SwipeHelper.helper.ItemTouchHelperAdapter;
import com.salas.javiert.magicmirror.Resources.SwipeHelper.helper.OnStartDragListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by javi6 on 6/2/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> implements ItemTouchHelperAdapter {

    private final OnStartDragListener mDragStartListener;

    // Question mark to make this a template
    List<?> data = Collections.emptyList();
    List<connectionSettings> updatedFeilds = new ArrayList<>();
    View view;
    private LayoutInflater inflater;

    public RecyclerAdapter(Context context, List<?> data, OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;
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
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
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
                    holder.settings_object_title.setText(connectionSettings.getTitle());
                    holder.settings_object_subtitle.setText(connectionSettings.getSubtext());

                    Log.d("OnBind", connectionSettings.getTitle() + " " + connectionSettings.getSubtext());

                    // Give our connectionSettings the views it needs
                    connectionSettings.loadFromHolder(holder);

                    // When the user longClicks the textview toggle the lock on the edittext
                    holder.settings_object_title.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            connectionSettings.toggleLockEditText();
                            modifiedConnection(connectionSettings);
                            return false;
                        }
                    });


                    TextWatcher listForChanges = new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            // Save if we need to
                            if (connectionSettings.getTitle().equals("Host:Port"))
                                myConnectionSingleton.getInstance().setHostPort(holder.settings_object_subtitle.getText().toString(), holder.settings_object_subtitle.getContext());
                            if (connectionSettings.getTitle().equals("Directory"))
                                myConnectionSingleton.getInstance().setDirectory(holder.settings_object_subtitle.getText().toString(), holder.settings_object_subtitle.getContext());

                            connectionSettings.setSubtext(holder.settings_object_subtitle.getText().toString());
                            //modifiedConnection(connectionSettings);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    };

                    EditText.OnEditorActionListener myActionListener = new EditText.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            // When the done button is pressed
                            if (actionId == EditorInfo.IME_ACTION_DONE) {

                                // Save if we need to
                                if (connectionSettings.getTitle().equals("Host:Port"))
                                    myConnectionSingleton.getInstance().setHostPort(holder.settings_object_subtitle.getText().toString(), holder.settings_object_subtitle.getContext());
                                if (connectionSettings.getTitle().equals("Directory"))
                                    myConnectionSingleton.getInstance().setDirectory(holder.settings_object_subtitle.getText().toString(), holder.settings_object_subtitle.getContext());

                                connectionSettings.setSubtext(holder.settings_object_subtitle.getText().toString());

                                myConnectionSingleton.getInstance().saveURLToPreference(holder.settings_object_subtitle.getContext());

                                // Tey the connection
                                connectionSettings.tryConnection(holder.settings_connection_status.getContext());

                                Log.d("actionID", (actionId == EditorInfo.IME_ACTION_DONE) ? "TRUE" : "FALSE");


                                modifiedConnection(connectionSettings);


                                return true;
                            }
                            return false;

                        }

                    };

                    holder.settings_object_subtitle.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                    holder.settings_object_subtitle.addTextChangedListener(listForChanges);
                    holder.settings_object_subtitle.setOnEditorActionListener(myActionListener);

                    // Hide to use later
                    holder.settings_loading.setVisibility(View.GONE);
                    holder.settings_connection_status.setVisibility(View.GONE);

                    // Restore any views states that we loaded form our SharedPreferences
                    connectionSettings.restoreState();


                    break;
                default:
                    Log.d("RecyclerAdapter", "Encountered a unknown class type. You're probably not seeing anything so go to RecyclerAdapter.java to define what you want to view ");
            }
    }

    private void modifiedConnection(connectionSettings connectionSettings) {
        // Save our changed fields
        if (!isNotInUpdatedFields(connectionSettings)) {
            updatedFeilds.add(connectionSettings);
            Log.d("updatedFeilds", "adding");
        } else {
            updateUdpatedFields(connectionSettings);
            Log.d("updatedFeilds", "updating");
        }
    }

    private void updateUdpatedFields(connectionSettings connectionSettings) {
        for (int i = 0; i < updatedFeilds.size(); i++)
            if (updatedFeilds.get(i).getTitle() == connectionSettings.getTitle()) {
                updatedFeilds.set(i, connectionSettings);
                return;
            }
    }

    private boolean isNotInUpdatedFields(connectionSettings connectionSettings) {
        for (connectionSettings conn : updatedFeilds)
            if (conn.getTitle() == connectionSettings.getTitle())
                return true;
        return false;
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public boolean haveChangesBeenMade() {
        return (updatedFeilds.size() > 0);
    }

    public List<connectionSettings> getConnectionSettings() {
        List<connectionSettings> myConnectionSettingsList = updatedFeilds;
        Log.d("FETCH_FROM_ADAPTER", String.valueOf(myConnectionSettingsList.size()));
        updatedFeilds = new ArrayList<>(); // Reset the list
        return myConnectionSettingsList;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    // Possible classes that will be used with this RecyclerView
    public enum typeOfObjectsList {
        ASSIGN_CLASS, QUEUETASK, SETTINGS, NAN
    }

    // Define our custom ViewHolder for the recycler view
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // For assignment_class
        private TextView assignment_class_title;
        private TextView assignment_class_id;
        // For connectionSettings
        private TextView settings_object_title;
        private ProgressBar settings_loading;
        private EditText settings_object_subtitle;
        private ImageView settings_connection_status, settings_lock;

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

        public TextView getAssignment_class_title() {
            return assignment_class_title;
        }

        public TextView getAssignment_class_id() {
            return assignment_class_id;
        }

        public TextView getSettings_object_title() {
            return settings_object_title;
        }

        public ProgressBar getSettings_loading() {
            return settings_loading;
        }

        public EditText getSettings_object_subtitle() {
            return settings_object_subtitle;
        }

        public ImageView getSettings_connection_status() {
            return settings_connection_status;
        }

        public ImageView getSettings_lock() {
            return settings_lock;
        }
    }
}
