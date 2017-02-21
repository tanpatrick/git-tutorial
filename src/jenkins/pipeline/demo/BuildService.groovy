/**
 * Service used to build artifacts
 */
package jenkins.pipeline.demo;

import java.io.Serializable;

public class BuildService implements Serializable {

	def steps;
	def currentBuild;
	def changeService;

	public BuildService(steps,currentBuild,changeService){
		this.steps = steps;
		this.currentBuild = currentBuild;
		this.changeService = changeService;
	}

	def build(modifiedDirs) {
		this.steps.echo "Building source code...";
		this.steps.echo "Modified dirs: ${modifiedDirs}";
		
	}
}


