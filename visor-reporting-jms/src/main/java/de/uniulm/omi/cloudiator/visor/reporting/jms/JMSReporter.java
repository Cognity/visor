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

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.visor.monitoring.Metric;
import de.uniulm.omi.cloudiator.visor.reporting.ReportingException;
import de.uniulm.omi.cloudiator.visor.reporting.ReportingInterface;
import de.uniulm.omi.cloudiator.visor.reporting.jms.JMSEncoding.EncodingException;
import java.util.Collection;
import javax.jms.JMSException;

public class JMSReporter implements ReportingInterface<Metric> {

  private final JMSProducer jmsProducer;

  @Inject
  public JMSReporter(JMSProducer jmsProducer) {
    checkNotNull(jmsProducer, "jmsProducer is null");
    this.jmsProducer = jmsProducer;
  }

  @Override
  public void report(Metric item) throws ReportingException {
    try {
      jmsProducer.sendMetric(item);
    } catch (JMSException | EncodingException e) {
      throw new ReportingException(e);
    }
  }

  @Override
  public void report(Collection<Metric> items) throws ReportingException {
    for (Metric item : items) {
      report(item);
    }
  }
}