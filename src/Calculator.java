import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * A simple calculator built using Java Swing.
 * <p>
 * This program creates a basic calculator GUI that performs
 * simple arithmetic operations like addition, subtraction,
 * multiplication, division, negation, percentage, and square root.
 * </p>
 * <p>
 * The calculator layout and color theme are inspired by iOS-style
 * design. It uses AWT and Swing components such as JFrame, JPanel,
 * JLabel, and JButton.
 * </p>
 * 
 * @author Mahmud
 * @version 1.0
 */
public class Calculator {
    /** Frame width in pixels. */
    int boardWidth = 360;

    /** Frame height in pixels. */
    int boardHeight = 540;

    /** Light gray colour used for top-row buttons. */
    Color customLightGray = new Color(212, 212, 210);

    /** Dark gray colour used for number buttons. */
    Color customDarkGray = new Color(80, 80, 80);

    /** Black colour used for the display and background. */
    Color customBlack = new Color(28, 28, 28);

    /** Orange colour used for operator buttons. */
    Color customOrange = new Color(255, 149, 0);

    /** Values for each button on the calculator, arranged row by row. */
    String[] buttonValues = {
        "AC", "+/-", "%", "÷", 
        "7", "8", "9", "×", 
        "4", "5", "6", "-",
        "1", "2", "3", "+",
        "0", ".", "√", "="
    };

    /** Symbols for the right column of the calculator (operators). */
    String[] rightSymbols = {"÷", "×", "-", "+", "="};

    /** Symbols for the top row of the calculator. */
    String[] topSymbols = {"AC", "+/-", "%"};
    
    /** The main calculator window frame. */
    JFrame frame = new JFrame("Calculator");

    /** Label used to display the numbers and results. */
    JLabel displayLabel = new JLabel();

    /** Panel containing the display label. */
    JPanel displayPanel = new JPanel();

    /** Panel containing all calculator buttons. */
    JPanel buttonsPanel = new JPanel();

    /** Left-hand side operand (first number). */
    String A = "0";

    /** The operator currently selected (e.g., +, -, ×, ÷). */
    String operator = null;

    /** Right-hand side operand (second number). */
    String B = null;

    /**
     * Constructor — sets up the calculator layout, colours, display, and buttons.
     */
    Calculator() {
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        displayLabel.setBackground(customBlack);
        displayLabel.setForeground(Color.white);
        displayLabel.setFont(new Font("Arial", Font.PLAIN, 80));
        displayLabel.setHorizontalAlignment(JLabel.RIGHT);
        displayLabel.setText("0");
        displayLabel.setOpaque(true);

        displayPanel.setLayout(new BorderLayout());
        displayPanel.add(displayLabel);
        frame.add(displayPanel, BorderLayout.NORTH);

        buttonsPanel.setLayout(new GridLayout(5, 4));
        buttonsPanel.setBackground(customBlack);
        frame.add(buttonsPanel);

        for (int i = 0; i < buttonValues.length; i++) {
            JButton button = new JButton();
            String buttonValue = buttonValues[i];
            button.setFont(new Font("Arial", Font.PLAIN, 30));
            button.setText(buttonValue);
            button.setFocusable(false);
            button.setBorder(new LineBorder(customBlack));

            // Assign colors based on button type
            if (Arrays.asList(topSymbols).contains(buttonValue)) {
                button.setBackground(customLightGray);
                button.setForeground(customBlack);
            }
            else if (Arrays.asList(rightSymbols).contains(buttonValue)) {
                button.setBackground(customOrange);
                button.setForeground(Color.white);
            }
            else {
                button.setBackground(customDarkGray);
                button.setForeground(Color.white);
            }

            buttonsPanel.add(button);

            // Add functionality for each button
            button.addActionListener(new ActionListener() {
                /**
                 * Handles the logic when any calculator button is pressed.
                 * 
                 * @param e the ActionEvent triggered by the button press
                 */
                public void actionPerformed(ActionEvent e) {
                    JButton button = (JButton) e.getSource();
                    String buttonValue = button.getText();

                    // Handle operator and equals buttons
                    if (Arrays.asList(rightSymbols).contains(buttonValue)) {
                        if (buttonValue == "=") {
                            if (A != null) {
                                B = displayLabel.getText();
                                double numA = Double.parseDouble(A);
                                double numB = Double.parseDouble(B);

                                if (operator == "+") {
                                    displayLabel.setText(removeZeroDecimal(numA+numB));
                                }
                                else if (operator == "-") {
                                    displayLabel.setText(removeZeroDecimal(numA-numB));
                                }
                                else if (operator == "×") {
                                    displayLabel.setText(removeZeroDecimal(numA*numB));
                                }
                                else if (operator == "÷") {
                                    displayLabel.setText(removeZeroDecimal(numA/numB));
                                }
                                clearAll();
                            }
                        }
                        else if ("+-×÷".contains(buttonValue)) {
                            if (operator == null) {
                                A = displayLabel.getText();
                                displayLabel.setText("0");
                                B = "0";
                            }
                            operator = buttonValue;
                        }
                    }

                    // Handle top-row buttons
                    else if (Arrays.asList(topSymbols).contains(buttonValue)) {
                        if (buttonValue == "AC") {
                            clearAll();
                            displayLabel.setText("0");
                        }
                        else if (buttonValue == "+/-") {
                            double numDisplay = Double.parseDouble(displayLabel.getText());
                            numDisplay *= -1;
                            displayLabel.setText(removeZeroDecimal(numDisplay));
                        }
                        else if (buttonValue == "%") {
                            double numDisplay = Double.parseDouble(displayLabel.getText());
                            numDisplay /= 100;
                            displayLabel.setText(removeZeroDecimal(numDisplay));
                        }
                    }

                    // Handle digits and decimal point
                    else {
                        if (buttonValue == ".") {
                            if (!displayLabel.getText().contains(buttonValue)) {
                                displayLabel.setText(displayLabel.getText() + buttonValue);
                            }
                        }
                        else if ("0123456789".contains(buttonValue)) {
                            if (displayLabel.getText() == "0") {
                                displayLabel.setText(buttonValue);
                            }
                            else {
                                displayLabel.setText(displayLabel.getText() + buttonValue);
                            }
                        }
                    }
                }
            });

            frame.setVisible(true);
        }
    }

    /**
     * Resets the calculator's stored operands and operator.
     * Used when AC is pressed or after a calculation completes.
     */
    void clearAll() {
        A = "0";
        operator = null;
        B = null;
    }

    /**
     * Removes unnecessary decimal zeros from a number.
     * For example, 5.0 → "5", 5.5 → "5.5".
     *
     * @param numDisplay the number to format
     * @return a formatted string without redundant ".0"
     */
    String removeZeroDecimal(double numDisplay) {
        if (numDisplay % 1 == 0) {
            return Integer.toString((int) numDisplay);
        }
        return Double.toString(numDisplay);
    }
}
