package com.salas.javiert.magicmirror.Objects.SingletonObjects.myQueueClasses;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by javi6 on 6/22/2017.
 */

public class myQueueSerializer implements JsonSerializer<myQueue> {
    private ArrayList<myQueueTask> QueueTaskListToBeSerialized = new ArrayList();

    @Override
    public JsonElement serialize(myQueue myQueue, Type type, JsonSerializationContext jsonSerializationContext) {
        return null;
    }
}
