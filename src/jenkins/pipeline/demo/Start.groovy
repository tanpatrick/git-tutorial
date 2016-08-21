@Library("demo");

def buildService = new jenkins.pipeline.demo.BuildService(steps,currentBuild);
def version = "1.0.${currentBuild.number}";
def modifiedDirs = buildService.retrieveModifiedDirs();

def changeService = new jenkins.pipeline.demo.ChangeService(steps,currentBuild);
def deployService = new jenkins.pipeline.demo.DeployService(steps,currentBuild,changeService);

stage('Build') {
	echo 'Started build stage...';
	buildService.build();
}

stage('Deploy') {
	echo 'Started deploy stage...';
	deployService.deploy();
}