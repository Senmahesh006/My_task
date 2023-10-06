package com.example.mytask.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytask.AddNewTask;
import com.example.mytask.MainActivity;
import com.example.mytask.Model.UserModel;
import com.example.mytask.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<UserModel> userModelList;
    private MainActivity activity;
    private FirebaseFirestore firestore;

    public UserAdapter(MainActivity mainActivity, List<UserModel> userModelList) {
        this.userModelList = userModelList;
        activity = mainActivity;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.eachtask, parent, false);
        firestore = FirebaseFirestore.getInstance();

        return new ViewHolder(view);
    }

    public void deleteTask(int position) {
        UserModel userModel = userModelList.get(position);
        firestore.collection("task").document(userModel.Userid).delete();
        userModelList.remove(position);
        notifyItemRemoved(position);
    }

    public Context getContext() {
        return activity;
    }

    public void editTask(int position) {
        UserModel userModel = userModelList.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("task", userModel.getTask());
        bundle.putString("id", userModel.Userid);

        AddNewTask addNewTask = new AddNewTask();
        addNewTask.setArguments(bundle);
        addNewTask.show(activity.getSupportFragmentManager(), addNewTask.getTag());
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        UserModel userModel = userModelList.get(position);
        holder.mcheck.setText(userModel.getTask());

        holder.mcheck.setChecked(toBoolean(userModel.getStatus()));

        holder.mcheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    firestore.collection("task").document(userModel.Userid).update("status", 1);
                } else {
                    firestore.collection("task").document(userModel.Userid).update("status", 0);
                }
            }
        });

        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTask(position);
            }
        });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are You Sure?")
                        .setTitle("Delete Task")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteTask(position);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notifyItemChanged(position);
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private boolean toBoolean(int status) {
        return status != 0;
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button btn_edit, btn_delete;
        CheckBox mcheck;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mcheck = itemView.findViewById(R.id.todoCheckBox);
            btn_edit = itemView.findViewById(R.id.btn_edit);
            btn_delete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
