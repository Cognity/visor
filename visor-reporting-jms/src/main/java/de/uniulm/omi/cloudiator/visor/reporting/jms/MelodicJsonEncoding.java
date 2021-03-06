/*
 * Copyright (c) 2014-2018 University of Ulm
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

package de.uniulm.omi.cloudiator.visor.reporting.jms;

import static de.uniulm.omi.cloudiator.visor.config.ContextConstants.CLOUD_ID;
import static de.uniulm.omi.cloudiator.visor.config.ContextConstants.COMPONENT_ID;
import static de.uniulm.omi.cloudiator.visor.config.ContextConstants.VM_ID;

import de.uniulm.omi.cloudiator.visor.monitoring.Metric;

public class MelodicJsonEncoding implements JMSEncoding {

  private final JacksonJsonEncoding<MelodicDataFormat> jsonEncoding;

  MelodicJsonEncoding() {

    jsonEncoding = JacksonJsonEncoding.of(metric -> MelodicDataFormatBuilder.create()
        .setComponentName(metric.getTags().get(COMPONENT_ID))
        .setCloudName(metric.getTags().getOrDefault(CLOUD_ID, null))
        .setMetricValue(metric.getValue())
        .setTimestamp(metric.getTimestamp())
        .setVmName(metric.getTags().get(VM_ID))
        .build());
  }

  @Override
  public String encode(Metric metric) throws EncodingException {
    return jsonEncoding.encode(metric);
  }
}
