package com.smtbos.myreturns;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import javax.microedition.khronos.egl.EGLDisplay;

public class Settings extends AppCompatActivity {
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    EditText txt_code, txt_units, txt_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editor = getSharedPreferences("MyReturns", MODE_PRIVATE).edit();
        preferences = getSharedPreferences("MyReturns", MODE_PRIVATE);

        String mutualfund = preferences.getString("mutualfund", "120594");

        Float units = preferences.getFloat("units", 0);
        Float amount = preferences.getFloat("amount", 0);

        txt_code = (EditText) findViewById(R.id.mutualfund);
        txt_units = (EditText) findViewById(R.id.units);
        txt_amount = (EditText) findViewById(R.id.amount);

        txt_code.setText(mutualfund);
        txt_units.setText(String.format("%.3f", (units)));
        txt_amount.setText(String.format("%.2f", (amount)));

    }

    public void btn_save_back(View view) {
        try {
            String mutualfund = String.valueOf(txt_code.getText());
            Float units = Float.parseFloat(String.valueOf(txt_units.getText()));
            Float amount = Float.parseFloat(String.valueOf(txt_amount.getText()));
            editor.putString("mutualfund", mutualfund);
            editor.putFloat("units", units);
            editor.putFloat("amount", amount);
            editor.commit();
            finish();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Issue in Application, Contact Developer For Help...", Toast.LENGTH_LONG).show();

        }
    }
}