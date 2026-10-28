package com.example.midexam;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etName, etId;
    TextView tvAge;
    SeekBar seekBarAge;
    Button btnAdd, btnView, btnEdit, btnDelete;

    DatabaseHelper databaseHelper;
    int age = 17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etId = findViewById(R.id.etId);
        tvAge = findViewById(R.id.tvAge);
        seekBarAge = findViewById(R.id.seekBarAge);

        btnAdd = findViewById(R.id.btnAdd);
        btnView = findViewById(R.id.btnView);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);

        databaseHelper = new DatabaseHelper(this);

        seekBarAge.setMax(33);
        seekBarAge.setProgress(0);

        seekBarAge.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(
                            SeekBar seekBar, int progress,
                            boolean fromUser) {
                        age = progress + 17;
                        tvAge.setText("Student Age: " + age);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

        btnAdd.setOnClickListener(v -> addStudent());
        btnView.setOnClickListener(v -> viewStudents());
        btnEdit.setOnClickListener(v -> editStudent());
        btnDelete.setOnClickListener(v -> deleteStudent());
    }

    private void addStudent() {
        String name = etName.getText().toString().trim();
        String id = etId.getText().toString().trim();

        if (name.isEmpty() || id.isEmpty()) {
            showMessage("Please enter Student Name and Student ID");
            return;
        }

        boolean success = databaseHelper.addStudent(id, name, age);

        if (success) {
            showMessage("Student added successfully");
            clearFields();
        } else {
            showMessage("Failed to add. Student ID may already exist.");
        }
    }

    private void viewStudents() {
        Cursor cursor = databaseHelper.viewStudents();
        StringBuilder result = new StringBuilder();

        try {
            if (cursor.getCount() == 0) {
                showMessage("No students found");
                return;
            }

            while (cursor.moveToNext()) {
                String id = cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID));
                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME));
                int studentAge = cursor.getInt(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COL_AGE));

                result.append("Student Name: ").append(name)
                        .append("\nStudent ID: ").append(id)
                        .append("\nStudent Age: ").append(studentAge)
                        .append("\n\n");
            }
        } finally {
            cursor.close();
        }

        new AlertDialog.Builder(this)
                .setTitle("Student List")
                .setMessage(result.toString())
                .setPositiveButton("OK", null)
                .show();
    }

    private void editStudent() {
        String name = etName.getText().toString().trim();
        String id = etId.getText().toString().trim();

        if (name.isEmpty() || id.isEmpty()) {
            showMessage("Enter Student ID and updated name");
            return;
        }

        boolean success = databaseHelper.editStudent(id, name, age);

        if (success) {
            showMessage("Student updated successfully");
            clearFields();
        } else {
            showMessage("Student ID not found");
        }
    }

    private void deleteStudent() {
        String id = etId.getText().toString().trim();

        if (id.isEmpty()) {
            showMessage("Enter Student ID to delete");
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Delete Student")
                .setMessage("Delete student with ID " + id + "?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (dialog, which) -> {
                    boolean success = databaseHelper.deleteStudent(id);

                    if (success) {
                        showMessage("Student deleted successfully");
                        clearFields();
                    } else {
                        showMessage("Student ID not found");
                    }
                })
                .show();
    }

    private void clearFields() {
        etName.setText("");
        etId.setText("");
        seekBarAge.setProgress(0);
        age = 17;
        tvAge.setText("Student Age: 17");
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
