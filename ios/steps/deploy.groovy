void call(app_env) {
    node {
        stage("Test: Static Code Analysis") {
            println "Test iOS library"
            println(pipelineConfig)
            println("#################### STARTED iOS DEPLOYMENT ####################")

            withCredentials([
                string(credentialsId: 'APPSTORE_KEY_ID', variable: 'APPSTORE_KEY_ID'),
                string(credentialsId: 'KEYCHAINPASSWORD', variable: 'KEYCHAINPASSWORD'),
                string(credentialsId: 'APPSTORE_API_KEY_FILE', variable: 'APPSTORE_API_KEY_FILE'),
                string(credentialsId: 'GITAUTHORIZATION', variable: 'GITAUTHORIZATION'),
                string(credentialsId: 'TOKEN', variable: 'TOKEN'),
                string(credentialsId: 'APPSTORE_ISSUER_ID', variable: 'APPSTORE_ISSUER_ID'),
                string(credentialsId: 'MATCH_PASSWORD', variable: 'MATCH_PASSWORD'),
                string(credentialsId: 'GIT_USERNAME', variable: 'GIT_USERNAME')
            ]) {

                script {
                    sh """
                    # Environment variable setup
                    export APP_IDENTIFIER=${pipelineConfig?.dev?.APP_IDENTIFIER}
                    export REPO_URL=${pipelineConfig?.dev?.REPO_URL}
                    export GIT_HOST=${pipelineConfig?.dev?.GIT_HOST}
                    export BRANCH_NAME=${pipelineConfig?.dev?.BRANCH_NAME}
                    export PROJECT_PATH=${pipelineConfig?.dev?.PROJECT_PATH}
                    export CODE_SIGN_IDENTITY=${pipelineConfig?.dev?.CODE_SIGN_IDENTITY}
                    export PROFILE_NAME=${pipelineConfig?.dev?.PROFILE_NAME}
                    export SCHEME=${pipelineConfig?.dev?.SCHEME}
                    export APPSTORE_KEY_ID=\$APPSTORE_KEY_ID
                    export KEYCHAINPASSWORD=\$KEYCHAINPASSWORD
                    export APPSTORE_API_KEY_FILE=\$APPSTORE_API_KEY_FILE
                    export GITAUTHORIZATION=\$GITAUTHORIZATION
                    export MATCH_PASSWORD=\$MATCH_PASSWORD
                    export APPSTORE_ISSUER_ID=\$APPSTORE_ISSUER_ID
                    export TOKEN=\$TOKEN
                    
                    echo "App Store Key ID: \$APPSTORE_KEY_ID"

                    # Clone or update the repository
                    if [ -d "jte_pipeline" ]; then
                        cd jte_pipeline && git pull
                    else
                        git clone https://\$GIT_USERNAME:\$TOKEN@\$GIT_HOST/\$REPO_URL
                        //https://github.com/sharadmadhesiya/jte_pipeline.git
                        cd jte_pipeline
                    fi

                    # Display current user and App Store Key ID
                    ls -lh
                    echo "Current user: \$(whoami)"
                    echo "App Store Key ID: \$APPSTORE_KEY_ID"

                    # Check if rbenv is installed, if not, install it
                    if ! command -v rbenv &> /dev/null; then
                        echo "Installing rbenv..."
                        export PATH="/opt/homebrew/bin:\$PATH"
                        brew install rbenv || echo "rbenv installation failed."
                    fi
                    eval "\$(rbenv init -)"

                    # Set up Ruby environment
                    rbenv install 3.0.0 --skip-existing
                    rbenv global 3.0.0
                    eval "\$(rbenv init -)"

                    # Configure GEM paths
                    export GEM_HOME=\$(rbenv root)/versions/3.0.0
                    export GEM_PATH=\$GEM_HOME
                    export PATH=\$GEM_HOME/bin:\$PATH

                    # Install Bundler and run bundle install
                    gem install bundler -v 2.4.22 --user-install
                    bundle install

                    # Xcode configuration
                    sudo xcode-select --switch /Applications/Xcode.app/Contents/Developer
                    xcodebuild -version

                    # Prepare Fastlane API key
                    echo "\$APPSTORE_API_KEY_FILE" > "fastlane/AuthKey_file.p8"
                    ls fastlane
                    pwd
                    cat fastlane/AuthKey_file.p8

                    # Execute Fastlane command
                    bundle exec fastlane release_build --verbose
                    """
                }
            }
        }
    }
}
