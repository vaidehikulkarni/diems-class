package com.example.diemsct;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

public class Contact extends AppCompatActivity {

    TextView contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

//        MainActivity.actionBar.setTitle("Contact Us");
//        MainActivity.actionBarMenu.findItem(R.id.contact).setChecked(true);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Contact Us");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        contact = (TextView) findViewById(R.id.contact);
        contact.setText(Html.fromHtml(getString(R.string.contact_us)));
        final MaterialEditText message = (MaterialEditText) findViewById(R.id.message);
        message.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (view.getId() == R.id.message) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });

        final MaterialEditText name = (MaterialEditText) findViewById(R.id.name);
        final MaterialEditText mail = (MaterialEditText) findViewById(R.id.mail);
        Button button = (Button) findViewById(R.id.btncont);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean cont = true;
                if(name.getText().toString().trim().equals(""))
                {
                    name.setError("Name is required");
                    cont = false;
                }
                if(mail.getText().toString().trim().equals(""))
                {
                    mail.setError("Email Id is required");
                    cont = false;
                }
                if(message.getText().toString().trim().equals(""))
                {
                    message.setError("Message is required");
                    cont = false;
                }

                if(!cont)
                    return;;

                Toast.makeText(Contact.this, "Message submitted successfully", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
