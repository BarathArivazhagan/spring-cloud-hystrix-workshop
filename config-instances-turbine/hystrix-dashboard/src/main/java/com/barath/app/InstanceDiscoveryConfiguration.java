package com.barath.app;

import org.springframework.cloud.netflix.turbine.TurbineProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.turbine.discovery.ConfigPropertyBasedDiscovery;
import com.netflix.turbine.discovery.InstanceDiscovery;

@Configuration
public class InstanceDiscoveryConfiguration {
	
	private final TurbineProperties turbineProperties;

	public InstanceDiscoveryConfiguration(TurbineProperties turbineProperties) {
		super();
		this.turbineProperties = turbineProperties;
	}
	
	@Bean
	public InstanceDiscovery configBasedInstanceDiscovery() {
		return new ConfigPropertyBasedDiscovery();
	}
	
	

}
