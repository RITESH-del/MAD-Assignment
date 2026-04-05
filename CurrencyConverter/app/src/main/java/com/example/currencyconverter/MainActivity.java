package com.example.currencyconverter;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.HashMap;

import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
    Spinner spinner;
    Spinner spinner1;
    EditText amountEditText;
    TextView resultTextView;
    Button convertButton, settingBtn;
    ArrayAdapter<String> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        spinner = findViewById(R.id.myspinner);
        amountEditText = findViewById(R.id.editTextNumberDecimal);
        resultTextView = findViewById(R.id.resultTextView);
        convertButton = findViewById(R.id.convertButton);
        settingBtn = findViewById(R.id.settingBtn);
        spinner1 = findViewById(R.id.myspinner1);

        String[] items = {"USD", "INR", "EUR", "JPY"};

        adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, items
        );

        spinner.setAdapter(adapter);
        spinner1.setAdapter(adapter);

        settingBtn.setOnClickListener(v-> {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        });

        exportExchange();
    }

    private void exportExchange(){
        HashMap<String, Double> exchangeRates = new HashMap<>();
        exchangeRates.put("USD", 1.0);
        exchangeRates.put("INR", 94.0);
        exchangeRates.put("EUR", 0.85);
        exchangeRates.put("JPY", 110.0);


        convertButton.setOnClickListener(v -> {
            String value1 = spinner.getSelectedItem().toString();
            String value2 = spinner1.getSelectedItem().toString();

            String input = amountEditText.getText().toString();

            if (input.isEmpty()) {
                resultTextView.setText("Enter amount");
                return;
            }

            double amount = Double.parseDouble(input);
            double result = (amount / exchangeRates.get(value1)) * exchangeRates.get(value2); // convert to USD then multiply by there conversion rate
            resultTextView.setText(String.format("%s %.2f %s", "Result: ", result, value2));
        });
    }


}