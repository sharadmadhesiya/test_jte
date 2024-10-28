void call(app_env) {
    node {
        stage("Test: Static Code Analysis") {
            println "Test iOS library"
            println(pipelineConfig)
            println("#################### STARTED iOS DEPLOYMENT ####################")

            withCredentials([string(credentialsId: 'APPSTORE_KEY_ID', variable: 'APPSTORE_KEY_ID')]) {
                sh """
                
                if [ -d "jte_pipeline" ]; then
                    cd jte_pipeline && git pull
                else
                    git clone https://github.com/sharadmadhesiya/jte_pipeline.git
                    cd jte_pipeline
                fi

                # Display current user and check key ID
                ls -lh
                echo \$APPSTORE_KEY_ID
                echo "Checking bundle id from shell"
                echo \${pipelineConfig.APPSTORE_BUNDLE_ID}
                whoami

                # Check if rbenv is installed, if not, install it
                if ! command -v rbenv &> /dev/null; then
                    echo "rbenv could not be found, installing..."
                    brew install rbenv
                fi
                eval "$(rbenv init -)"

                # Install Ruby version and set it up
                rbenv install 3.0.0 --skip-existing
                rbenv global 3.0.0
                eval "$(rbenv init -)"

                # Set up GEM_HOME, GEM_PATH, and PATH
                export GEM_HOME=$(rbenv root)/versions/3.0.0
                export GEM_PATH=$GEM_HOME
                export PATH=$GEM_HOME/bin:$PATH

                # Install the specified version of Bundler and bundle install
                gem install bundler -v 2.4.22 --user-install
                cd jte_pipeline  # Navigate to directory with Gemfile, if not already there
                bundle install

                # Set up and verify Xcode configuration
                sudo xcode-select --switch /Applications/Xcode.app/Contents/Developer
                xcodebuild -version
                """
            }
        }
    }
}
