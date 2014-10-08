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

package de.uniulm.omi.monitoring.probes.impl;

import com.sun.management.OperatingSystemMXBean;
import de.uniulm.omi.monitoring.probes.Interval;
import de.uniulm.omi.monitoring.probes.api.Probe;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

/**
 * A probe for measuring the CPU usage in % on the given machine.
 */
public class CpuUsageProbe implements Probe {


    @Override
    public Interval getInterval() {
        return new Interval(20, TimeUnit.SECONDS);
    }

    /**
     * The name of this metric.
     *
     * @return the metric name.
     */
    @Override
    public String getMetricName() {
        return "cpu_usage_percentage";
    }

    /**
     * Returns the value of this metric.
     * <p/>
     * Uses com.sun.management.OperatingSystemMXBean to measure
     * the cpu load of the system.
     *
     * @return the current cpu usage in percentage retrieved.
     * @throws MetricNotAvailableException if the measurement could not be executed and the bean return a negative value.
     */
    @Override
    public Double getMetricValue() throws MetricNotAvailableException {
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(
                OperatingSystemMXBean.class);

        double systemCpuLoad = osBean.getSystemCpuLoad();
        double systemCpuLoadPercentage = systemCpuLoad * 100;

        if (systemCpuLoad < 0) {
            throw new MetricNotAvailableException("Received negative value");
        }

        return systemCpuLoadPercentage;
    }
}
