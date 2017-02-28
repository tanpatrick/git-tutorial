@Library("demo")

def version = "1.0.${currentBuild.number}";

def changeService = new jenkins.pipeline.demo.ChangeService(steps,currentBuild,env);

def buildService = new jenkins.pipeline.demo.BuildService(steps,currentBuild,changeService);
def deployService = new jenkins.pipeline.demo.DeployService(steps,currentBuild,changeService);

def modifiedDirs = changeService.retrieveModifiedDirs();

def deployConfig = [
	[
		env: "dev",
		stage: "DEV"
	],
	[
		env: "sit",
		stage: "SIT"
	],
	[
		env: "preprod",
		stage: "PREPROD"
	],
	[
		env: "prod",
		stage: "PROD"
	]
];

stage('Build') {
	buildService.build();
}

for(def dc : deployConfig){
	stage(dc.stage){
		deployService.deploy(dc);
	}
}