/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Objects.SingletonObjects_REMOVE_ME.myQueueClasses;

/**
 * Created by javi6 on 6/28/2017.
 */

public class mySendQueue extends myQueue {
    private static mySendQueue instance = null;

    public mySendQueue() {
        super();
    }

    //Everytime you need an instance, call this
    public static mySendQueue getInstance() {
        if (instance == null)
            instance = new mySendQueue();

        return instance;
    }

}
