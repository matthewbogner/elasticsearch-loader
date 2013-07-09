package net.ibogner.elasticsearch.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class LoaderConfig extends Configuration {

    @JsonProperty @NotNull @Valid
    private String clusterName;

    @JsonProperty @NotNull @Valid
    private String node1IP;

    @JsonProperty @NotNull @Valid
    private int node1Port;

    @JsonProperty @NotNull @Valid
    private String node2IP;

    @JsonProperty @NotNull @Valid
    private int node2Port;


    public String getClusterName() {
        return clusterName;
    }

    public String getNode1IP() {
        return node1IP;
    }

    public int getNode1Port() {
        return node1Port;
    }

    public String getNode2IP() {
        return node2IP;
    }

    public int getNode2Port() {
        return node2Port;
    }
}
