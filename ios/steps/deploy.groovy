void call(app_env) {
    node {
        stage('Checkout Code') {
            
                git branch: 'main', url: 'https://github.com/sharadmadhesiya/jte_pipeline.git'            
        }
        stage("Test: Static Code Analysis") {
            println "Test ios library"
            println(pipelineConfig)
            println(pipelineConfig.APPSTORE_BUNDLE_ID)
            //println (pipelineConfig.APPSTORE_BUNDLE_ID)
            
            println("#################### STARTED ios DEPLOYMENT ####################")

            // Print the APPSTORE_BUNDLE_ID in Groovy
            //println("App Store Bundle ID: ${APPSTORE_BUNDLE_ID}")

            // Ensure APPSTORE_KEY_ID is defined in Jenkins Credentials
            withCredentials([string(credentialsId: 'APPSTORE_KEY_ID', variable: 'APPSTORE_KEY_ID')]) {
                sh """
                
                
                cd jte_pipeline
                ls -lh
                echo \$APPSTORE_KEY_ID
                echo "Checking bundle id from shell"
                echo $pipelineConfig.APPSTORE_BUNDLE_ID
                
                """
            }
        }
        
        stage('Install Dependencies') {
            
                sh '''
                # Check if rbenv is installed, if not, install it
                if ! command -v rbenv &> /dev/null; then
                    echo "rbenv could not be found, installing..."
                    brew install rbenv
                    # Initialize rbenv
                    eval "$(rbenv init -)"
                fi

                # Install Ruby version and set it up
                rbenv install 3.0.0 --skip-existing
                rbenv global 3.0.0

                # Install bundler and Fastlane dependencies
                gem install bundler
                bundle install
                '''
            
        }
        stage('configure xcode'){
            sh '''
                sudo xcode-select --switch /Applications/Xcode.app/Contents/Developer
                xcodebuild -version
                '''
        }
    }
}
