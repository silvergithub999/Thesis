package com.example.thesis.Buttons;

/**
 * Class to convert ButtonValue variables to int values.
 */
public final class ButtonValueConverter {
    /**
     * Converts buttonValue value to int. For example FIVE = 5.
     * If it is not a number button, then returns -1000.
     * @param buttonValue - the value to convert
     * @return the number on the button.
     */
    public static int convertButtonValueToInt(ButtonValue buttonValue) {
        switch (buttonValue) {
            case ZERO:
                return 0;
            case ONE:
                return 1;
            case TWO:
                return 2;
            case THREE:
                return 3;
            case FOUR:
                return 4;
            case FIVE:
                return 5;
            case SIX:
                return 6;
            case SEVEN:
                return 7;
            case EIGHT:
                return 8;
            case NINE:
                return 9;
            default:
                return -1000;
        }
    }


    /**
     * Converts int value on button to buttonValue.
     * If value is invalid, then returns null.
     * @param pinValue - number on the pin button.
     * @return buttonValue of the number.
     */
    public static ButtonValue convertPinIntToButtonValue(int pinValue) {
        switch (pinValue) {
            case 0:
                return ButtonValue.ZERO;
            case 1:
                return ButtonValue.ONE;
            case 2:
                return ButtonValue.TWO;
            case 3:
                return ButtonValue.THREE;
            case 4:
                return ButtonValue.FOUR;
            case 5:
                return ButtonValue.FIVE;
            case 6:
                return ButtonValue.SIX;
            case 7:
                return ButtonValue.SEVEN;
            case 8:
                return ButtonValue.EIGHT;
            case 9:
                return ButtonValue.NINE;
            default:
                return null;
        }
    }
}
