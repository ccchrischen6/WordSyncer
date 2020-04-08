package com.example.wordsyncer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference("WordSyncer");
    EditText e1;
    EditText e2;
    String key;
    String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e1 = (EditText) findViewById(R.id.Key);
        e2 = (EditText) findViewById(R.id.Value);
        Button write = (Button) findViewById(R.id.Write);
        Button read = (Button) findViewById(R.id.Read);
        Button delete = (Button) findViewById(R.id.Delete);
        write.setOnClickListener(actionListener);
        read.setOnClickListener(actionListener);
        delete.setOnClickListener(actionListener);

    }

/**
 *
 */

    /**for next and previous button*/
    private View.OnClickListener actionListener = new View.OnClickListener() {
        public void onClick(View v) {
            Button button = (Button) v;
            key = e1.getText().toString();
            value = e2.getText().toString();
            /**get button text*/
            char action = button.getText().toString().charAt(0);

            switch (action){
                case 'w':
                    if (value==null || key==null || key.length()*value.length()==0){
                        Toast.makeText(MainActivity.this, "Illegal format", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ref.child(key).setValue(value);
                    Toast.makeText(MainActivity.this, "Data has been uploaded", Toast.LENGTH_SHORT).show();
                    return;

                case 'r':
                    read();
                    return;

                case 'd':
                    ref.child(key).removeValue();
                    return;
            }




        }
    };

    private void read(){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.child(key).exists()) {
                    Toast.makeText(MainActivity.this, "Null Object Error", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    e2.setText("" + dataSnapshot.child(key).getValue());
                    Toast.makeText(MainActivity.this, "Data has been retrieved", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
