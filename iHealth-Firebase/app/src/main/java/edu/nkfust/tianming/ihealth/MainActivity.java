package edu.nkfust.tianming.ihealth;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class MainActivity extends AppCompatActivity {
    private String username;
    private Context context;
    private String firebaseURL="https://ihealth-9730b.firebaseio.com/user/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity);
        context=this;
        username="";
    }
    public void btnClick(View view){
        startActivity(new Intent(this,RecordActivity.class));
    }
    public void btnRecordClick(View view){
        if(username!="") {
            startActivity(new Intent(this, RecordActivity.class).putExtra("username", username));
        }else {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Error");
            builder.setMessage("Please login.");
            builder.setIcon(android.R.drawable.ic_delete);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.create().show();
        }
    }
    public void barCodeScan(View view){
        if(username!=""){
            startActivity(new Intent(this,BarCodeScanActivity.class).putExtra("username",username));
        }else {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Error");
            builder.setMessage("Please login.");
            builder.setIcon(android.R.drawable.ic_delete);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.create().show();
        }
    }
    public void login(View view){
        String username=((EditText)findViewById(R.id.ET_username)).getText().toString();
        String password=((EditText)findViewById(R.id.ET_password)).getText().toString();
        login(username,password);
        Log.d("Login","start");
    }
    public void logout(View view){
        setUsername("");
        ((TextView)findViewById(R.id.loginName)).setText("未登入");
        findViewById(R.id.logoutBtn).setVisibility(View.GONE);
        show("Logout");
    }
    public void signUp(View view){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        final View dialogView= getLayoutInflater().inflate(R.layout.dialog_signup,null);
        builder.setView(dialogView);
        builder.setPositiveButton("Sign up", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String username=((EditText)dialogView.findViewById(R.id.signUpUsername)).getText().toString();
                String password=((EditText)dialogView.findViewById(R.id.signUpPassword)).getText().toString();
                addNewUser(username,password);
                show("Sign up successful.");
                login(username,password);
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }
    public void addNewUser(String username,String password){
        Firebase.setAndroidContext(this);
        Firebase firebaseRef=new Firebase(firebaseURL);
        firebaseRef.child(username).setValue(new User(password));
    }
    public void login(final String username, final String password){
        Firebase.setAndroidContext(this);
        Firebase firebaseRef = new Firebase( firebaseURL );


// Queries can only order by one key at a time
//        Query queryRef = firebaseRef.orderByChild("password");
//        queryRef.addChildEventListener(new ChildEventListener() {

        firebaseRef.child(username).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                if(snapshot.getKey()!="password")
                    return;
                Log.d("get data",snapshot.toString());
                String firebasePassword = snapshot.getValue().toString();
                Log.d("get username&password", "username = " + username + " , password = " + firebasePassword);

                if(password.equals(firebasePassword)){
                    setUsername(username);
                    ((TextView)findViewById(R.id.loginName)).setText("Hello , "+username);
                    findViewById(R.id.logoutBtn).setVisibility(View.VISIBLE);
                    show("Login successful");
                    Log.d("Login status","successful");
                }else{
                    show("Login failed");
                    Log.d("Login status","failed");
                }
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
    public void setUsername(String username){
        this.username=username;
        Log.d("set username",username);
    }
    public void show(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
