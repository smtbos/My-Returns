package com.smtbos.myreturns;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    TextView txt_returns, txt_current_value, txt_lastupdte;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editor = getSharedPreferences("MyReturns", MODE_PRIVATE).edit();
        preferences = getSharedPreferences("MyReturns", MODE_PRIVATE);
        txt_returns = (TextView) findViewById(R.id.returns);
        txt_current_value = (TextView) findViewById(R.id.curentvalue);
        txt_lastupdte = (TextView) findViewById(R.id.lastupdate);
        requestQueue = Volley.newRequestQueue(this);
        load_data();
    }

    public void btn_click(View view) {
        Intent i = new Intent(this, Settings.class);
        startActivity(i);
    }

    public void load_data() {
        String mutualfund = preferences.getString("mutualfund", "120594");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://api.mfapi.in/mf/" + mutualfund, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray data = (JSONArray) response.get("data");
                    JSONObject last = (JSONObject) data.get(0);

                    String date = (String) last.getString("date");
                    Double nav_double = (Double) last.getDouble("nav");
                    Float nav = nav_double.floatValue();
                    Float units = preferences.getFloat("units", 10);
                    Float amount = preferences.getFloat("amount", 1750);
                    Float current_value = (nav * units);

                    txt_current_value.setText(String.format("%.2f", current_value));
                    txt_returns.setText(String.format("%.2f", (current_value - amount)));
                    txt_lastupdte.setText(date);
                } catch (JSONException ex) {
                    Toast.makeText(getApplicationContext(), "Issue in Application, Contact Developer For Help...", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Issue in Application, Contact Developer For Help...", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        load_data();
    }
}