package com.example.solotravellersapp.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.solotravellersapp.R;

public class AddToCalendarActivity extends AppCompatActivity {

    Button addtocalendar_btn, close_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_to_calendar);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addtocalendar_btn = findViewById(R.id.addtocalendar_btn);

        addtocalendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _dialogconfirmationpage();
                showToast("added to calendar!");
            }
        });
    }

    private void _dialogconfirmationpage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View view_ = layoutInflaterAndroid.inflate(R.layout.dialog_confirmation_page, null);
        builder.setView(view_);
        builder.setCancelable(true);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        close_btn = view_.findViewById(R.id.close_btn);

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
