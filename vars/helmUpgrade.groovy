
def call(String namespaceParameter, Map args=[:]){
    call namespace: namespaceParameter, set: args
}

def call(Map params){

    def namespace = null
    def args = [:]

    if (params != null){
        namespace = params.namespace?.trim()
        args = params.set
    }

    if (!namespace){
        currentBuild.result = 'FAILURE'
        error "Undefined namespace: ${params.namespace}"
    }
    def exposedArgs = ' '
    if (args && args.size() > 0){
        exposedArgs = ''
        args.each { k, v ->
            if (v instanceof CharSequence){
                exposedArgs += ' --set-string '
            } else {
                exposedArgs += ' --set '
            }
            exposedArgs += "\"$k=$v\""
        }
        exposedArgs += ' '
    }
    def releaseName = "${imageName()}-${namespace}"
    def stdErr = sh(returnStdout: true, script: 'mktemp /tmp/helm_upgrade_stderr.XXXXXX').trim()
    try{
        def status = sh(returnStatus: true, script: "helm upgrade -f \"chart/values-${namespace}.yaml\" --install --force --wait --namespace \"${namespace}\"${exposedArgs}\"${releaseName}\" chart/ 2>${stdErr}")
        if (status != 0){
            def errorText = sh(returnStdout: true, script: "cat ${stdErr}").trim()
            if (errorText){
                echo "${errorText}"
                if (errorText.contains('UPGRADE FAILED')){
                    // 0 - is previous revision; https://github.com/helm/helm/issues/1796#issuecomment-311385728
                    def rollbackStatus = sh returnStatus: true, script: "helm rollback --wait \"${releaseName}\" 0"
                    if (rollbackStatus != 0){
                        errorText += "\nRollback failed. Exit code ${rollbackStatus}"
                    }
                } else {
                    echo "It seems not a upgrading failure. If it's the failure, you can do 'helm rollback --wait \"${releaseName}\" 0'"
                }
                currentBuild.result = 'FAILURE'
                error "Helm upgrade exit code ${status}\n${errorText}"
            }
            currentBuild.result = 'FAILURE'
            error "Helm upgrade exit code ${status}"
        }
    }finally{
        sh  returnStdout: true, script: "rm ${stdErr}"
    }
}