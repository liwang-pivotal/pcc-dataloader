package io.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.pdx.PdxInstance;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;

@Configuration
@Profile("local")
public class LoaderLocalConfig {
	
	@Autowired 
	ClientCache clientCache;
	
	@Bean(name = "gemfireCache")
	public ClientCache clientCache() {
		ClientCacheFactory ccf = new ClientCacheFactory();
		ccf.addPoolLocator("localhost", 10334);
		ccf.setPdxSerializer(new ReflectionBasedAutoSerializer(".*"));
		ccf.setPdxReadSerialized(false);
		ccf.setPoolSubscriptionEnabled(true);

		ClientCache clientCache = ccf.create();

		return clientCache;
	}
	
	@Bean(name = "transactions")
	public Region<String, PdxInstance> transactionRegion(@Autowired ClientCache clientCache) {
		ClientRegionFactory<String, PdxInstance> transactionRegionFactory = clientCache.createClientRegionFactory(ClientRegionShortcut.PROXY);

		Region<String, PdxInstance> transactionRegion = transactionRegionFactory.create("transactions");

		return transactionRegion;
	}
	
}
