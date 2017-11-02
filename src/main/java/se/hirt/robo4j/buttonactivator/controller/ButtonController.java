/*
 * Copyright (C) 2014-2017. Miroslav Wengner, Marcus Hirt
 * This LcdExampleController.java  is part of robo4j.
 * module: robo4j-rpi-lcd-example
 *
 * robo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * robo4j is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with robo4j .  If not, see <http://www.gnu.org/licenses/>.
 */
package se.hirt.robo4j.buttonactivator.controller;

import com.robo4j.ConfigurationException;
import com.robo4j.CriticalSectionTrait;
import com.robo4j.RoboContext;
import com.robo4j.RoboUnit;
import com.robo4j.configuration.Configuration;
import com.robo4j.logging.SimpleLoggingUtil;

/**
 * This configurable controller will control the button presses.
 * 
 * @author Marcus Hirt (@hirt)
 * @author Miroslav Wengner (@miragemiko)
 */
@CriticalSectionTrait
public class ButtonController extends RoboUnit<String> {

	private int pressTime;
	private boolean isButtonPressRunning;

	public ButtonController(RoboContext context, String id) {
		super(String.class, context, id);
	}

	@Override
	public void onMessage(String message) {
		if (!isButtonPressRunning) {
			process(message);
		} else {
			SimpleLoggingUtil.print(getClass(), "Skipping " + message + " due to test already running!");
		}
	}

	private void process(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInitialization(Configuration configuration) throws ConfigurationException {
		pressTime = configuration.getInteger("pressTime", 400);
	}

}
