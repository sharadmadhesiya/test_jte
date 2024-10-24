void call(app_env) {
    node {
        stage("Test: Static Code Analysis") {
            println "Test ios library"
            println(pipelineConfig)
            //println (pipelineConfig.APPSTORE_BUNDLE_ID)
            
            println("#################### STARTED S3 DEPLOYMENT ####################")

            // Print the APPSTORE_BUNDLE_ID in Groovy
            //println("App Store Bundle ID: ${APPSTORE_BUNDLE_ID}")

            // Ensure APPSTORE_KEY_ID is defined in Jenkins Credentials
            withCredentials([string(credentialsId: 'APPSTORE_KEY_ID', variable: 'APPSTORE_KEY_ID')]) {
                sh """
                echo \$APPSTORE_KEY_ID
                echo "Checking bundle id from shell"
                echo ${APPSTORE_BUNDLE_ID}
                """
            }
        }
    }
}
