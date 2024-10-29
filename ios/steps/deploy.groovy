void call(app_env) {
    node {
        stage("Test: Static Code Analysis") {
            println "Test iOS library"
            println(pipelineConfig)
            println("#################### STARTED iOS DEPLOYMENT ####################")

            withCredentials([string(credentialsId: 'APPSTORE_KEY_ID', variable: 'APPSTORE_KEY_ID'),
                             string(credentialsId: 'KEYCHAINPASSWORD', variable: 'KEYCHAINPASSWORD'),
                             string(credentialsId: 'APPSTORE_API_KEY_FILE', variable: 'APPSTORE_API_KEY_FILE'),
                             string(credentialsId: 'GITAUTHORIZATION', variable: 'GITAUTHORIZATION'),
                             string(credentialsId: 'TOKEN', variable: 'TOKEN'),
                             string(credentialsId: 'APPSTORE_ISSUER_ID', variable: 'APPSTORE_ISSUER_ID'),
                             string(credentialsId: 'MATCH_PASSWORD', variable: 'MATCH_PASSWORD')]) {

                script {
                    // Set environment variables in the shell
                    sh """
                    export APPSTORE_KEY_ID=\$APPSTORE_KEY_ID
                    export KEYCHAINPASSWORD=\$KEYCHAINPASSWORD
                    export APPSTORE_API_KEY_FILE=\$APPSTORE_API_KEY_FILE
                    export GITAUTHORIZATION=\$GITAUTHORIZATION
                    export MATCH_PASSWORD=\$MATCH_PASSWORD
                    export APPSTORE_ISSUER_ID=\$APPSTORE_ISSUER_ID
                    export TOKEN=\$TOKEN
                    
                    echo \$APPSTORE_KEY_ID
                    
                    if [ -d "jte_pipeline" ]; then
                        cd jte_pipeline && git pull
                    else
                        git clone https://github.com/sharadmadhesiya/jte_pipeline.git
                        cd jte_pipeline
                    fi

                    # Display current user and check key ID
                    ls -lh
                    echo "Current user: \$(whoami)"
                    echo "App Store Key ID: \$APPSTORE_KEY_ID"

                    # Check if rbenv is installed, if not, install it
                    if ! command -v rbenv &> /dev/null; then
                        echo "rbenv could not be found, installing..."
                        brew install rbenv
                    fi
                    eval "\$(rbenv init -)"

                    # Install Ruby version and set it up
                    rbenv install 3.0.0 --skip-existing
                    rbenv global 3.0.0
                    eval "\$(rbenv init -)"

                    # Set up GEM_HOME, GEM_PATH, and PATH
                    export GEM_HOME=\$(rbenv root)/versions/3.0.0
                    export GEM_PATH=\$GEM_HOME
                    export PATH=\$GEM_HOME/bin:\$PATH

                    # Install the specified version of Bundler and bundle install
                    gem install bundler -v 2.4.22 --user-install
                    bundle install

                    # Set up and verify Xcode configuration
                    sudo xcode-select --switch /Applications/Xcode.app/Contents/Developer
                    xcodebuild -version

                    echo \$APPSTORE_API_KEY_FILE >> "fastlane/AuthKey_file.p8"
                    ls fastlane
                    cat fastlane/AuthKey_file.p8
                    bundle exec fastlane release_build --verbose
                    """
                }
            }
        }
    }
}
