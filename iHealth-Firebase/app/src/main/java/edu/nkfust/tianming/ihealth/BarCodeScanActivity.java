package edu.nkfust.tianming.ihealth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class BarCodeScanActivity extends AppCompatActivity {


    private Button btn_scan;
    private TextView txt_url;
    private String username;
    private long count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code_scan);

        username= getIntent().getStringExtra("username");
        Log.d("username scan",username);
        this.btn_scan = (Button) findViewById(R.id.btn_scan);
        this.txt_url = (TextView) findViewById(R.id.txt_url);

        this.btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(BarCodeScanActivity.this).initiateScan();
                getDataCount();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (result != null) {
            String codeContent = result.getContents();
            String codeName = result.getFormatName();
            txt_url.setText(codeName + " \n" + codeContent);
            Toast.makeText(getApplicationContext(), codeContent, Toast.LENGTH_LONG).show();

            writeFirebase(new Record(codeName,codeContent));
        } else {
            Toast.makeText(getApplicationContext(), "nothing", Toast.LENGTH_LONG).show();
        }
    }
    public void getDataCount(){

        Firebase myFirebaseRef = new Firebase("https://ihealth-9730b.firebaseio.com/user/"+username+"/record/");
        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count =dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    public void writeFirebase(Record record){
        // Setup Firebase library
        Firebase.setAndroidContext(this);

        // Creating a Firebase database Reference
        Firebase myFirebaseRef = new Firebase("https://ihealth-9730b.firebaseio.com/user/"+username+"/record/");

        // Writing Data
        myFirebaseRef.child(String.valueOf(count)).setValue(record);
    }
}
