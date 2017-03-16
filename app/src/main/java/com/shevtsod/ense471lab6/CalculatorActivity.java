package com.shevtsod.ense471lab6;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Arrays;

public class CalculatorActivity extends AppCompatActivity {

    //Defines the maximum number of digits to hold in currentValue
    public static final int MAX_DIGITS = 20;
    //The string that is displayed when equals is pressed
    public static final String RESULT = "ANS";
    //Defines which symbols are operators
    public static final char[] OPERATORS = {'+', '-', '*', '/'};

    private TextView expression;
    private TextView currentValue;
    private boolean enteredValue = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        //Get expression and currentValue from the activity and store them in this class
        expression = (TextView) findViewById(R.id.expression);
        currentValue = (TextView) findViewById(R.id.currentValue);

        //Reset expression and currentValue to 0
        expression.setText("0");
        currentValue.setText("0");

        //Subscribe undo button as a LongClickListener with event handler LongClickUndo
        ImageButton undoButton = (ImageButton) findViewById(R.id.btn_undo);
        undoButton.setOnLongClickListener(new LongClickUndo());
    }

    /**
     * Checks if expression's last character is currently an operator and returns true if so
     * @return true if last character of expression is an operator
     */
    private boolean isExpressionEndAnOperator() {
        if(expression.length() == 0)
            return false;

        String expressionString = expression.getText().toString();
        String expressionStringEnd = Character.toString(expressionString.charAt(expressionString.length() - 1));
        return new String(OPERATORS).contains(expressionStringEnd);
    }

    /*------------------------------------------ EVENT HANDLERS --------------------------------------------*/

    /**
     * Event: A number button has been clicked. Set the value of currentValue appropriately
     * @param v The button that was clicked
     */
    public void numberButtonPressed(View v) {
        //Don't do anything if currentValue already has MAX_DIGITS
        if(currentValue.getText().toString().length() >= MAX_DIGITS)
            return;

        String numberString;
        //From the element ID, decide which number was pressed
        switch(v.getId()) {
            case R.id.btn_0: numberString = "0";
                break;
            case R.id.btn_1: numberString = "1";
                break;
            case R.id.btn_2: numberString = "2";
                break;
            case R.id.btn_3: numberString = "3";
                break;
            case R.id.btn_4: numberString = "4";
                break;
            case R.id.btn_5: numberString = "5";
                break;
            case R.id.btn_6: numberString = "6";
                break;
            case R.id.btn_7: numberString = "7";
                break;
            case R.id.btn_8: numberString = "8";
                break;
            case R.id.btn_9: numberString = "9";
                break;
            default: return;
        }

        String currentValueString = currentValue.getText().toString();

        //Modify the value of currentValue (if it is 0, remove the 0)
        if(currentValueString.equals("0")) {
            currentValue.setText(numberString);
        } else {
            currentValueString += numberString;
            currentValue.setText(currentValueString);
        }

        enteredValue = true;
    }

    /**
     * Event: An operator button was clicked. Add the operator to expression
     * @param v The button that was clicked
     */
    public void operatorButtonPressed(View v) {
        char operator;
        //Determine which operation was pressed
        switch(v.getId()) {
            case R.id.btn_plus: operator = '+';
                break;
            case R.id.btn_minus: operator = '-';
                break;
            case R.id.btn_mult: operator = '*';
                break;
            case R.id.btn_div: operator = '/';
                break;
            default:
                return;
        }

        //If expression is 0 but currentValue is not 0, remove the 0
        if(expression.getText().equals("0") && !currentValue.getText().equals("0"))
            expression.setText("");

        //Concatenate expression with currentValue and then with operator if the last character is
        //not an operator, otherwise replace the previous operator
        String expressionString = expression.getText().toString();
        String currentValueString = currentValue.getText().toString();
        String newExpression;

        //Check if the final character in the expression is an operator, replace this operator if so
        //Otherwise add the operator
        if(isExpressionEndAnOperator() && !enteredValue) {
            newExpression = expressionString.substring(0, expressionString.length() - 1) + operator;
        } else {
            newExpression = expression.getText().toString() + currentValueString + operator;
            currentValue.setText("0");
            enteredValue = false;
        }

        expression.setText(newExpression);
    }

    /**
     * Event: Undo button clicked. Undo the last character in currentValue, or if it is 0 undo the
     * last character in expression.
     * @param v The button that was clicked
     */
    public void undoPressed(View v) {
        String currentValueString = currentValue.getText().toString();
        String expressionString = expression.getText().toString();

        //If currentValue and expression are both 0, do nothing
        if(currentValue.getText().toString().equals("0") && expression.getText().toString().equals("0"))
            return;

        //If currentValue is of length 1, set it to 0
        if(currentValueString.length() == 1 && !currentValueString.equals("0")) {
            currentValue.setText("0");
        }
        //Otherwise, if currentValue is not 0, remove the last digit in currentValue
        else if(!currentValueString.equals("0")) {
            currentValue.setText(currentValueString.substring(0, currentValueString.length() - 1));
            //If currentValue is ANS, set it to 0
            if(currentValueString.equals(RESULT))
                currentValue.setText("0");
        }
        //Otherwise, if currentValue is 0, and expression is of length 1, set it to 0
        else if(expressionString.length() == 1) {
            expression.setText("0");
        }
        //Otherwise, if currentValue is 0, and expression is not 0, remove the last character in expression
        else if(!expressionString.equals("0")) {
            expression.setText(expressionString.substring(0, expressionString.length() - 1));
            //If expressionString is ANS, set it to 0
            if(expressionString.equals(RESULT))
                expression.setText("0");
        }

        if(!isExpressionEndAnOperator())
            enteredValue = true;
    }

    /**
     * Event: Undo button held. Undo the entire value in currentValue, or if it is 0 undo the entire value
     * in expression.
     */
    private class LongClickUndo implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            String currentValueString = currentValue.getText().toString();

            //When long clicked, if currentValue is 0, clear expression
            if(currentValueString.equals("0"))
                expression.setText("0");
                //Otherwise clear currentValue
            else {
                currentValue.setText("0");
                enteredValue = false;
            }

            return true;
        }
    }

    /**
     * Event: DEL button pressed. Immediately clear both currentValue and expression
     * @param v The button that was clicked
     */
    public void deletePressed(View v) {
        currentValue.setText("0");
        expression.setText("0");
        enteredValue = false;
    }

    /**
     * Event: Equals button clicked. Pass the expression to internal expression parser and calculate the result
     * return the result to the user stored in 'ANS' for further calculations with this result
     * @param v The button that was clicked
     */
    public void equalsPressed(View v) {
        //Would call calculation methods here
        String newValue = "ANS";
        currentValue.setText(newValue);
        expression.setText("0");
    }

    /**
     * Event: Open bracket clicked. Add an open bracket
     * @param v The button that was clicked
     */
    public void openBracketPressed(View v) {
        //Could add open bracket rules here
        String newExpression = expression.getText().toString() + "(";
        expression.setText(newExpression);
    }

    /**
     * Event: Closed bracket clicked. Add a closed bracket
     * @param v The button that was clicked
     */
    public void closedBracketPressed(View v) {
        //Could add closed bracket rules here
        String newExpression;

        if(enteredValue) {
            newExpression = expression.getText().toString() + currentValue.getText().toString() + ")";
            expression.setText(newExpression);
            currentValue.setText("0");
            enteredValue = false;
        } else {
            newExpression = expression.getText().toString() + ")";
        }

        expression.setText(newExpression);
    }

    /**
     * Event: Radix button was clicked (decimal point). Add a radix to currentValue
     * @param v The button that was clicked
     */
    public void radixPressed(View v) {
        String newValue = currentValue.getText() + ".";
        currentValue.setText(newValue);
        enteredValue = true;
    }

}
