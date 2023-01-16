package models

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(["metaClass"])
@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
class Component {

    @JsonProperty(value = "component")
    List<Map<String, Object>> component

    Component(List<Map<String, Object>> component) {
        this.component = component
    }

    Component() {
    }

    List<Map<String, Object>> getComponent() {
        return component
    }

    void setComponent(List<Map<String, Object>> component) {
        this.component = component
    }

    @Override
    public String toString() {
        return component
    }
}
