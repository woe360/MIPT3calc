package com.example.mipt3calc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private StringBuilder input = new StringBuilder();
    private TextView resultTextView;
    private TextView solutionTextView;

    private SharedPreferences sharedPreferences;
    private static final String MEMORY_KEY = "memory";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = findViewById(R.id.result_tv);
        solutionTextView = findViewById(R.id.solution_tv);

        sharedPreferences = getPreferences(MODE_PRIVATE);

        int[] buttonIds = {
                R.id.button_c, R.id.button_sqrt, R.id.button_divide,
                R.id.button_7, R.id.button_8, R.id.button_9, R.id.button_multiply,
                R.id.button_4, R.id.button_5, R.id.button_6, R.id.button_plus,
                R.id.button_1, R.id.button_2, R.id.button_3, R.id.button_minus,
                R.id.button_ac, R.id.button_0, R.id.button_dot, R.id.button_equals,
                R.id.button_percentage, R.id.button_sign,
                R.id.button_mc, R.id.button_mr, R.id.button_ms, R.id.button_mplus, R.id.button_mminus
        };

        for (int buttonId : buttonIds) {
            Button button = findViewById(buttonId);
            button.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        Button button = (Button) view;
        String buttonText = button.getText().toString();

        switch (buttonText) {
            case "C":
                deleteLastCharacter();
                break;
            case "CE":
                clearInput();
                break;
            case "=":
                evaluateExpression();
                break;
            case "+":
            case "-":
            case "*":
            case "/":
                input.append(" ").append(buttonText).append(" ");
                updateResultText();
                break;
            case "SQRT":
                calculateSquareRoot();
                break;
            case "1/x":
                calculateReciprocal();
                break;
            case "%":
                calculatePercentage();
                break;
            case "±":
                toggleSign();
                break;
            case "MC":
                clearMemory();
                break;
            case "MR":
                recallMemory();
                break;
            case "MS":
                storeMemory();
                break;
            case "M+":
                addToMemory();
                break;
            case "M-":
                subtractFromMemory();
                break;
            default:
                input.append(buttonText);
                updateResultText();
                break;
        }
    }

    private void clearInput() {
        input.setLength(0);
        updateResultText();
    }

    private void deleteLastCharacter() {
        if (input.length() > 0) {
            input.deleteCharAt(input.length() - 1);
            updateResultText();
        }
    }

    private void updateResultText() {
        resultTextView.setText(input.toString());
    }

    private void evaluateExpression() {
        try {
            String[] parts = input.toString().split(" ");
            if (parts.length == 3) {
                double num1 = Double.parseDouble(parts[0]);
                String operator = parts[1];
                double num2 = Double.parseDouble(parts[2]);

                double result = performOperation(num1, operator, num2);

                resultTextView.setText(String.valueOf(result));
                solutionTextView.setText(input.toString() + " = " + result);
                clearInput();
            } else {
                throw new IllegalArgumentException("Typo");
            }
        } catch (Exception e) {
            resultTextView.setText("Error");
            solutionTextView.setText("Typo");
        }
    }

    private double performOperation(double num1, String operator, double num2) {
        switch (operator) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                if (num2 != 0) {
                    return num1 / num2;
                } else {
                    throw new ArithmeticException("Division by zero");
                }
            default:
                throw new IllegalArgumentException("Invalid operator");
        }
    }

    private void calculateSquareRoot() {
        try {
            double num = Double.parseDouble(input.toString());
            if (num >= 0) {
                double result = Math.sqrt(num);
                resultTextView.setText(String.valueOf(result));
                solutionTextView.setText("√" + input.toString() + " = " + result);
                clearInput();
            } else {
                throw new IllegalArgumentException("Cannot calculate square root of a negative number");
            }
        } catch (Exception e) {
            resultTextView.setText("Error");
            solutionTextView.setText("Invalid Expression");
            clearInput();
        }
    }

    private void calculateReciprocal() {
        try {
            double num = Double.parseDouble(input.toString());
            if (num != 0) {
                double result = 1 / num;
                resultTextView.setText(String.valueOf(result));
                solutionTextView.setText("1/" + input.toString() + " = " + result);
                clearInput();
            } else {
                throw new ArithmeticException("Reciprocal of zero is undefined");
            }
        } catch (Exception e) {
            resultTextView.setText("Error");
            solutionTextView.setText("Invalid Expression");
            clearInput();
        }
    }

    private void calculatePercentage() {
        try {
            double num = Double.parseDouble(input.toString());
            double result = num / 100;
            resultTextView.setText(String.valueOf(result));
            solutionTextView.setText(input.toString() + "% = " + result);
            clearInput();
        } catch (Exception e) {
            resultTextView.setText("Error");
            solutionTextView.setText("Invalid Expression");
            clearInput();
        }
    }

    private void toggleSign() {
        try {
            double num = Double.parseDouble(input.toString());
            double result = -num;
            resultTextView.setText(String.valueOf(result));
            solutionTextView.setText("-" + input.toString() + " = " + result);
            clearInput();
        } catch (Exception e) {
            resultTextView.setText("Error");
            solutionTextView.setText("Invalid Expression");
            clearInput();
        }
    }
    private void clearMemory() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(MEMORY_KEY);
        editor.apply();
    }

    private void recallMemory() {
        double memoryValue = sharedPreferences.getFloat(MEMORY_KEY, 0);
        input.append(String.valueOf(memoryValue));
        updateResultText();
    }

    private void storeMemory() {
        try {
            double num = Double.parseDouble(input.toString());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat(MEMORY_KEY, (float) num);
            editor.apply();
            clearInput();
        } catch (Exception e) {
            resultTextView.setText("Error");
            solutionTextView.setText("Invalid Expression");
            clearInput();
        }
    }

    private void addToMemory() {
        try {
            double num = Double.parseDouble(input.toString());
            double currentMemoryValue = sharedPreferences.getFloat(MEMORY_KEY, 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat(MEMORY_KEY, (float) (currentMemoryValue + num));
            editor.apply();
            clearInput();
        } catch (Exception e) {
            resultTextView.setText("Error");
            solutionTextView.setText("Invalid Expression");
            clearInput();
        }
    }

    private void subtractFromMemory() {
        try {
            double num = Double.parseDouble(input.toString());
            double currentMemoryValue = sharedPreferences.getFloat(MEMORY_KEY, 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat(MEMORY_KEY, (float) (currentMemoryValue - num));
            editor.apply();
            clearInput();
        } catch (Exception e) {
            resultTextView.setText("Error");
            solutionTextView.setText("Invalid Expression");
            clearInput();
        }
    }
}
