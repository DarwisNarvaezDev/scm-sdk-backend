package scm.sdk.backend

import grails.converters.JSON

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
        // Clone the repo into path
        def result = scmService.cloneRepositoryToTemp()
        if( result ){
            if( 'Success' != result.results ) {
                if (result.results.contains("Authentication") || result.results.contains("authorized")) {
                    def message = "You are trying to access a private repo, please provide the property 'git.authToken'"
                    render(contentType: 'application.json', text:
                            (
                                    [message: message]
                            ) as JSON)
                } else if (result.results.contains("origin") || result.results.contains("remote")){
                    def message = "Did you provide an URL via configuration? prop: 'git.scmUrl'"
                    render(contentType: 'application.json', text:
                            (
                                    [message: message]
                            ) as JSON)
                }else{
                    println result.results
                    render(contentType: 'application.json', text: (result.results) as JSON)
                }
            }else {
                render(
                        contentType: 'application/json', text:
                        (
                                [
                                        yaml: yamlService.parseIntoJson(),
                                ]
                        ) as JSON
                )
            }
        }else{
            render(contentType: 'application.json', text:
                    (
                            [message: result.results]
                    ) as JSON)
        }
    }
}
