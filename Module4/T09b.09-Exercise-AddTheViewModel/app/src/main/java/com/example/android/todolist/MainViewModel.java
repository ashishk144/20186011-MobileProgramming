package com.example.android.todolist;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.todolist.database.AppDatabase;
import com.example.android.todolist.database.TaskEntry;

import java.util.List;

// TODO (1) make this class extend AndroidViewModel and implement its default constructor
public class MainViewModel extends AndroidViewModel {
    private LiveData<List<TaskEntry>> tasks;
    private static final String TAG = MainViewModel.class.getSimpleName();
    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Active retrieval");
        tasks = db.taskDao().loadAllTasks();
    }

    public LiveData<List<TaskEntry>> getTasks() {
        return tasks;
    }
}
