package com.cis.base;

import com.cis.base.config.core.boot.AbstractApplicationConfiguration;
import com.cis.base.config.core.boot.ExitUtil;
import com.cis.base.config.core.boot.TomcatConfig;
import com.cis.base.config.core.boot.db.DataSourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

public class ServerApplication {

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext ctx = SpringApplication.run(Configuration.class, args);
		ExitUtil.waitForKeyPressToCleanlyExit(ctx);
	}

	@Import({DataSourceConfig.class, TomcatConfig.class})
	private static class Configuration extends AbstractApplicationConfiguration {
	}

}
