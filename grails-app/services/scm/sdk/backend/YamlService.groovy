package scm.sdk.backend

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import grails.gorm.transactions.Transactional
import models.YamlModel
import org.springframework.beans.factory.annotation.Value

import javax.annotation.PostConstruct

// YAML Files service to perform basic actions

@Transactional
class YamlService {

    @Value('${files.yamlFileName:null}')
    String filename

    def mapper
    ScmService scmService

    @PostConstruct
    def init() {
        if (null == filename || 'null' == filename) {
            filename = 'components.yaml'
        }
    }

    def parseIntoJson() {
        def filePath = "${scmService.getGitTargetFolder()}/${filename}"
        return readFile(new File(filePath))
    }

    private readFile(File yamlFile) {
        try {
            mapper = new ObjectMapper(new YAMLFactory())
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            YamlModel model = mapper.readValue(yamlFile, YamlModel.class)
            return model
        } catch (Exception e) {
            throw e
        }
    }

}
