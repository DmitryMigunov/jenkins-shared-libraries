
def call(Map params){
    call(params.namespace as String, params.set as Map)
}

def call(String namespace, Map args=null){
    def namespaceLocal = namespace?.trim()
    if (!namespaceLocal){
        error "Undefined namespace: ${namespace}"
    }
    def exposedArgs = ' '
    if (args && args.size() > 0){
        exposedArgs = ''
        args.each { k, v ->
            exposedArgs += ' --set '
            exposedArgs += "\"$k=$v\""
        }
        exposedArgs += ' '
    }


    sh "helm upgrade -f \"chart/values-${namespaceLocal}.yaml\" --install --force --wait --namespace \"${namespaceLocal}\"${exposedArgs}\"${imageName()}-${namespaceLocal}\" chart/"

}
