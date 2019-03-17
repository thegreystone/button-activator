/*
 *  Copyright (C) 2017 Marcus Hirt
 *                     www.hirt.se
 *
 * This software is free:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESSED OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright (C) Marcus Hirt, 2017
 */
package se.hirt.robo4j.buttonactivator.controller;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.pi4j.io.gpio.GpioFactory;
import com.robo4j.ConfigurationException;
import com.robo4j.CriticalSectionTrait;
import com.robo4j.RoboContext;
import com.robo4j.RoboUnit;
import com.robo4j.configuration.Configuration;
import com.robo4j.hw.rpi.pwm.PWMServo;
import com.robo4j.logging.SimpleLoggingUtil;
import com.robo4j.socket.http.codec.StringMessage;

/**
 * This configurable controller will control the button presses.
 * 
 * @author Marcus Hirt (@hirt)
 */
@CriticalSectionTrait
public class ButtonController extends RoboUnit<StringMessage> {
	private int pressTime;
	private float startInput;
	private float endInput;
	private int pinAddress;
	private PWMServo servo;
	
	private boolean isButtonPressRunning;

	public ButtonController(RoboContext context, String id) {
		super(StringMessage.class, context, id);
	}

	@Override
	public void onMessage(StringMessage message) {
		if (!isButtonPressRunning) {
			process(message);
		} else {
			SimpleLoggingUtil.print(getClass(), "Skipping " + message + " due to test already running!");
		}
	}

	private void process(StringMessage message) {
		if ("push".equals(message.getMessage())) {
			schedulePress();
		} else {
			SimpleLoggingUtil.print(getClass(), "Got unknown message " + message);
		}
	}

	private void schedulePress() {
		moveToEnd();		
		getContext().getScheduler().schedule(this::moveToStart, pressTime, TimeUnit.MILLISECONDS);
	}

	@Override
	public void onInitialization(Configuration configuration) throws ConfigurationException {
		pressTime = configuration.getInteger("pressTime", 400);
		pinAddress = configuration.getInteger("pinAddress", 1);

		Float startInputParam = configuration.getFloat("startInput", null);
		if (startInputParam == null) {
			throw ConfigurationException.createMissingConfigNameException("startInput");
		}
		startInput = startInputParam;
		
		Float endInputParam = configuration.getFloat("endInput", null);
		if (endInputParam == null) {
			throw ConfigurationException.createMissingConfigNameException("endInput");
		}
		endInput = endInputParam;
		
		servo = new PWMServo(pinAddress, false);
		try {
			servo.setInput(startInput);
		} catch (IOException e) {
			throw new ConfigurationException("Could not initialize servo start position", e);
		}
	}
	
	public void moveToEnd() {
		try {
			servo.setInput(endInput);
		} catch (IOException e) {
			SimpleLoggingUtil.error(getClass(), "Could not move servo to press end point");
		}
	}
	
	public void moveToStart() {
		try {
			servo.setInput(startInput);
		} catch (IOException e) {
			SimpleLoggingUtil.error(getClass(), "Could not move servo to start point");
		}
	}

	@Override
	public void shutdown() {
		super.shutdown();
		GpioFactory.getInstance().shutdown();
	}

}
