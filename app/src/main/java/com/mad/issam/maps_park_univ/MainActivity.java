package com.mad.issam.maps_park_univ;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    public static final String TYPE = "com.mad.issam.maps_park_univ.TYPE";
    public static final String LONGEUR = "com.mad.issam.maps_park_univ.LONGEUR";
    private Button tout ;
    private Button autour ;
    private Button tour ;
    private int  a ;
    public EditText lon ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Intent typee = new Intent(getApplicationContext(),MapsActivity.class);
        tout = (Button)findViewById(R.id.tout);
        tout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typ ="toutes";

                typee.putExtra(TYPE,typ);
                startActivity(typee);
            }
        });

        autour = (Button)findViewById(R.id.un);
        autour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typ ="une";
                typee.putExtra(TYPE,typ);
                startActivity(typee);
            }
        });

        tour = (Button)findViewById(R.id.tour);
        tour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lon = (EditText)findViewById(R.id.km);
                int kmm = Integer.parseInt(lon.getText().toString());
                String typ ="toure";
                typee.putExtra(TYPE,typ);
                typee.putExtra(LONGEUR,kmm);
                startActivity(typee);

            }
        });

    }
}
