/**
 * Logback: the reliable, generic, fast and flexible logging framework.
 * Copyright (C) 1999-2015, QOS.ch. All rights reserved.
 *
 * This program and the accompanying materials are dual-licensed under
 * either the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation
 *
 *   or (per the licensee's choosing)
 *
 * under the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation.
 */
package ch.qos.logback.classic.joran.action;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.core.joran.action.Action;
import ch.qos.logback.core.joran.action.ActionConst;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import org.xml.sax.Attributes;

import java.util.List;

/**
 * Action to handle the <level> element nested within <logger> element. 
 * 
 * <p>This action is <b>deprecated</b>. Use the level attribute within the logger
 * element.
 * 
 * @author Ceki Gulcu
 */
public class TracingRegexAction extends Action {

    boolean inError = false;

    public void begin(InterpretationContext ec, String name, Attributes attributes) {
        Object o = ec.peekObject();

        if (!(o instanceof Logger)) {
            inError = true;
            addError("For element <level>, could not find a logger at the top of execution stack.");
            return;
        }

        Logger l = (Logger) o;

        String loggerName = l.getName();

        String regexStr = ec.subst(attributes.getValue(ActionConst.VALUE_ATTR));
        // addInfo("Encapsulating logger name is [" + loggerName
        // + "], level value is  [" + levelStr + "].");

        String[] regexes = regexStr.split("~");

        if (regexes.length==3) {
            l.setActivateRegex(regexes[0]);
            l.setDeactivateRegex(regexes[1]);
            l.setDelayedActivateRegex(regexes[2]);
        }

        addInfo(loggerName + " level set to " + l.getLevel());
    }

    public void finish(InterpretationContext ec) {
    }

    public void end(InterpretationContext ec, String e) {
    }
}
