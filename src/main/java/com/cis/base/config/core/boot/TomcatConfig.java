package com.cis.base.config.core.boot;

import org.apache.catalina.connector.Connector;
import org.apache.commons.io.FileUtils;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * @author YSivlay
 */
@Configuration
public class TomcatConfig {

    @Bean
    public TomcatServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.setContextPath("/psdm-provider");
        tomcat.addAdditionalTomcatConnectors(createHttpConnector());
        return tomcat;
    }

    private Connector createHttpConnector() {
        Connector connector = new Connector(Http11NioProtocol.class.getName());
        try {
            File file = getFile(new ClassPathResource("/keystore.jks"));
            connector.setScheme("http");
            connector.setSecure(true);
            connector.setPort(8443);
        } catch (IOException ex) {
            throw new IllegalStateException("can't access keystore: [" + "keystore" + "] or truststore: [" + "keystore" + "]", ex);
        }

        return connector;
    }

    public File getFile(Resource resource) throws IOException {
        try {
            return resource.getFile();
        } catch (IOException e) {
        }

        try {
            URL url = resource.getURL();
            File targetFile = new File(Objects.requireNonNull(resource.getFilename()));
            long len = resource.contentLength();
            if (!targetFile.exists() || targetFile.length() != len) { // Only
                FileUtils.copyURLToFile(url, targetFile);
            }
            return targetFile;
        } catch (IOException e) {
            throw new IOException("Can not obtain a file for resource: " + resource, e);
        }

    }
}
