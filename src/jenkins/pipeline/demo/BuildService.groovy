/**
 * Service used to build artifacts
 */
package jenkins.pipeline.demo;

import java.io.Serializable;

public class BuildService implements Serializable {

	def steps;
	def currentBuild;

	public BuildService(steps,currentBuild){
		this.steps = steps;
		this.currentBuild = currentBuild;
	}

	def build() {
		this.steps.echo "Building source code...";
	}
}


