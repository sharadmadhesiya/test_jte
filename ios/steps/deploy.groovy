void call(app_env) {
    node {
        stage("Test: Static Code Analysis") {
            println "Test ios library"
            println(pipelineConfig)
            println("#################### STARTED ios DEPLOYMENT ####################")

            withCredentials([string(credentialsId: 'APPSTORE_KEY_ID', variable: 'APPSTORE_KEY_ID')]) {
                sh """
                if [ -d "jte_pipeline" ]; then
                  cd jte_pipeline && git pull
                else
                  git clone https://github.com/sharadmadhesiya/jte_pipeline.git
                fi

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

        stage('Configure Xcode') {
            sh '''
            sudo xcode-select --switch /Applications/Xcode.app/Contents/Developer
            xcodebuild -version
            '''
        }
    }
}
