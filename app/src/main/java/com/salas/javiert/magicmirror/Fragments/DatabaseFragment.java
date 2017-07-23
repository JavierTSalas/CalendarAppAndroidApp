/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Fragments;

import android.arch.lifecycle.LifecycleFragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.salas.javiert.magicmirror.R;
import com.salas.javiert.magicmirror.Resources.Room.serverAddress.Entities.serverAddressItem;
import com.salas.javiert.magicmirror.Resources.Room.serverAddress.serverAddressDatabase;
import com.salas.javiert.magicmirror.Resources.Room.serverAddress.serverAddressDatabaseCreator;
import com.salas.javiert.magicmirror.Views.DataBindingTest.DatabaseFragment.Fragment.ConnectionListViewModel;
import com.salas.javiert.magicmirror.Views.DataBindingTest.DatabaseFragment.Fragment.RecyclerViewClasses.dataBindingAdapter;
import com.salas.javiert.magicmirror.databinding.LayoutFragmentDatabaseBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by javi6 on 7/16/2017.
 */

public class DatabaseFragment extends LifecycleFragment {

    private LayoutFragmentDatabaseBinding mBinding;

    private ConnectionListViewModel model;
    private dataBindingAdapter mAdapter;
    private RecyclerView mRecycler;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Find the databinding elements and inflate the layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.layout_fragment_database, container, false);
        // Get the view so we can still use findViewById
        View view = mBinding.getRoot();

        configDataBind(mBinding);

        mBinding.bAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearList();
            }


        });
        mBinding.bQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryAction();
            }
        });

        mRecycler = mBinding.Recycler;
        mAdapter = new dataBindingAdapter(new ArrayList<serverAddressItem>());
        mRecycler.setLayoutManager(new LinearLayoutManager(this.getContext()));

        mRecycler.setAdapter(mAdapter);


        return view;
    }

    private void clearList() {
        List<serverAddressItem> myList = new ArrayList<>();
        mAdapter.setItems(myList);
    }

    private void configDataBind(LayoutFragmentDatabaseBinding mBinding) {
        int ColumdID = 1;
        String Tv1 = "Please enter a column number";
        String Tv2 = "Please enter a action number";
        String b1 = "SAVE";
        String b2 = "QUERY";


        mBinding.setColumnID(String.valueOf(ColumdID));
        mBinding.setTextView1Name(Tv1);
        mBinding.setTextView2Name(Tv2);
        mBinding.setIsLoading(true);
        mBinding.bAction.setText(b1);
        mBinding.bQuery.setText(b2);
    }

    private void createDb() {
        final serverAddressDatabaseCreator serverAddressDatabaseCreator = com.salas.javiert.magicmirror.Resources.Room.serverAddress.serverAddressDatabaseCreator.getInstance(getContext());
        if (serverAddressDatabaseCreator.isDatabaseCreated().getValue() == false)
            serverAddressDatabaseCreator.createDb(getContext());
        serverAddressDatabaseCreator.createDb(getContext());
    }

    private void queryAction() {
        final serverAddressDatabaseCreator serverAddressDatabaseCreator = com.salas.javiert.magicmirror.Resources.Room.serverAddress.serverAddressDatabaseCreator.getInstance(this.getContext());
        serverAddressDatabase db = serverAddressDatabaseCreator.getDatabase();
        mAdapter.setItems(db.serverAddressDao().getConnections(0, Integer.valueOf(mBinding.getColumnID())));
    }
    //Fill the string arrays from the resources, we need context to use getResources()

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

/*

        // Create our factory
        ConnectionListViewModel.Factory factory = new ConnectionListViewModel.Factory(getActivity().getApplication());

        // Make our viewModel
        model = ViewModelProviders.of(this, factory).get(ConnectionListViewModel.class);

        // Observe it
        model.getLiveData().observe(DatabaseFragment.this, new Observer<List<serverAddressItem>>() {
            @Override
            public void onChanged(@Nullable List<serverAddressItem> serverAddressItems) {
                //
                mBinding.setIsLoading(true);
                try {
                    if (serverAddressItems != null)
                        mAdapter.setItems(serverAddressItems);
                } finally {
                    mBinding.setIsLoading(false);
                }
            }
        });

*/

    }


}
