
package com.mossle.bpm.support;

import java.io.IOException;
import java.util.List;
import java.util.zip.ZipInputStream;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ContextResource;
import org.springframework.core.io.Resource;

public class AutoDeployer {
    private Logger logger = LoggerFactory.getLogger(AutoDeployer.class);

	private ProcessEngine processEngine;
	protected Resource[] deploymentResources = new Resource[0];

	@PostConstruct
	public void init() {
		if (deploymentResources == null || deploymentResources.length == 0) {
			return;
		}
		RepositoryService repositoryService = processEngine.getRepositoryService();

		for (Resource resource : deploymentResources) {
			String resourceName = null;

			if (resource instanceof ContextResource) {
				resourceName = ((ContextResource) resource).getPathWithinContext();

			} else if (resource instanceof ByteArrayResource) {
				resourceName = resource.getDescription();

			} else {
				try {
					resourceName = resource.getFile().getAbsolutePath();
				} catch (IOException e) {
					resourceName = resource.getFilename();
				}
			}

			try {
				DeploymentBuilder deploymentBuilder = repositoryService
					.createDeployment()
					.enableDuplicateFiltering()
					.name(resourceName);
				if ( resourceName.endsWith(".bar")
					|| resourceName.endsWith(".zip")
					|| resourceName.endsWith(".jar") ) {
					deploymentBuilder.addZipInputStream(new ZipInputStream(resource.getInputStream()));
				} else {
					deploymentBuilder.addInputStream(resourceName, resource.getInputStream());
				}
				deploymentBuilder.deploy();
				logger.info("auto deploy : {}", resourceName);
			} catch (IOException e) {
				throw new ActivitiException("couldn't auto deploy resource '" + resource + "': " + e.getMessage(), e);
			}
		}

	}

	public boolean checkDeploymentUpToDate(String resourceName, long lastModified) {
		List<Deployment> deployments = processEngine.getRepositoryService()
			.createDeploymentQuery()
			.deploymentName(resourceName)
			.orderByDeploymenTime()
			.desc()
			.list();
		if (deployments.isEmpty()) {
			return false;
		}
		Deployment deployment = deployments.get(0);
		return deployment.getDeploymentTime().getTime() > lastModified;
	}

	public void setProcessEngine(ProcessEngine processEngine) {
		this.processEngine = processEngine;
	}

	public void setDeploymentResources(Resource[] deploymentResources) {
		this.deploymentResources = deploymentResources;
	}
}
