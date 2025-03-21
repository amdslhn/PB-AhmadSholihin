package com.example.logsignup;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CatatanKeuangan extends AppCompatActivity {

    private EditText setJumlah, setDeskripsi;
    private RadioButton Pemasukan, Pengeluaran;
    private Button btnSimpan;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private SupTransaction adapter;
    private List<Transaction> transactionList = new ArrayList<>();
    private TextView totalSaldo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catatan_keuangan);

        databaseReference = FirebaseDatabase.getInstance().getReference("transactions");

        setJumlah = findViewById(R.id.setJumlah);
        setDeskripsi = findViewById(R.id.setDeskripsi);
        Pemasukan = findViewById(R.id.Pemasukan);
        Pengeluaran = findViewById(R.id.Pengeluaran);
        btnSimpan = findViewById(R.id.btnSimpan);
        totalSaldo = findViewById(R.id.totalSaldo);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SupTransaction(transactionList);
        recyclerView.setAdapter(adapter);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanTransaksi();
            }
        });

        loadTransactions();
    }

    private void simpanTransaksi() {
        String id = databaseReference.push().getKey(); // Generate ID unik
        String jenis = Pemasukan.isChecked() ? "Pemasukan" : "Pengeluaran";
        int jumlah = Integer.parseInt(setJumlah.getText().toString().trim());
        String deskripsi = setDeskripsi.getText().toString().trim();
        String tanggal = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        Transaction transaksi = new Transaction(id, jenis, jumlah, deskripsi, tanggal);

        assert id != null;
        databaseReference.child(id).setValue(transaksi)
                .addOnSuccessListener(aVoid -> Toast.makeText(CatatanKeuangan.this, "Transaksi berhasil disimpan!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(CatatanKeuangan.this, "Gagal menyimpan transaksi", Toast.LENGTH_SHORT).show());
    }

    private void loadTransactions() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                transactionList.clear();
                int saldo = 0;

                for (DataSnapshot data : snapshot.getChildren()) {
                    Transaction transaksi = data.getValue(Transaction.class);
                    if (transaksi != null) {
                        transactionList.add(transaksi);
                        saldo += transaksi.getJenis().equals("Pemasukan") ? transaksi.getJumlah() : -transaksi.getJumlah();
                    }
                }

                adapter.notifyDataSetChanged();
                totalSaldo.setText("Total Saldo: Rp " + saldo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CatatanKeuangan.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
