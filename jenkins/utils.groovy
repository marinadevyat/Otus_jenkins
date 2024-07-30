def prepareConfig() {
	def yamlConfig = readYaml text: $YAML_CPNFIG

	yamlConfig.each(k. v -> System.setProperty(v))
}

def triggerJob(def jobName, def config) {
	return build job: $jobName, parameters: ["YAML_CONFIG": config]
}

this

