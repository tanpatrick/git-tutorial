/**
 * Service used to deploy the generated artifacts
 */
package jenkins.pipeline.demo;

import java.io.Serializable;

public class DeployService implements Serializable {

	def steps;
	def currentBuild;
	def changeService;

	public DeployService(steps,currentBuild,changeService){
		this.steps = steps;
		this.currentBuild = currentBuild;
		this.changeService = changeService;
	}

	def deploy(deployConfig){
//		this.steps.node("deploy") {
			this.steps.echo "Deploying changes [${deployConfig.env}]...";

			def modifiedDirs = changeService.retrieveModifiedDirs();

			this.steps.echo "Sleeping 10 seconds to allow server sufficient time to warm up";
			this.steps.sleep time: 10, unit: "SECONDS";
//		}
	}
}


