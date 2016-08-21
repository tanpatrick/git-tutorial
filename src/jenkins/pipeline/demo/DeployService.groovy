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

	def deploy(){
		this.steps.echo 'Deploying changes...';
	}
}


