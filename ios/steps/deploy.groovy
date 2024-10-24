void call() {

    stage("Test: Static Code Analysis"){
        println "Test ios library"
            println("#################### STARTED S3  DEPLOYMENT ####################")
        println(env)
    if (env.APPSTORE_KEY_ID != null ) {
      withCredentials([[ credentialsId: "${env.APPSTORE_KEY_ID}"]]) {
          sh """
          echo ${env.APPSTORE_KEY_ID}
          """
      		
        }
    }
    else{
      println("Missing required Application environment parameters. Please check source code pipeline_config.groovy file variables: S3_BUCKET_NAME,AWS_CREDENTIALS_ID, DEPLOY_LIB_BRANCH_NAME, DEPLOY_TARGET_FOLDER")   
    }
    }
}
