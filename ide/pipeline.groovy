pipeline {
  
  agent any
  triggers{ pollSCM('* * * * *') }
    stages {
        stage('Checkout') {
            steps {
                // Get some code from a GitHub repository
                git url:'https://github.com/nagkraju/jgsu-spring-petclinic.git', branch: 'main'
            }
        } 
        
        stage('Build') {
            steps {
                // Get some code from a GitHub repository
               // git url:'https://github.com/nagkraju/jgsu-spring-petclinic.git', branch: 'main'

                sh './mvnw clean package'

                // Run Maven on a Unix agent.
               // sh "mvn -Dmaven.test.failure.ignore=true clean package"

                // To run Maven on a Windows agent, use
                // bat "mvn -Dmaven.test.failure.ignore=true clean package"
            }

            post {
                // If Maven was able to run the tests, even if some of the test
                // failed, record the test results and archive the jar file.
                always {
                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'
               }
                changed {    
                emailext body: 'Please go to ${BUILD_URL} and verify the build', 
                subject: 'Job \'${JOB_NAME}\' (${BUILD_NUMBER}) is waiting for input',
                    to: 'nagkraju@yahoo.com'
               }
            }
        }
    }
}
