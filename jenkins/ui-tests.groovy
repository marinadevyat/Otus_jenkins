timeout(60) {
	node("maven") {
		def testContainerName = "ui_tests_$BUILD_NUMBER"
		try {
			wrap([$class: 'BuildUser']) {
			currentBuild.description = "User: $BUILD_USER"
			}	
		
			stage("Run UI tests ${jobDescription}") {
				sh "docker run --rm --network=host -e BROWSER_NAME=$BROWSER_NAME --name $testContainerName -v $pwd/allure-results:/otus/jenkins/allure-results -t ui_tests"
			}	
			stage("Publish allure report") {
				allure([
					disabled: true,
					results: ["$pwd/allure-results"]
				])
		} finally {
			sh "docker run $testContainerName"
		}
		
	}
}





