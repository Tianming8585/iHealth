package edu.nkfust.tianming.ihealth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;

public class RecordActivity extends AppCompatActivity {
    private ListView recordLv;
    private ArrayList<String> recordAl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        recordLv=(ListView)findViewById(R.id.recordLv);
        recordAl=new ArrayList<String>();
        String username= getIntent().getStringExtra("username");
        Log.d("getinent","zz"+username);
        query(username);
    }
    public void query(final String username){
        Firebase.setAndroidContext(this);
        Firebase firebaseRef = new Firebase( "https://ihealth-9730b.firebaseio.com/user/"+username+"/record/" );

// Queries can only order by one key at a time
//        Query queryRef = firebaseRef.orderByChild("password");
//        queryRef.addChildEventListener(new ChildEventListener() {

        firebaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                Record record = snapshot.getValue(Record.class);
                Log.d("show code", "codeName = " + record.getCodeName() + " , CodeContent = " + record.getCodeContent());
                recordAl.add("CodeName : "+record.getCodeName()+"\nCodeContent : "+record.getCodeContent());
                refreshRecord();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });

    }
    public void refreshRecord(){
        ArrayAdapter<String> listAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,recordAl);
        recordLv.setAdapter(listAdapter);
//        recordLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getApplicationContext(), "你選擇的是" + recordAl.get(i), Toast.LENGTH_SHORT).show();
//
//            }
//        });
    }
}
