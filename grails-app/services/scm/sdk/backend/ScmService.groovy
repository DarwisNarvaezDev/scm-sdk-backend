package scm.sdk.backend

import grails.gorm.transactions.Transactional
import org.apache.commons.io.FileUtils
import org.eclipse.jgit.api.Git
import org.springframework.beans.factory.annotation.Value

@Transactional
class ScmService {

    @Value('${git.scmUrl}')
    def gitUrlFromConfig
    @Value('${git.targetFolder}')
    def gitTargetFolderFromConfig

    def checkFolder() {
        try{
            if( gitUrlFromConfig ){
                if( gitTargetFolderFromConfig ){
                    def destFolder = new File(gitTargetFolderFromConfig)
                    if (destFolder.exists() && destFolder.isDirectory()) {
                        return true
                    } else {
                        return false
                    }
                }else{
                    throw new MissingPropertyException('git.targetFolder')
                }
            }else{
                throw new MissingPropertyException("git.scmUrl")
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

    def gitClone(String URI, File targetPath){
        Git git = Git.cloneRepository()
                .setURI(URI)
                .setDirectory(targetPath)
                .call();
    }

    @Transactional
    def cloneRepositoryToTemp(String URI, File targetPath, boolean deleteAtTheEnd) {
        if( gitUrlFromConfig && gitTargetFolderFromConfig ){
            def targetFile = new File(gitTargetFolderFromConfig)
            gitClone(gitUrlFromConfig, targetFile)
            if( deleteAtTheEnd ){
                deleteFolder(targetFile)
            }
        }else{
            gitClone(URI, targetPath)
            if( deleteAtTheEnd ){
                deleteFolder(targetPath)
            }
        }
    }

}
