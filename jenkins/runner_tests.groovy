timeout(60) {
        node("maven") {
                wrap([$class: 'BuildUser']) {
                        currentBuild.description = "User: $BUILD_USER"

                }
                stage("Checkout") {
                        checkout git-ui-tests
                }
                def jobs = [:]
                env.TEST_TYPE.each(k, v -> {
                        jobs["$v"] = node("maven") {
                                stage("Running test $v")
                                jobs ["$v"] = "$v"
                        }
                })
                parallel jobs

                stage("Publish allure report") {
                        sh "mkdir -p ./allure-results"
                        dir("allure-results") {
                                jobs.each(k. v -> {
                                        copyArtifacts projectName: $v, selector: specific(v.getBuildNumber())
                        })

                        }
                        allure([
                                disabled: true,
                                results: ["$pwd/allure-results"]
                        ])
                }
        }

}

