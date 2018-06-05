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

package de.uniulm.omi.cloudiator.visor.sensors;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniulm.omi.cloudiator.visor.exceptions.MeasurementNotAvailableException;
import de.uniulm.omi.cloudiator.visor.exceptions.SensorInitializationException;
import de.uniulm.omi.cloudiator.visor.monitoring.AbstractSensor;
import de.uniulm.omi.cloudiator.visor.monitoring.Measurement;
import de.uniulm.omi.cloudiator.visor.monitoring.MonitorContext;
import de.uniulm.omi.cloudiator.visor.monitoring.SensorConfiguration;
import de.uniulm.omi.cloudiator.visor.sensors.restUtil.PerformanceResponse;
import de.uniulm.omi.cloudiator.visor.sensors.restUtil.RestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A probe for measuring the CPU usage in % on the given machine.
 */
public class ProductsResponseTimeSensor extends AbstractSensor {

    private final static Logger LOG = LoggerFactory.getLogger(RestUtil.class);
    private final String URL_CONFIG = "cp.productsMetrics.url";
    private URL url;
    private ObjectMapper objectMapper;


    @Override
    @SuppressWarnings("Duplicates")
    protected Measurement measureSingle() throws MeasurementNotAvailableException {
        double responseTime = 0;
        ResponseEntity<byte[]> response = RestUtil.callRestUrl(url.toString(), HttpMethod.GET, null);
        /*
         * Parse the response
         */
        if (objectMapper == null)  {
            objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                PerformanceResponse metrics = objectMapper.readValue(response.getBody(),
                        objectMapper.getTypeFactory().constructType(PerformanceResponse.class));
                responseTime = metrics.getAvgResponseTime();
            } catch (IOException e) {
                LOG.error("Could not retrieve report from service instance", e);
            } finally {
                return measurementBuilder(Double.class).now().value(responseTime).build();
            }
        }

        return measurementBuilder(Double.class).now().value(0).build();

    }

    @Override
    protected void initialize(MonitorContext monitorContext,
                              SensorConfiguration sensorConfiguration) throws SensorInitializationException {
        super.initialize(monitorContext, sensorConfiguration);
        try {
            this.url = new URL(sensorConfiguration.getValue(URL_CONFIG).orElse("http://172.16.12.15:9700/cpmetrics/seconds/10000"));
        } catch (MalformedURLException e) {
            throw new SensorInitializationException(
                    "Url provided is not correct.", e);
        }
    }
}
