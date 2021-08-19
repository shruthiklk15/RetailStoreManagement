node {

	def mvnHome
	stage('Prepare') {
		git url: 'git@github.com:HarshaPuthussery/product-app.git', branch: 'develop'
		mvnHome = tool 'mvn'
	}

	stage('Build') {
		if (isUnix()) {		
			sh "'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore clean package"
		} else {
			bat(/"${mvnHome}\bin\mvn" -Dmaven.test.failure.ignore clean package/)
		}
	}

	stage('Unit Test') {
		junit '**/target/surefire-reports/TEST-*.xml'
		archive 'target/*.jar'
	}

	stage('Integration Test') {
		if (isUnix()) {
			sh "'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore clean verify"
		} else {
			bat(/"${mvnHome}\bin\mvn" -Dmaven.test.failure.ignore clean verify/)
		}
	}


	stage('Deploy') {
		bat 'curl -u jenkins:jenkins -F filedata=target/**.war "http://localhost:5050/manager/text/deploy?path=/ibmdevops&update=true"'
	}

	stage("Smoke Test"){
		bat "curl --retry-delay 10 --retry 5 http://localhost:5050/ibmdevops/api/v1/products"
	}
	
}