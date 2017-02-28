/**
 * Service used to retrieve the changes push to git
 */
package jenkins.pipeline.demo;

import java.io.Serializable;

public class ChangeService implements Serializable {

	def steps;
	def currentBuild;
	def env;

	public ChangeService(steps,currentBuild,env) {
		this.steps = steps;
		this.currentBuild = currentBuild;
		this.env = env;
	}

	def retrieveModifiedDirs() {
		this.steps.echo 'Scanning modified dirs [current build]...';

		Set modifiedDirs = new HashSet(scanChangeLogSets(currentBuild.getId(), currentBuild.getChangeSets()));

		this.steps.echo "Current build modified dirs ${modifiedDirs}";

		def prevBuild = currentBuild.getPreviousBuild();

		// build is in progress when build result is null.
		if(prevBuild != null && prevBuild.getResult() != null) {
			def buildStatus = prevBuild.getResult().toString();

			this.steps.echo 'Checking previous build status...';
			this.steps.echo "Previous build status: [${buildStatus}]";

			if("ABORTED".equals(buildStatus) || "FAILURE".equals(buildStatus)) {
				/**
				 * If previous build status is ABORTED or FAILURE, need to re-scan the previous builds for all modified dirs. 
				 * Ensure that modified files from previous failed builds are included on the latest build deployment.
				 * Re-scanned builds included are builds after the last successful build. 
				 */
				modifiedDirs.addAll(retrievePrevModifiedDirs());
			} else {
				this.steps.echo 'Skipped scanning of modified dirs [previous build]...';
			}
		} else if(prevBuild != null) {
			this.steps.echo "Skipped scanning of modified dirs [previous build - ${prevBuild.id}], status is still in progress.";
		}

		this.steps.echo "Merged modified dirs: ${modifiedDirs}";

		return modifiedDirs;
	}

	def retrievePrevModifiedDirs() {
		this.steps.echo 'Scanning modified dirs [previous builds]...';

		def jobName = this.env.JOB_NAME;
		def job = jenkins.model.Jenkins.instance.getItem(jobName);

		def previousBuilds = job.getBuilds();

		Set modifiedDirs = new HashSet();

		for(def prevBuild : previousBuilds) {
			def buildStatus = prevBuild.getResult();

			if(prevBuild.getId() == currentBuild.getId() || buildStatus == null) {
				// skip current build or if build is in progress
				continue;
			} else if("SUCCESS".equals(buildStatus.toString())) {
				// exit loop once build status is SUCCESS
				this.steps.echo "Scanning of previous builds modified dirs stopped at build [${prevBuild.id}].";
				break;
			} else if("ABORTED".equals(buildStatus.toString()) || "FAILURE".equals(buildStatus.toString())) {
				this.steps.echo "Scanning build [${prevBuild.id}] [${buildStatus}]";

				def buildModifiedDirs = scanChangeLogSets(prevBuild.getId(), prevBuild.getChangeSets());

				this.steps.echo "Build [${prevBuild.id}] modified dirs ${buildModifiedDirs}";

				modifiedDirs.addAll(buildModifiedDirs);
			}
		}

		return modifiedDirs;
	}

	def scanChangeLogSets(buildNo, changeLogSets) {
		this.steps.echo "Scanning build [${buildNo}] change logs...";
		
		Set<String> dirs = new HashSet<String>();

		for(def change : changeLogSets) {
			def entries  = change.items;

			for(def entry : entries) {
				this.steps.echo "${entry.commitId} by ${entry.author} on ${new Date(entry.timestamp)}: ${entry.msg}";
				
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