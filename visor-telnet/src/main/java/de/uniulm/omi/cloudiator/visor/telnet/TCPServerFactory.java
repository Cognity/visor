/*
 * Copyright (c) 2014-2015 University of Ulm
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package de.uniulm.omi.cloudiator.visor.telnet;

import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.visor.execution.ExecutionService;
import de.uniulm.omi.cloudiator.visor.monitoring.Metric;
import de.uniulm.omi.cloudiator.visor.monitoring.MonitorContext;
import de.uniulm.omi.cloudiator.visor.reporting.QueuedReporting;
import de.uniulm.omi.cloudiator.visor.reporting.ReportingInterface;
import de.uniulm.omi.cloudiator.visor.server.AbstractServerFactory;
import de.uniulm.omi.cloudiator.visor.server.Server;

import java.io.IOException;

/**
 * Created by daniel on 23.10.15.
 */
public class TCPServerFactory extends AbstractServerFactory {

    private final ExecutionService executionService;
    private final ReportingInterface<Metric> metricReporting;

    @Inject public TCPServerFactory(ExecutionService executionService,
        @QueuedReporting ReportingInterface<Metric> metricReporting) {
        this.executionService = executionService;
        this.metricReporting = metricReporting;
    }

    @Override public Server createServer(String uuid, int port, MonitorContext monitorContext)
        throws IOException {
        return new TCPServer(executionService, metricReporting,
            new StringToMetricParser(monitorContext), port, uuid);
    }
}