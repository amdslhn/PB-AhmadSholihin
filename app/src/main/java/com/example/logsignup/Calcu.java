package com.example.logsignup;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Calcu extends AppCompatActivity {

    private EditText etPrice, etDiscount;
    private Button btnCalculate;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calcu);

        etPrice = findViewById(R.id.price);
        etDiscount = findViewById(R.id.discount);
        btnCalculate = findViewById(R.id.btn_calculate);
        tvResult = findViewById(R.id.tv_result);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateDiscount();
            }
        });
    }

    private void calculateDiscount() {
        String priceInput = etPrice.getText().toString();
        String discountInput = etDiscount.getText().toString();

        if (priceInput.isEmpty() || discountInput.isEmpty()) {
            Toast.makeText(this, "Masukkan harga dan diskon!", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceInput);
        double discountPercent = Double.parseDouble(discountInput);

        if (discountPercent < 0 || discountPercent > 100) {
            Toast.makeText(this, "Persentase diskon harus antara 0 - 100!", Toast.LENGTH_SHORT).show();
            return;
        }

        double discountAmount = (discountPercent / 100) * price;
        double finalPrice = price - discountAmount;

        tvResult.setText(String.format("Harga setelah diskon: Rp %.2f", finalPrice));
    }
}
