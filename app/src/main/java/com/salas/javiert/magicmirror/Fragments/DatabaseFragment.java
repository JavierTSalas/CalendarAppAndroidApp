/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Fragments;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Resources.Room.DatabaseCreator;
import com.salas.javiert.magicmirror.Resources.Room.connection.Entities.connectionDataBaseItem;
import com.salas.javiert.magicmirror.Views.LiveDataTest.ConnectionListViewModel;
import com.salas.javiert.magicmirror.Views.LiveDataTest.LiveDataAdapter;
import com.salas.javiert.magicmirror.databinding.LayoutFragmentDatabaseBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by javi6 on 7/16/2017.
 */

public class DatabaseFragment extends LifecycleFragment {
    public static int COLUMN;
    EditText etAction, etColumn;
    Button bAction, bQuery;


    private LayoutFragmentDatabaseBinding mBinding;

    private ConnectionListViewModel model;
    private LiveDataAdapter mAdapter;
    private RecyclerView mRecycler;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Find the databinding elements and inflate the layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.layout_fragment_database, container, false);
        // Get the view so we can still use findViewById
        View view = mBinding.getRoot();

        configDataBind(mBinding);

        etAction = (EditText) view.findViewById(R.id.etAction);
        etColumn = (EditText) view.findViewById(R.id.etColumn);
        bAction = (Button) view.findViewById(R.id.bAction);
        bAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }


        });
        bQuery = (Button) view.findViewById(R.id.bQuery);
        bQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryAction();
            }
        });

        mRecycler = (RecyclerView) view.findViewById(R.id.Recycler);
        mAdapter = new LiveDataAdapter(new ArrayList<connectionDataBaseItem>());
        mRecycler.setLayoutManager(new LinearLayoutManager(this.getContext()));

        mRecycler.setAdapter(mAdapter);


        return view;
    }

    private void configDataBind(LayoutFragmentDatabaseBinding mBinding) {
        int ColumdID = 1;
        String Tv1 = "Please enter a column number";
        String Tv2 = "Please enter a action number";


        mBinding.setColumnID(String.valueOf(ColumdID));
        mBinding.setTextView1Name(Tv1);
        mBinding.setTextView2Name(Tv2);
    }

    private void createDb() {
        DatabaseCreator.getInstance(getContext()).createDb(getContext());
    }

    private void queryAction() {
        final DatabaseCreator databaseCreator = DatabaseCreator.getInstance(this.getContext());
        int ColumnNumber = Integer.valueOf(mBinding.getColumnID());
        if (databaseCreator.isDatabaseCreated().getValue() == false)
            databaseCreator.createDb(getContext());
        model.setMyLiveList(databaseCreator.getDatabase().connectionDao().getIndex(ColumnNumber));
    }
    //Fill the string arrays from the resources, we need context to use getResources()

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        // I don't know what this does
        // TODO:Understand this
        ConnectionListViewModel.Factory factory = new ConnectionListViewModel.Factory(getActivity().getApplication());
        model = ViewModelProviders.of(this, factory).get(ConnectionListViewModel.class);

        model.getLiveData().observe(DatabaseFragment.this, new Observer<List<connectionDataBaseItem>>() {
            @Override
            public void onChanged(@Nullable List<connectionDataBaseItem> connectionDataBaseItems) {
                if (connectionDataBaseItems != null)
                    mAdapter.setItems(connectionDataBaseItems);
            }
        });

        createDb();

    }


}
