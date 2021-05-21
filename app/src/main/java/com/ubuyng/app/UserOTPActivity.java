package com.ubuyng.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class UserOTPActivity extends AppCompatActivity {

    private EditText text1, text2, text3, text4;
    private Button verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        text1 = findViewById(R.id.otp1);
        text2 = findViewById(R.id.otp2);
        text3 = findViewById(R.id.otp3);
        text4 = findViewById(R.id.otp4);

        verify = findViewById(R.id.verify_btn);

        verify.animate().setInterpolator(new CycleInterpolator(3)).setDuration(2000).scaleXBy(0).scaleX(1)
                .scaleYBy(0).scaleY(1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.blueAccent));
        } else {
            getWindow().setStatusBarColor(Color.BLACK);
        }

        //To change focus as soon as 1 digit is entered in edit text, "shiftRequest" method is created.
        shiftRequest(text1, text2);
        shiftRequest(text2, text3);
        shiftRequest(text3, text4);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "SuccessFull", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void shiftRequest(final EditText from, final EditText to) {
        from.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String otp1 = from.getText().toString();
                if (otp1.length() > 0) {
                    from.clearFocus();
                    to.requestFocus();
                    to.setCursorVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

}
