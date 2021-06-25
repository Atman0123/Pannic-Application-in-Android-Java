package com.example.project;

import android.Manifest.permission;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.audiofx.BassBoost;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Button btnlocation,btnmsg,signout,add;
    Button btn,num;
    EditText cnt;
    TextView text1, text2, text3, text4, text5,text7;
    EditText text6;

    FusedLocationProviderClient fusedLocationProviderClient;

    TextView data;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id=item.getItemId();
        
        if(id==R.id.Contact)
        {
            Toast.makeText(this, "Contact", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(MainActivity.this,addContact.class);
            startActivity(intent);
            return  true;
        }
        else if(id==R.id.show)
        {
            Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(MainActivity.this,profile.class);
            startActivity(intent);
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        btnlocation = findViewById(R.id.btnlocation);
        btnmsg = findViewById(R.id.btnmsg);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);
        text5 = findViewById(R.id.text5);
        btn=findViewById(R.id.btn);
        num=findViewById(R.id.number);
        cnt=findViewById(R.id.cnt);
//        text6 = findViewById(R.id.text6);
        text7 = findViewById(R.id.text7);
//        add=findViewById(R.id.add);
        data=findViewById(R.id.data);



            num.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sh=getSharedPreferences("con",MODE_PRIVATE);
                    String num=sh.getString("contact","No Value as of now");

                    text7.setText(num);
                }
            });






        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        btnlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(com.example.project.MainActivity.this, permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                    getLocation();

                } else {
                    ActivityCompat.requestPermissions(com.example.project.MainActivity.this, new String[]{permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });

        btnmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M)
                {
                    if(checkSelfPermission(permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED)
                    {
                        senfmessage();
                    }
                    else
                    {
                        requestPermissions(new String[]{permission.SEND_SMS},1);
                    }
                }


            }
        });


        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(getApplicationContext(),addContact.class);
               startActivity(intent);
            }
        });
    }

    private void senfmessage() {


        try {
            String number= text7.getText().toString().trim();
            String message="  "+text3.getText().toString().trim()+"  "+text4.getText().toString().trim()+"  "+text5.getText().toString().trim();
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number,null,message,null,null);
            Toast.makeText(this,"Message is Sent", Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this,"Message is Not-Sent", Toast.LENGTH_LONG).show();
        }



    }

    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                Location location=task.getResult();
                if(location != null)
                {
                    Geocoder geocoder= new Geocoder(com.example.project.MainActivity.this, Locale.getDefault());

                    try {
                        List<Address> addresses= geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                        text1.setText(Html.fromHtml(
                                "<font color='#6200EE'><b>Latitude : <b><br></font>"
                                +addresses.get(0).getLatitude()
                        ));
                        text2.setText(Html.fromHtml(
                                "<font color='#6200EE'><b>Lognitude : <b><br></font>"
                                        +addresses.get(0).getLongitude()
                        ));

                        text3.setText(Html.fromHtml(
                                "<font color='#6200EE'><b>Country Name : <b><br></font>"
                                        +addresses.get(0).getCountryName()
                        ));
                        text4.setText(Html.fromHtml(
                                "<font color='#6200EE'><b>Locality : <b><br></font>"
                                        +addresses.get(0).getLocality()
                        ));
                        text5.setText(Html.fromHtml(
                                "<font color='#6200EE'><b>Address : <b><br></font>"
                                        +addresses.get(0).getAddressLine(0)
                        ));
                        addContact c=new addContact();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }


    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        //startActivity(new Intent(getApplicationContext(),login.class));
    }
}