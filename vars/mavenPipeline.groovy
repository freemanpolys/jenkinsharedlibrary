/*
This is the skleton of MNS maven project.
It defines a  directive 'mavenPipeline that takes Map as config parameters.
At the time of writing this lines the supported parameters ares:
    devsMail: emails to notify after the build. Default is ""
    qualityGateTime: the Sonar quality gate time value, defaults to 1
    qualityGateUnit: the Sonar qualitu gate unit, defaults to 'MINUTE'
    packageBranches: the branches to package on Nexus, defaults to 'develop'
    deployBranches: the branches to deploy with ansible, defaults to 'develop'
    profile: the profile to deploy, defaults to dev. This�defines also the name of the playbook file conventionnaly named as ${profile}-playbook.yml
*/
devsMail = ''
def  call(Map<String, Object> config) {
    pipeline {
    agent  any
    tools {
        maven "Maven"
    }

    stages {

        stage('check java') {
            steps {
                sh "java -version"
            }
        }

        stage('clean') {
        steps {
            sh 'mvn clean'
        }
        }

        stage('backend tests') {
        steps {
            sh 'mvn test'
        }
        }
        
        stage('packaging') {
            steps {
                script {
                    profile = getConfig(config, "profile", "dev")
                }
                sh "./mvnw verify -P$profile -DskipTests"
                archiveArtifacts artifacts: '**/target/*.war', fingerprint: true
            }
        }

    }
    }

}

def getConfig(Map<String, Object> config, String key, defaultValue) {
    def value = config[key]
    return (value != null) ? value : defaultValue
}