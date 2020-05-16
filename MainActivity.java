package com.example.lab33;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    RadioGroup radioGroup1;
    RadioButton radioButton1;
    EditText aInput, bInput, cInput, dInput, yInput;
    int aValue, bValue, cValue, dValue, yValue;
    double mutation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioGroup1 = findViewById(R.id.radioGroup1);
    }

    public void checkButton(View v) {
        int radioId = radioGroup1.getCheckedRadioButtonId();
        radioButton1 = findViewById(radioId);
        Toast.makeText(this, "Selected mutation = " + radioButton1.getText(),
                Toast.LENGTH_SHORT).show();
    }

    public void startExecution(View v) {
        int radioId1 = radioGroup1.getCheckedRadioButtonId();
        radioButton1 = findViewById(radioId1);
        aInput = findViewById(R.id.editText1);
        bInput = findViewById(R.id.editText2);
        cInput = findViewById(R.id.editText3);
        dInput = findViewById(R.id.editText4);
        yInput = findViewById(R.id.editText5);
        aValue = Integer.parseInt(aInput.getText().toString());
        bValue = Integer.parseInt(bInput.getText().toString());
        cValue = Integer.parseInt(cInput.getText().toString());
        dValue = Integer.parseInt(dInput.getText().toString());
        yValue = Integer.parseInt(yInput.getText().toString());
        mutation = Double.parseDouble(radioButton1.getText().toString());
        hideKeyboard();

        int[] result = new int[5];
        for (int i = 10; i <= 100; i += 10) {
            int[] a = countGen(i);
            if ( i == 10 || a[0] < result[0]) {
                result = a;
            }
        }
        showToast("Number of iterations = " + result[0] + "\n" +
                aValue + "*" + result[1] + " + " + bValue + "*" + result[2] + " + " + cValue + "*" + result[3] + " + " + dValue + "*" + result[4] + " = " + yValue);
    }

    private void showToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    public void hideKeyboard() {
        try {
            InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            assert inputManager != null;
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int[] countGen(int size) {
        int count = 0;
        Random rand = new Random();
        ArrayList<ArrayList<Integer>> popList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ArrayList<Integer> tmp = new ArrayList<>(4);
            for (int j = 0; j < 4; j++) {
                tmp.add(rand.nextInt(yValue / 2));
            }
            popList.add(tmp);
        }
        do {
            count++;
            double sum = 0;
            int[] delta = new int[size];
            for (int i = 0; i < size; i++) {
                delta[i] = Math.abs(yValue - popList.get(i).get(0) * aValue - popList.get(i).get(1) * bValue - popList.get(i).get(2) * cValue - popList.get(i).get(3) * dValue);
                if (delta[i] == 0) {
                    return new int[]{count, popList.get(i).get(0), popList.get(i).get(1), popList.get(i).get(2), popList.get(i).get(3)};
            }
                sum += 1.0 / delta[i];
            }
            double[] percentParent = new double[size];
            for (int i = 0; i < size; i++) {
                percentParent[i] = 1.0 / (delta[i] * sum) * 100;
            }
            double[] child = new double[size + 1];
            child[0] = 0;
            for (int i = 0; i < size - 1; i++) {
                child[i + 1] = child[i] + percentParent[i];
            }
            child[size - 1] = 100;
            ArrayList<ArrayList<Integer>> parentList = new ArrayList<>();
            int parent;
            for (int i = 0; i < size * 2; i++) {
                parent = rand.nextInt(100);
                for (int j = 0; j < child.length - 1; j++) {
                    if (child[j] <= parent && child[j + 1] > parent) {
                        parentList.add(popList.get(j));
                    }
                }
            }
            popList.clear();
            for (int i = 0; i < parentList.size(); i = i + 2) {
                ArrayList<Integer> nextGen = new ArrayList<>(4);
                parent = (int)(Math.random() * 3 + 1);
                for (int j = 0; j < parent; j++) {
                    nextGen.add(parentList.get(i).get(j));
                }
                for (int j = parent; j < 4; j++) {
                    nextGen.add(parentList.get(i + 1).get(j));
                }
                popList.add(nextGen);
            }
            parentList.clear();
            if (Math.random() < mutation) {
                int change = rand.nextInt(size);
                for (int i = 0; i < change; i++) {
                    popList.get(rand.nextInt(size)).set(rand.nextInt(4),
                            rand.nextInt(yValue / 2));
                }
            }
        } while (true);
    }
}
