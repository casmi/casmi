/*
 *   casmi
 *   http://casmi.github.com/
 *   Copyright (C) 2011, Xcoo, Inc.
 *
 *  casmi is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package casmi.util;

import java.util.TimeZone;

/**
 * <p>
 * Easy scheduler class using a UNIX cron pattern.
 * </p>
 * 
 * Wraps cron4j library (http://www.sauronsoftware.it/projects/cron4j/) licensed
 * by LGPL.
 * 
 * @author T. Takeuchi
 * 
 */
public class Cron {

    /** Scheduler object of cron4j library. */
    private final it.sauronsoftware.cron4j.Scheduler cron4jScheduler =
        new it.sauronsoftware.cron4j.Scheduler();

    /** Task id auto-generated by cron4j library. */
    private final String id;

    /**
     * Constructor.
     * 
     * @param cronPattern Cron pattern for the task scheduling.
     * @param task Task as a Runnable object.
     */
    public Cron(String cronPattern, Runnable task) {
        id = cron4jScheduler.schedule(cronPattern, task);
    }

    /**
     * Changes a scheduling cron pattern.
     * 
     * @param cronPattern cron pattern for the task scheduling.
     */
    public void schedule(String cronPattern) {
        cron4jScheduler.reschedule(id, cronPattern);
    }

    /**
     * Starts the scheduler.
     * When the scheduled is started the supplied tasks are executed at the given moment.
     * 
     * @throws IllegalStateException Thrown if this scheduler is already started.
     */
    public void start() throws IllegalStateException {
        cron4jScheduler.start();
    }

    /**
     * Stops the scheduler execution.
     * Before returning, it waits the end of all the running tasks previously launched.
     * Once the scheduler has been stopped it can be started again with a start() call.
     * 
     * @throws IllegalStateException Thrown if this scheduler is not started.
     */
    public void stop() throws IllegalStateException {
        cron4jScheduler.stop();
    }
    
    /**
     * Executes immediately a task.
     */
    public void exec() {
        cron4jScheduler.launch(cron4jScheduler.getTask(id));
    }

    /**
     * Tests if this scheduler is started.
     * 
     * @return true if the scheduler is started, false if it is stopped.
     */
    public boolean isStarted() {
        return cron4jScheduler.isStarted();
    }

    /**
     * Retrieves the cron pattern of a task.
     * 
     * @return The cron pattern of the task, or null if the task was not found.
     */
    public String getCronPattern() {
        return cron4jScheduler.getSchedulingPattern(id).toString();
    }

    /**
     * Retrieves the Runnable object of a task.
     * 
     * @return The Runnable object of the task, or null if the task was not found.
     */
    @SuppressWarnings("deprecation")
    public Runnable getTask() {
        return cron4jScheduler.getTaskRunnable(id);
    }

    /**
     * Returns the time zone applied by the cron scheduler.
     * 
     * @return The time zone applied by the cron scheduler.
     */
    public TimeZone getTimeZone() {
        return cron4jScheduler.getTimeZone();
    }

    /**
     * Sets the time zone applied by the scheduler.
     * Current system time is adapted to the supplied time zone before comparing
     * it with registered scheduling patterns.
     * The result is that any supplied scheduling pattern is treated according to
     * the specified time zone.
     * In example, suppose:
     * 
     * System time: 10:00
     * System time zone: GMT+1
     * Scheduler time zone: GMT+3
     * 
     * The scheduler, before comparing system time with patterns, translates 10:00
     * from GMT+1 to GMT+3. It means that 10:00 becomes 12:00. The resulted time
     * is then used by the scheduler to activate tasks. So, in the given configuration
     * at the given moment, any task scheduled as 0 12 * * * will be executed,
     * while any 0 10 * * * will not.
     * 
     * @param timeZone The time zone applied by the cron scheduler.
     */
    public void setTimeZone(TimeZone timeZone) {
        cron4jScheduler.setTimeZone(timeZone);
    }

    @Override
    public String toString() {
        String out = "Cron pattern: " + getCronPattern() + "\n" +
            "Task: " + getTask() + "\n" +
            "Time zone: " + getTimeZone();
        return out;
    }
}
