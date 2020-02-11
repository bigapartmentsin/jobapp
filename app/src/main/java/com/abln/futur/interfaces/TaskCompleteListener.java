package com.abln.futur.interfaces;

import android.content.Context;
import android.content.Intent;

import java.io.Serializable;

public interface TaskCompleteListener extends Serializable {
    void onTaskCompleted(Context context, Intent intent);
}
