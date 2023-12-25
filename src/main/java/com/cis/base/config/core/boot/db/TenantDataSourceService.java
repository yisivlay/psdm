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
package com.cis.base.config.core.boot.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class TenantDataSourceService {

    private static final Logger logger = LoggerFactory.getLogger(TenantDataSourceService.class);
    @Value("${cis.tenantdb.fixup:true}")
    private boolean enabled;

    // required=false is important here, because in
    // WebApplicationInitializerConfiguration for classic WAR there
    // is (intentionally) no MariaDB4j nor a DataSourceProperties
    // bean (because in the WAR we're using a DS from JNDI)
    private @Autowired(required = false)
    DataSourceProperties dsp;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TenantDataSourceService(@Qualifier("tenantDataSourceJndi") final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void fixUpTenantsSchemaServerPort() {
        if (!enabled) {
            logger.info("No schema_server_port UPDATE made to tenant_server_connections table of the psdm-tenants schema, because cis.tenantdb.fixup = false");
            return;
        }
        if (dsp == null) {
            logger.debug("No schema_server_port UPDATE made to tenant_server_connections table of the psdm-tenants schema (because neither MariaDB4j nor our own Spring Boot DataSourceConfiguration is used in a traditional WAR)");
            return;
        }
        int r = jdbcTemplate.update("UPDATE tenant_server_connections SET schema_server = ?, " +
                        "schema_server_port = ?, schema_username = ?, schema_password = ?",
                        dsp.getHost(), dsp.getPort(), dsp.getUsername(), dsp.getPassword());
        if (r == 0)
            logger.warn("UPDATE tenant_server_connections SET ... did not update ANY rows - something is probably wrong");
        else
            logger.info("Updated " + r + " rows in the tenant_server_connections table of the psdm-tenants schema to the real current host: " + dsp.getHost() + ", port: " + dsp.getPort());
    }

}
