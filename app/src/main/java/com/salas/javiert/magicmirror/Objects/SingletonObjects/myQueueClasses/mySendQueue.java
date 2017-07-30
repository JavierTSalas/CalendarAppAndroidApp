/*
 * Copyright (c) 2017. Javier Salas
 * Just another instance of myQueue
 * This probably isn't a good practice
 * TODO: Not do this
 */

package com.salas.javiert.magicmirror.Objects.SingletonObjects.myQueueClasses;

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
