package scm.sdk.backend

import grails.gorm.transactions.Transactional
import org.apache.commons.io.FileUtils
import org.eclipse.jgit.api.Git
import org.springframework.beans.factory.annotation.Value

@Transactional
class ScmService {

    @Value('${git.scmUrl:null}')
    String gitUrlFromConfig
    @Value('${git.targetFolder:null}')
    String gitTargetFolderFromConfig

    def checkFolder() {
        try{
            println "The url from config ${gitUrlFromConfig}"
            println "The target folder from config: ${gitTargetFolderFromConfig}"

            if( gitUrlFromConfig != null ){
                if( gitTargetFolderFromConfig != 'null' ){
                    def destFolder = new File(gitTargetFolderFromConfig)
                    if (destFolder.exists() && destFolder.isDirectory()) {
                        println "Working with folder: ${destFolder.toString()}"
                        return true
                    } else {
                        return false
                    }
                }else{
                    def currentUserHomePath = System.getProperty("user.home")
                    def tmpPath = 'tmp'
                    def defaultFolder = new File("${currentUserHomePath}/${tmpPath}")
                    if( defaultFolder.exists() && defaultFolder.isDirectory() ){
                        println "Working with a default folder: ${defaultFolder.toString()}"
                        return true
                    }else{
                        throw new Exception("Error with the default folder ${defaultFolder.toString()} is not a path")
                    }
                }
            }else{
                throw new Exception("Git SCM URI is not set in app configuration")
            }
        }catch(Exception e){
            println e
        }
    }

    def deleteFolder(String folder = null) {
        if( gitTargetFolderFromConfig ){
            def scmTargetFolder = new File(gitTargetFolderFromConfig)
            FileUtils.deleteDirectory(scmTargetFolder)
        }else{
            def scmTargetFolder = new File(folder)
            FileUtils.deleteDirectory(scmTargetFolder)
        }
    }

    def createFolderAndGitClone(String URI, File targetPath){
        Git git = Git.cloneRepository()
                .setURI(URI)
                .setDirectory(targetPath)
                .call();
    }

    @Transactional
    def cloneRepositoryToTemp(String URI, File targetPath, boolean deleteAtTheEnd) {
        if( gitUrlFromConfig && gitTargetFolderFromConfig ){
            def targetFile = new File(gitTargetFolderFromConfig)
            createFolderAndGitClone(gitUrlFromConfig, targetFile)
            if( deleteAtTheEnd ){
                deleteFolder(targetFile)
            }
        }else{
            createFolderAndGitClone(URI, targetPath)
            if( deleteAtTheEnd ){
                deleteFolder(targetPath)
            }
        }
    }

}
