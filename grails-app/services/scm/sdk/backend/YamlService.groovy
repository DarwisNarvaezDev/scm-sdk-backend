package scm.sdk.backend

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import grails.gorm.transactions.Transactional
import models.YamlModel
import org.springframework.beans.factory.annotation.Value

// YAML Files service to perform basic actions

@Transactional
class YamlService {

    @Value('${files.yamlFileName}')
    String filename

    def mapper
    ScmService scmService

    def parseIntoJson() {
        def defaultFilename = 'components.yaml'
        if(filename){
            return readFile(new File(filename))
        }else{
            return readFile(new File(defaultFilename))
        }
    }

    private readFile(File yamlFile) {
        if (scmService.gitTargetFolderFromConfig) {
            try {
                mapper = new ObjectMapper(new YAMLFactory())
                mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
                YamlModel model = mapper.readValue(new File(scmService.gitTargetFolderFromConfig + '/' + yamlFile), YamlModel.class)
                return model
            } catch (Exception e) {
                throw e
            }
        }else{
            // TODO
        }
    }

}
