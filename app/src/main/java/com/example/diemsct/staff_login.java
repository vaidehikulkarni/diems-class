package com.example.diemsct;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class staff_login extends AppCompatActivity {

    Button adLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.staff_login);

        adLog = (Button)findViewById(R.id.btnalog);
        adLog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                Intent i = new Intent(getApplicationContext(), Staff_Activity.class);
                startActivity(i);
            }
        });
    }
}
