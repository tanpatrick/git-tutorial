@Library("demo")

def version = "1.0.${currentBuild.number}";

def changeService = new jenkins.pipeline.demo.ChangeService(steps,currentBuild);

def buildService = new jenkins.pipeline.demo.BuildService(steps,currentBuild,changeService);
def deployService = new jenkins.pipeline.demo.DeployService(steps,currentBuild,changeService);

stage('Build') {
	echo 'Started build stage...';
	buildService.build();
}

stage('Deploy') {
	echo 'Started deploy stage...';
	deployService.deploy();
}