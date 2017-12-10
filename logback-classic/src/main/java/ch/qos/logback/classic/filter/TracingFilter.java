package ch.qos.logback.classic.filter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.boolex.EvaluationException;
import ch.qos.logback.core.boolex.EventEvaluator;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Queue;
import org.apache.commons.collections4.queue.CircularFifoQueue;

public class TracingFilter extends Filter<ILoggingEvent> {

    //EventEvaluator<ILoggingEvent> activateEvaluator;
    //EventEvaluator<ILoggingEvent> deactivateEvaluator;
    String activateRegex;
    String deactivateRegex;
    String delayedActivateRegex;

    Pattern activatePattern;
    Pattern delayedActivatePattern;
    Pattern deactivatePattern;

    @Override
    public void start() {
        if (activateRegex != null || deactivateRegex != null) {
            if (activateRegex != null)
                activatePattern = Pattern.compile(activateRegex);
            if (delayedActivateRegex != null)
                delayedActivatePattern = Pattern.compile(delayedActivateRegex);
            if (deactivateRegex != null)
                deactivatePattern = Pattern.compile(deactivateRegex);
            super.start();
        } else {
            addError("No evaluator set for filter " + this.getName());
        }
    }

    public String getActivateRegex() {
        return activateRegex;
    }

    public void setActivateRegex(String activateRegex) {
        this.activateRegex = activateRegex;
    }

    public String getDeactivateRegex() {
        return deactivateRegex;
    }

    public void setDeactivateRegex(String deactivateRegex) {
        this.deactivateRegex = deactivateRegex;
    }

    public String getDelayedActivateRegex() {
        return delayedActivateRegex;
    }

    public void setDelayedActivateRegex(String delayedActivateRegex) {
        this.delayedActivateRegex = delayedActivateRegex;
    }


    public FilterReply decide(ILoggingEvent event) {
        if (!isStarted()) {
            return FilterReply.NEUTRAL;
        }

        if (activatePattern != null) {
            Matcher matcher = activatePattern.matcher(event.getMessage());
            if (matcher.matches()) {
                event.getLoggerContext().setTracingActive();
            }
        }

        if (deactivatePattern != null) {
            Matcher matcher = deactivatePattern.matcher(event.getMessage());
            if (matcher.matches()) {
                event.getLoggerContext().setTracingInactive();
            }
        }

        if (delayedActivatePattern != null) {
            Matcher matcher = deactivatePattern.matcher(event.getMessage());
            if (matcher.matches()) {
                Collection<ILoggingEvent> buffered = event.getLoggerContext().getAllLogEvents();

                for (ILoggingEvent e : buffered) {
                    
                }
            }
        }


        return FilterReply.NEUTRAL;
    }
}
