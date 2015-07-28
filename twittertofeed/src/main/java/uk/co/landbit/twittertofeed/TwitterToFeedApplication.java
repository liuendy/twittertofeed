package uk.co.landbit.twittertofeed;

import java.util.Arrays;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.undertow.UndertowBuilderCustomizer;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.xnio.Options;

import com.zaxxer.hikari.HikariDataSource;

import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import uk.co.landbit.twittertofeed.config.ApplicationConfiguration;
import uk.co.landbit.twittertofeed.integration.IntegrationFlowConfiguration;


//@SpringBootApplication
@Configuration
@ComponentScan
@EnableAutoConfiguration(exclude = { })
@EnableWebMvc
@EnableSocial
@Import({ ApplicationConfiguration.class, IntegrationFlowConfiguration.class })
public class TwitterToFeedApplication {

	
	private final static Logger log = LoggerFactory.getLogger(TwitterToFeedApplication.class);
	
	private @Value("${spring.datasource.driver-class-name}") String driverClassName;
	private @Value("${spring.datasource.username:}") String username;
	private @Value("${spring.datasource.password:}") String password;
	private @Value("${spring.datasource.validation-query:}") String validationQuery;
	private @Value("${spring.datasource.url}") String url;
	private @Value("${spring.datasource.custom-hikari-minimun-idle:8}") int minimumIdle;
	private @Value("${spring.datasource.custom-hikari-max-pool-size:32}") int maximumPoolSize;
	private @Value("${spring.datasource.custom-hikari-connection-timeout:30000}") long connectionTimeout;

	public static void main(String[] args) throws Exception {
		ApplicationContext ctx = SpringApplication.run(TwitterToFeedApplication.class, args);
		logActiveProfiles(ctx);
		logBeans(ctx);
	}


	@Bean
	EhCacheCacheManager ehCacheCacheManager() {
		return new EhCacheCacheManager(ehCacheManagerFactoryBean().getObject());
	}

	@Bean
	EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
		EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
		ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
		return ehCacheManagerFactoryBean;
	}

	@Bean
	public UndertowEmbeddedServletContainerFactory embeddedServletContainerFactory() {
		UndertowEmbeddedServletContainerFactory factory = new UndertowEmbeddedServletContainerFactory();
		factory.addBuilderCustomizers(new UndertowBuilderCustomizer() {

			@Override
			public void customize(Undertow.Builder builder) {
				builder.setBufferSize(1024 * 16)
						.setIoThreads(Runtime.getRuntime().availableProcessors() * 2)
						.setSocketOption(Options.BACKLOG, 10000)
						.setServerOption(UndertowOptions.ALWAYS_SET_KEEP_ALIVE, false)
						.setServerOption(UndertowOptions.ALWAYS_SET_DATE, true)
						.setWorkerThreads(64);
			}

		});
		return factory;
	}

	@Bean
	public DataSource dataSource() {
		final HikariDataSource ds = new HikariDataSource();
		ds.setMinimumIdle(minimumIdle);
		ds.setMaximumPoolSize(maximumPoolSize);
		ds.setConnectionTimeout(connectionTimeout);
		ds.setDriverClassName(driverClassName);
		ds.setJdbcUrl(url);
		ds.setUsername(username);
		ds.setPassword(password);
		ds.setConnectionTestQuery(validationQuery);

		return ds;
	}

	
	private static void logActiveProfiles(ApplicationContext ctx) {
		StringBuilder sb = new StringBuilder("\n    Active profiles:\n");
		String[] activeProfiles = ctx.getEnvironment().getActiveProfiles();
		if (activeProfiles.length == 0) {
			sb.append("        No active profiles.\n");
		} else {
			for (String profile : activeProfiles) {
				sb.append("        " + profile + "\n");
			}
		}
		log.info(sb.toString());
	}

	private static void logBeans(ApplicationContext ctx) {
		StringBuilder sb = new StringBuilder("\n    Application beans:\n");
		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (String beanName : beanNames) {
			sb.append("        " + beanName + "\n");
		}
		log.info(sb.toString());
	}
}
