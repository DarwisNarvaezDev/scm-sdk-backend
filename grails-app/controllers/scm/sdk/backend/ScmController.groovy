package scm.sdk.backend

class ScmController {

    /*
    TODO:
     Make a git repo with this
     Create a temp, download the data, read it, deliver it to the frontend and then destroy de file and the folder
     Properly work with defaults if the envs are not set
     Work with private repos
    * */

    ScmService scmService
    YamlService yamlService

    def index(){
        serveScmData()
    }

    def serveScmData() {
        println "Serving data... ${yamlService.parseIntoJson()}"
        render yamlService.parseIntoJson()
    }
}
