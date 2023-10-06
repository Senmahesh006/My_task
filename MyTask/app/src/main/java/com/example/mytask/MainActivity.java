package com.example.mytask;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.mytask.Adapter.UserAdapter;
import com.example.mytask.Model.UserModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements  OnDialogCloseListner{
    RecyclerView recyclerView;
    private FloatingActionButton mFab;
    private FirebaseFirestore firestore;

  private List<UserModel> mList;
    private UserAdapter userAdapter;

    private Query query;
    private ImageView p;
    private ListenerRegistration listenerRegistration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.tasksRecyclerView);
        mFab = findViewById(R.id.fab);
        firestore = FirebaseFirestore.getInstance();
        p=findViewById(R.id.profile);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));


        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,profile.class);
                startActivity(intent);
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG);
            }
        });


        mList = new ArrayList<>();
        userAdapter = new UserAdapter(MainActivity.this , mList);

    //    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new Helper(userAdapter));
    //     itemTouchHelper.attachToRecyclerView(recyclerView);
        showData();
        recyclerView.setAdapter(userAdapter);

    }

    private void showData() {

     query = firestore.collection("task").orderBy("time",Query.Direction.DESCENDING);
              listenerRegistration= query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange documentChange : value.getDocumentChanges()){
                    if (documentChange.getType() == DocumentChange.Type.ADDED){
                        String id = documentChange.getDocument().getId();
                        UserModel userModel = documentChange.getDocument().toObject(UserModel.class).withId(id);
                        mList.add(userModel);
                        userAdapter.notifyDataSetChanged();
                    }
                }
                listenerRegistration.remove();
            }
        });
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList.clear();
        showData();
        userAdapter.notifyDataSetChanged();
    }
}