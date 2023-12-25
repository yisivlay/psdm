/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.cis.base.config.core.boot;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class JdbcDriverConfig {

    private final static String DRIVER_CLASS_PROPERTY_NAME = "DRIVER_CLASS_NAME";
    private final static String PROTOCOL_PROPERTY_NAME = "PROTOCOL";
    private final static String SUB_PROTOCOL_PROPERTY_NAME = "SUB_PROTOCOL";
    private final static String PORT_PROPERTY_NAME = "PORT";
    @Autowired
    ApplicationContext context;
    private String driverClassName;
    private String protocol;
    private String subProtocol;
    private Integer port;

    @PostConstruct
    protected void init() {
        Environment environment = context.getEnvironment();
        driverClassName = environment.getProperty(DRIVER_CLASS_PROPERTY_NAME);
        protocol = environment.getProperty(PROTOCOL_PROPERTY_NAME);
        subProtocol = environment.getProperty(SUB_PROTOCOL_PROPERTY_NAME);
        port = Integer.parseInt(Objects.requireNonNull(environment.getProperty(PORT_PROPERTY_NAME)));
    }

    public String getDriverClassName() {
        return this.driverClassName;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public String getSubProtocol() {
        return this.subProtocol;
    }

    public Integer getPort() {
        return this.port;
    }

    public String constructProtocol(String schemaServer, String schemaServerPort, String schemaName) {
        return protocol + ":" + subProtocol + "://" + schemaServer + ':' + schemaServerPort + '/' + schemaName;
    }

}
