# button-activator
Simple Robo4J example which physically presses an arbitrary button.

Configure the start and end state of the servo, and what time the pointer should be in the end state before returning. That's it.

Use the com.robo4j.hw.rpi.pwm.PWMServoExample in robo4j-hw-rpi to carefully find a good value for the startInput and endInput.

![Button Activator With Lid](http://hirt.se/images/github/button-presser-lid-on.gif)

![Button Activator Without Lid](http://hirt.se/images/github/button-presser-lid-off.gif)

Here are the plans to print one:
[https://www.thingiverse.com/thing:2807950]()

## To build
First build and install Robo4J (see Robo4J README.md).

Next build the fatJar for the button activator:
```bash
./gradlew fatJar
```

Finally run it (don't forget to source the environment.sh from Robo4J):
```bash
sudo java -cp $ROBO4J_PATH:/build/libs/button-activator-alpha-0.4.jar se.hirt.robo4j.buttonactivator.ButtonActivatorMain
```