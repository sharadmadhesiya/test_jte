void call() {

    stage("Test: Static Code Analysis") {
        println "Test ios library"
        println("#################### STARTED S3 DEPLOYMENT ####################")

        // Ensure APPSTORE_KEY_ID is defined in Jenkins Credentials
        withCredentials([string(credentialsId: 'APPSTORE_KEY_ID', variable: 'APPSTORE_KEY_ID')]) {
            sh """
            echo ${APPSTORE_KEY_ID}
            """
        }
    }
}
