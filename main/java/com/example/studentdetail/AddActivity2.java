package com.example.studentdetail;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddActivity2 extends AppCompatActivity {

    EditText name,course,email,turl;
    Button btnAdd,btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add2);

        name = (EditText) findViewById(R.id.txtName);
        course = (EditText) findViewById(R.id.txtCourse);
        email = (EditText) findViewById(R.id.txtEmail);
        turl = (EditText) findViewById(R.id.txtImageUrl);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnBack = (Button) findViewById(R.id.btnBack);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
                clearAll();

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
        private void insertData()
        {
          Map<String,Object>map =new HashMap();
          map.put("name",name.getText().toString());
          map.put("course",course.getText().toString());
          map.put("email",email.getText().toString());
          map.put("turl",turl.getText().toString());
          FirebaseDatabase.getInstance().getReference().child("students").push()
                  .setValue(map)
                  .addOnSuccessListener(new OnSuccessListener<Void>() {
                      @Override
                      public void onSuccess(Void unused) {
                          Toast.makeText(AddActivity2.this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();

                      }
                  })
                  .addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                          Toast.makeText(AddActivity2.this, "Error while inserting the data", Toast.LENGTH_SHORT).show();
                      }
                  });
        }

        private void clearAll()
        {
          name.setText("");
          course.setText("");
          email.setText("");
          turl.setText("");
        }


    }



