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
package se.hirt.robo4j.buttonactivator;

import com.robo4j.RoboBuilder;
import com.robo4j.RoboBuilderException;
import com.robo4j.RoboContext;
import com.robo4j.RoboReference;
import com.robo4j.socket.http.codec.StringMessage;
import com.robo4j.util.SystemUtil;

import java.io.IOException;

/**
 * Run this class to initialize Robo4J.
 *
 * @author Marcus Hirt (@hirt)
 */
public class ButtonActivatorMain {
    public static void main(String[] args) throws RoboBuilderException, IOException {
        ClassLoader classLoader = ButtonActivatorMain.class.getClassLoader();
        RoboBuilder builder = new RoboBuilder().add(classLoader.getResourceAsStream("robo4j.xml"));
        RoboContext ctx = builder.build();

        System.out.println("State before start:");
        System.out.println(SystemUtil.printStateReport(ctx));
        ctx.start();

        System.out.println("State after start:");
        System.out.println(SystemUtil.printStateReport(ctx));

        final RoboReference<?> httpRef = ctx.getReference("http");
        final RoboReference<StringMessage> ctrlRef = ctx.getReference("controller");
        System.out.println(SystemUtil.printSocketEndPoint(httpRef, ctrlRef));

        // Schedule one initial button press on startup, just because I am too
        // lazy to write tests.
        ctrlRef.sendMessage(new StringMessage("push"));

        System.out.println("Press enter to quit!");
        System.in.read();
        ctx.shutdown();
    }

}
