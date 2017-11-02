# button-activator
Simple Robo4J example which physically presses an arbitrary button.

Configure the start and end state of the servo, and what time the pointer should be in the end state before returning. That's it.

Use the com.robo4j.hw.rpi.pwm.PWMServoExample in robo4j-hw-rpi to carefully find a good value for the startInput and endInput.