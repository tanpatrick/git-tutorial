/**
 * Service used to retrieve the changes push to git
 */
package jenkins.pipeline.demo;

import java.io.Serializable;

public class ChangeService implements Serializable {

	def steps;
	def currentBuild;

	public ChangeService(steps,currentBuild) {
		this.steps = steps;
		this.currentBuild = currentBuild;
	}
}