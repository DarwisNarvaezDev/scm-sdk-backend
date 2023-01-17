package scm.sdk.backend

import grails.gorm.transactions.Transactional
import org.apache.commons.io.FileUtils
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import org.springframework.beans.factory.annotation.Value

import javax.annotation.PostConstruct

@Transactional
class ScmService {

    @Value('${git.scmUrl:null}')
    String gitUri
    @Value('${git.targetFolder:null}')
    String gitTargetFolder
    @Value('${git.authToken:null}')
    String gitAuthToken

    @PostConstruct
    def init() {
        if (gitUri == null) {
            throw new MissingPropertyException("The URI to the Git SCM repo must be set")
        }
        if (gitTargetFolder == 'null') {
            def currentUserHomePath = System.getProperty("user.home")
            def tmpPath = 'tmp'
            def defaultTargetFolder = 'destroy'
            gitTargetFolder = "${currentUserHomePath}/${tmpPath}/${defaultTargetFolder}"
        }
    }

    def deleteFolder() {
        def scmTargetFolder = new File(gitTargetFolder)
        FileUtils.deleteDirectory(scmTargetFolder)
    }

    def createFolderAndGitClone(String URI, File targetPath) {
        def gitTokenEmpty = 'null' == gitAuthToken
        try{
            if( targetPath.exists() && targetPath.isDirectory() ){
                deleteFolder()
            }
            if( gitTokenEmpty ){
                return getGitRepo(URI, targetPath)
            }else{
                return getGitRepo(URI, targetPath, true)
            }
        }catch(Exception e){
            return [gitObject: null, results: e.toString()]
        }
    }

    def getGitRepo(String URI, File targetPath, boolean withToken = false){
        try{
            if( withToken ){
                Git git = Git.cloneRepository()
                        .setURI(URI)
                        .setDirectory(targetPath)
                        .setCredentialsProvider(new UsernamePasswordCredentialsProvider("token", gitAuthToken))
                        .call()
                return [gitObject: git, results: 'Success']
            }else{
                Git git = Git.cloneRepository()
                        .setURI(URI)
                        .setDirectory(targetPath)
                        .call()
                return [gitObject: git, results: 'Success']
            }
        }catch(Exception e){
            return [gitObject: null, results: e.toString()]
        }
    }

    def cloneRepositoryToTemp() {
        def targetFile = new File(gitTargetFolder)
        createFolderAndGitClone(gitUri, targetFile)
    }

}
