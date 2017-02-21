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

	def retrieveModifiedDirs() {
		this.steps.echo 'Scanning for modified dirs on the current build...';

		def changeLogSets = currentBuild.changeSets;

		Set<String> dirs = new HashSet<String>();

		for(def change : changeLogSets) {
			def entries  = change.items;

			for(def entry : entries) {
				for(def path : entry.affectedPaths) {
					dirs.add(path.substring(0, path.indexOf("/")));
				}
			}
		}

		this.steps.echo "Modified dirs: ${dirs}";

		return dirs;
	}
}