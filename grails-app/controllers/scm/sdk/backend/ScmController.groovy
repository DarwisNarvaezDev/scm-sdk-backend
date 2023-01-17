package scm.sdk.backend

class ScmController {

    /*
    TODO:
     Work with private repos
    * */

    ScmService scmService
    YamlService yamlService

    def index(){
        serveScmData()
    }

    def serveScmData() {
        // Get from user if we destroy the folder and its files after the SCM
        println "destroy scm folder? ${params.destroyFolder}"
        // Clone the repo into path
        println "Get: ${scmService.gitTargetFolder}/${yamlService.getFilename()}"
        def result = scmService.cloneRepositoryToTemp()
        if( result ){
            if( 'Success' != result.results ){
                if( result.results.contains("Authentication") ){
                    render "You are trying to access a private repo, please provide the property 'git.authToken'"
                }else{
                    render result.results
                }
            }else{
                render yamlService.parseIntoJson()
            }
        }else{
            flash.error = "An error occurred in controller"
        }
    }
}
