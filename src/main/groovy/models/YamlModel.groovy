package models

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(["metaClass"])
@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
class YamlModel {

    @JsonProperty(value = "components")
    List<Component> components

    YamlModel() {
    }

    YamlModel(List<Component> components) {
        this.components = components
    }

    List<Component> getComponents() {
        return components
    }

    void setComponents(List<Component> components) {
        this.components = components
    }


    @Override
    public String toString() {
        return components
    }
}
