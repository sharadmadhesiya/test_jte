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
                whoami
                # Check if rbenv is installed, if not, install it
                if ! command -v rbenv &> /dev/null; then
                    echo "rbenv could not be found, installing..."
                    brew install rbenv
                    eval "$(rbenv init -)"
                fi

                # Install Ruby version and set it up
                rbenv install 3.0.0 --skip-existing
                rbenv global 3.0.0
                eval "$(rbenv init -)"

                # Set up GEM_HOME, GEM_PATH, and PATH
                export GEM_HOME=$(rbenv root)/versions/3.0.0
                export GEM_PATH=$GEM_HOME
                export PATH=$GEM_HOME/bin:$PATH

                # Install the specified version of bundler
                gem install bundler -v 2.4.22 --user-install

                
                bundle install

                xcode-select --switch /Applications/Xcode.app/Contents/Developer
                xcodebuild -version

                """
            }
        }

//         stage('Install Dependencies') {
//         sh '''
//         # Check if rbenv is installed, if not, install it
//         if ! command -v rbenv &> /dev/null; then
//             echo "rbenv could not be found, installing..."
//             brew install rbenv
//             eval "$(rbenv init -)"
//         fi

//         # Install Ruby version and set it up
//         rbenv install 3.0.0 --skip-existing
//         rbenv global 3.0.0
//         eval "$(rbenv init -)"

//         # Set up GEM_HOME, GEM_PATH, and PATH
//         export GEM_HOME=$(rbenv root)/versions/3.0.0
//         export GEM_PATH=$GEM_HOME
//         export PATH=$GEM_HOME/bin:$PATH

//         # Install the specified version of bundler
//         gem install bundler -v 2.4.22 --user-install

        
//         bundle install
//         '''
// }


        // stage('Configure Xcode') {
        //     sh '''
        //     sudo xcode-select --switch /Applications/Xcode.app/Contents/Developer
        //     xcodebuild -version
        //     '''
        // }
    }
}
