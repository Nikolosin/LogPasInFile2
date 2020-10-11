package com.example.logpasinfile2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editLogin;
    private EditText editPassword;
    private Button btnLogin;
    private Button btnReg;
    private CheckBox cbStorage;
    private SharedPreferences checkboxStatus;
    private static String STATUS = "status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitView();
        final File logFile = new File(getApplicationContext().getExternalFilesDir(null), "Data.txt");
        checkboxStatus = getSharedPreferences("CheckboxStatus", MODE_PRIVATE);
        cbStorage.setChecked(checkboxStatus.getBoolean(STATUS, false));
        cbStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsChecked();
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editLogin.getText().length() == 0 | editPassword.getText().length() == 0) {
                    Toast toast = Toast.makeText(MainActivity.this, R.string.error, Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    if (cbStorage.isChecked()) {
                        SaveDataExternal(logFile);
                    } else {
                        SaveDataInternal();
                    }
                    Toast toast = Toast.makeText(MainActivity.this, R.string.ok, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> list;
                if (cbStorage.isChecked()) {
                    list = LoadDataExternal(logFile);
                } else {
                    list = LoadDataInternal();
                }
                if (editLogin.getText().length() == 0 | editPassword.getText().length() == 0) {
                    Toast toast = Toast.makeText(MainActivity.this, R.string.error, Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    if (editLogin.getText().toString().equals(list.get(0)) & editPassword.getText().toString().equals(list.get(1))) {
                        Toast toast = Toast.makeText(MainActivity.this, R.string.checkOk, Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        Toast toast = Toast.makeText(MainActivity.this, R.string.checkNo, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });
    }

    private void InitView() {
        editLogin = findViewById(R.id.editLog);
        editPassword = findViewById(R.id.editPas);
        btnLogin = findViewById(R.id.btnLog);
        btnReg = findViewById(R.id.btnReg);
        cbStorage = findViewById(R.id.checkStorage);
    }

    private void SaveDataInternal() {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput("LogPas", MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert fileOutputStream != null;
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        BufferedWriter bw = new BufferedWriter(outputStreamWriter);
        try {
            String str = "\n" + editLogin.getText().toString() + "\n" + editPassword.getText().toString();
            bw.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void SaveDataExternal(File file) {
        try (FileWriter logWriter = new FileWriter(file)) {
            String str = "\n" + editLogin.getText().toString() + "\n" + editPassword.getText().toString();
            logWriter.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> LoadDataInternal() {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = openFileInput("LogPas");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert fileInputStream != null;
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        ArrayList<String> list = new ArrayList<>();
        try {
            String line = reader.readLine();
            while (line != null) {
                line = reader.readLine();
                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private ArrayList<String> LoadDataExternal(File file) {
        ArrayList<String> list = new ArrayList<>();
        try (FileReader logReader = new FileReader(file);
             BufferedReader reader = new BufferedReader(logReader)) {
            try {
                String line = reader.readLine();
                while (line != null) {
                    line = reader.readLine();
                    list.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void IsChecked() {
        checkboxStatus = getSharedPreferences("CheckboxStatus", MODE_PRIVATE);
        SharedPreferences.Editor myEditor = checkboxStatus.edit();
        boolean status = cbStorage.isChecked();
        if (status) {
            myEditor.putBoolean(STATUS, true);
        } else {
            myEditor.putBoolean(STATUS, false);
        }
        myEditor.apply();
    }
}