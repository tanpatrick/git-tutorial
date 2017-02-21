/**
 * Service used to retrieve the changes push to git
 */
package jenkins.pipeline.demo;

import java.io.Serializable;

import javax.swing.plaf.ListUI;

public class ChangeService implements Serializable {

	def steps;
	def currentBuild;
	def env;

	public ChangeService(steps,currentBuild,env) {
		this.steps = steps;
		this.currentBuild = currentBuild;
		this.env = env;
	}

	def retrievePrevModifiedDirs() {
		this.steps.echo 'Scanning modified dirs [previous builds]...';

		def jobName = this.env.JOB_NAME;
		def job = jenkins.model.Jenkins.instance.getItem(jobName);

		def previousBuilds = job.getBuilds();

		Set modifiedDirs = new HashSet();

		for(def prevBuild : previousBuilds) {
			if(prevBuild.getId() == currentBuild.getId()) {
				// skip current build
				continue;
			} else if("SUCCESS".equals(prevBuild.getResult().toString())) {
				// exit loop once build status is SUCCESS
				break;
			} else {
				this.steps.echo "Scanning build [${prevBuild.id}] ${prevBuild.result}";

				def buildModifiedDirs = scanChangeLogSets(prevBuild.getChangeSets());

				this.steps.echo "Build [${prevBuild.id}] modified dirs ${buildModifiedDirs}"

				modifiedDirs.addAll(buildModifiedDirs);
			}
		}

		return modifiedDirs;
	}
	
	def retrieveModifiedDirs() {
		this.steps.echo 'Scanning modified dirs [current build]...';

		Set modifiedDirs = new HashSet(scanChangeLogSets(currentBuild.getChangeSets()));
		
		this.steps.echo "Current build modified dirs ${modifiedDirs}";

		def prevBuild = currentBuild.getPreviousBuild();

		if(prevBuild != null) {
			this.steps.echo 'Checking previous build status...';
			this.steps.echo "Previous build status: [${prevBuild.result}]";

			if(!"SUCCESS".equals(prevBuild.getResult().toString())) {
				modifiedDirs.addAll(retrievePrevModifiedDirs());
			} else {
				this.steps.echo 'Skipped scanning of modified dirs [previous build]...';
			}
		}

		this.steps.echo "Modified dirs: ${modifiedDirs}";

		return modifiedDirs;
	}

	def scanChangeLogSets(changeLogSets) {
		Set<String> dirs = new HashSet<String>();

		for(def change : changeLogSets) {
			def entries  = change.items;

			for(def entry : entries) {
				for(def path : entry.affectedPaths) {
					def _index = path.indexOf("/");

					if(_index >= 0) {
						dirs.add(path.substring(0, _index));
					} else {
						dirs.add(path);
					}
				}
			}
		}

		return dirs;
	}
}