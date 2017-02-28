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

	def deploy(modifiedDirs){
		this.steps.echo 'Deploying changes...';
		this.steps.echo "Modified dirs: ${modifiedDirs}";
		
		this.steps.echo "Sleeping 2 minutes to allow server sufficient time to warm up";
		this.steps.sleep time: 2, unit: "MINUTES";
	}
}


