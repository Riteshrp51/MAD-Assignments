package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private TextView tvExpression, tvResult;
    private StringBuilder expression = new StringBuilder();
    private boolean isParenthesisOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvExpression = findViewById(R.id.tvExpression);
        tvResult = findViewById(R.id.tvResult);

        setClickListeners();
    }

    private void setClickListeners() {
        int[] buttonIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
                R.id.btnDot, R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply, R.id.btnDivide, R.id.btnPercent,
                R.id.btnAC, R.id.btnEquals, R.id.btnParentheses, R.id.btnSign
        };

        View.OnClickListener listener = view -> {
            Button b = (Button) view;
            onButtonClick(b.getText().toString());
        };

        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(listener);
        }

        findViewById(R.id.btnBackspace).setOnClickListener(v -> {
            if (expression.length() > 0) {
                expression.deleteCharAt(expression.length() - 1);
                updateDisplay();
            }
        });
    }

    private void onButtonClick(String text) {
        switch (text) {
            case "C":
                expression.setLength(0);
                tvResult.setText("0");
                isParenthesisOpen = false;
                break;
            case "( )":
                if (!isParenthesisOpen) {
                    expression.append("(");
                    isParenthesisOpen = true;
                } else {
                    expression.append(")");
                    isParenthesisOpen = false;
                }
                break;
            case "+/-":
                toggleSign();
                break;
            case "=":
                String finalResult = calculate(expression.toString());
                tvResult.setText(finalResult);
                expression.setLength(0);
                if (!finalResult.equals("Error")) {
                    expression.append(finalResult);
                }
                break;
            case "÷": case "×": case "-": case "+":
                if (expression.length() > 0) {
                    char lastChar = expression.charAt(expression.length() - 1);
                    if (isOperator(lastChar)) {
                        expression.deleteCharAt(expression.length() - 1);
                    }
                    expression.append(text);
                }
                break;
            case "%":
                handlePercent();
                break;
            default:
                expression.append(text);
                break;
        }
        updateDisplay();
    }

    private void updateDisplay() {
        tvExpression.setText(expression.toString());
        String preview = calculate(expression.toString());
        if (!preview.equals("Error") && !expression.toString().isEmpty()) {
            tvResult.setText(preview);
        } else if (expression.toString().isEmpty()) {
            tvResult.setText("0");
        }
    }

    private void toggleSign() {
        if (expression.length() == 0) return;
        String expr = expression.toString();
        String lastToken = getLastToken(expr);
        if (lastToken.isEmpty()) return;

        int startIndex = expr.lastIndexOf(lastToken);
        if (startIndex > 0 && expr.charAt(startIndex - 1) == '-') {
            expression.deleteCharAt(startIndex - 1);
        } else {
            expression.insert(startIndex, "-");
        }
    }

    private void handlePercent() {
        if (expression.length() == 0) return;
        String expr = expression.toString();
        String lastToken = getLastToken(expr);
        if (!lastToken.isEmpty()) {
            try {
                double val = Double.parseDouble(lastToken) / 100.0;
                expression.setLength(expression.length() - lastToken.length());
                expression.append(formatResult(val));
            } catch (Exception ignored) {}
        }
    }

    private String getLastToken(String expr) {
        StringBuilder sb = new StringBuilder();
        for (int i = expr.length() - 1; i >= 0; i--) {
            char c = expr.charAt(i);
            if (Character.isDigit(c) || c == '.') {
                sb.insert(0, c);
            } else {
                break;
            }
        }
        return sb.toString();
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '×' || c == '÷';
    }

    private String calculate(String expr) {
        if (expr.isEmpty()) return "0";
        try {
            String normalizedExpr = expr.replace('×', '*').replace('÷', '/');
            return formatResult(evaluate(normalizedExpr));
        } catch (Exception e) {
            return "Error";
        }
    }

    private String formatResult(double result) {
        if (result == (long) result) return String.valueOf((long) result);
        return String.valueOf(result);
    }

    private double evaluate(String expression) {
        try {
            char[] tokens = expression.toCharArray();
            Stack<Double> values = new Stack<>();
            Stack<Character> ops = new Stack<>();

            for (int i = 0; i < tokens.length; i++) {
                if (tokens[i] == ' ') continue;
                if (tokens[i] >= '0' && tokens[i] <= '9' || tokens[i] == '.') {
                    StringBuilder sbuf = new StringBuilder();
                    while (i < tokens.length && (tokens[i] >= '0' && tokens[i] <= '9' || tokens[i] == '.'))
                        sbuf.append(tokens[i++]);
                    values.push(Double.parseDouble(sbuf.toString()));
                    i--;
                } else if (tokens[i] == '(') {
                    ops.push(tokens[i]);
                } else if (tokens[i] == ')') {
                    while (ops.peek() != '(')
                        values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                    ops.pop();
                } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/') {
                    while (!ops.empty() && hasPrecedence(tokens[i], ops.peek()))
                        values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                    ops.push(tokens[i]);
                }
            }
            while (!ops.empty())
                values.push(applyOp(ops.pop(), values.pop(), values.pop()));

            return values.isEmpty() ? 0 : values.pop();
        } catch (Exception e) {
            throw new RuntimeException("Error");
        }
    }

    private boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') return false;
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) return false;
        return true;
    }

    private double applyOp(char op, double b, double a) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/': if (b == 0) throw new UnsupportedOperationException("Error"); return a / b;
        }
        return 0;
    }
}