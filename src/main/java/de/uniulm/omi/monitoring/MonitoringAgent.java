/*
 *
 *  * Copyright (c) 2014 University of Ulm
 *  *
 *  * See the NOTICE file distributed with this work for additional information
 *  * regarding copyright ownership.  Licensed under the Apache License, Version 2.0 (the
 *  * "License"); you may not use this file except in compliance
 *  * with the License.  You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing,
 *  * software distributed under the License is distributed on an
 *  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  * KIND, either express or implied.  See the License for the
 *  * specific language governing permissions and limitations
 *  * under the License.
 *
 */

package de.uniulm.omi.monitoring;

import de.uniulm.omi.monitoring.cli.CliOptions;
import de.uniulm.omi.monitoring.metric.impl.ApplicationMetric;
import de.uniulm.omi.monitoring.metric.impl.Metric;
import de.uniulm.omi.monitoring.probes.impl.CpuUsageProbe;
import de.uniulm.omi.monitoring.probes.impl.MemoryUsageProbe;
import de.uniulm.omi.monitoring.probes.impl.scheduler.ProbeScheduler;
import de.uniulm.omi.monitoring.reporting.api.ReportingInterface;
import de.uniulm.omi.monitoring.reporting.impl.KairosDb;
import de.uniulm.omi.monitoring.reporting.impl.Queue;
import de.uniulm.omi.monitoring.server.impl.MetricRequestParser;
import de.uniulm.omi.monitoring.server.impl.Server;
import org.apache.commons.cli.ParseException;

public class MonitoringAgent {

    public static void main(String[] args) throws ParseException {

        CliOptions.setArguments(args);

        //metric queue
        ReportingInterface<Metric> metricQueue = new Queue<>(1, new KairosDb(CliOptions.getKairosServer(), CliOptions.getKairosPort()));

        //create a new server
        Server<? extends Metric> server = new Server<>(9002, metricQueue, new MetricRequestParser(), 1);

        //run the server
        Thread thread = new Thread(server);
        thread.start();

        //create a scheduler
        ProbeScheduler scheduler = new ProbeScheduler(1, metricQueue);
        scheduler.registerProbe(new CpuUsageProbe());
        scheduler.registerProbe(new MemoryUsageProbe());
    }
}
