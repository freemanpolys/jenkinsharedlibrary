def call(Map<String, Object> config) {
    pipeline {
      agent any
      stages {
        stage('Even Stage') {
          steps {
            echo "The build number is even"
          }
        }
      }
    }
}

def getConfig(Map<String, Object> config, String key, defaultValue) {
    def value = config[key]
    return (value != null) ? value : defaultValue
}