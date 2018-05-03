package com.netflix.simianarmy.client.aws;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.autoscaling.AmazonAutoScalingClient;
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancingClient;
import com.amazonaws.services.elasticloadbalancing.model.*;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ElasticLoadBalancer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticLoadBalancer.class);;
    private AmazonElasticLoadBalancingClient elbClient;
    private String region;

    public ElasticLoadBalancer(AmazonElasticLoadBalancingClient elbClient, String region) {
        this.elbClient = elbClient;
        this.region = region;
    }

    /**
     * Describe a set of specific ELBs.
     *
     * @param names the ELB names
     * @return the ELBs
     */
    public List<LoadBalancerDescription> describeElasticLoadBalancers(String... names) {
        if (names == null || names.length == 0) {
            LOGGER.info(String.format("Getting all ELBs in region %s.", region));
        } else {
            LOGGER.info(String.format("Getting ELBs for %d names in region %s.", names.length, region));
        }

        DescribeLoadBalancersRequest request = new DescribeLoadBalancersRequest().withLoadBalancerNames(names);
        DescribeLoadBalancersResult result = elbClient.describeLoadBalancers(request);
        List<LoadBalancerDescription> elbs = result.getLoadBalancerDescriptions();
        LOGGER.info(String.format("Got %d ELBs in region %s.", elbs.size(), region));
        return elbs;
    }

    /**
     * Describe a specific ELB.
     *
     * @param name the ELB names
     * @return the ELBs
     */
    public LoadBalancerAttributes describeElasticLoadBalancerAttributes(String name) {
        LOGGER.info(String.format("Getting attributes for ELB with name '%s' in region %s.", name, region));
        DescribeLoadBalancerAttributesRequest request = new DescribeLoadBalancerAttributesRequest().withLoadBalancerName(name);
        DescribeLoadBalancerAttributesResult result = elbClient.describeLoadBalancerAttributes(request);
        LoadBalancerAttributes attrs = result.getLoadBalancerAttributes();
        LOGGER.info(String.format("Got attributes for ELB with name '%s' in region %s.", name, region));
        return attrs;
    }

    /**
     * Retreive the tags for a specific ELB.
     *
     * @param name the ELB names
     * @return the ELBs
     */
    public List<TagDescription> describeElasticLoadBalancerTags(String name) {
        LOGGER.info(String.format("Getting tags for ELB with name '%s' in region %s.", name, region));

        DescribeTagsRequest request = new DescribeTagsRequest().withLoadBalancerNames(name);
        DescribeTagsResult result = elbClient.describeTags(request);
        LOGGER.info(String.format("Got tags for ELB with name '%s' in region %s.", name, region));
        return result.getTagDescriptions();
    }

    public void deleteElasticLoadBalancer(String elbId) {
        Validate.notEmpty(elbId);
        LOGGER.info(String.format("Deleting ELB %s in region %s.", elbId, region));

        DeleteLoadBalancerRequest request = new DeleteLoadBalancerRequest(elbId);
        elbClient.deleteLoadBalancer(request);
    }


    public static ElasticLoadBalancer create(AWSCredentialsProvider awsCredentialsProvider, ClientConfiguration awsClientConfig, String region) {
        return new ElasticLoadBalancer(elbClient(awsCredentialsProvider,awsClientConfig,region), region);
    }

    private static AmazonElasticLoadBalancingClient elbClient(AWSCredentialsProvider awsCredentialsProvider, ClientConfiguration awsClientConfig, String region){
        AmazonElasticLoadBalancingClient client;
            if (awsClientConfig == null) {
                if (awsCredentialsProvider == null) {
                    client = new AmazonElasticLoadBalancingClient();
                } else {
                    client = new AmazonElasticLoadBalancingClient(awsCredentialsProvider);
                }
            } else {
                if (awsCredentialsProvider == null) {
                    client = new AmazonElasticLoadBalancingClient(awsClientConfig);
                } else {
                    client = new AmazonElasticLoadBalancingClient(awsCredentialsProvider, awsClientConfig);
                }
            }
            client.setEndpoint("autoscaling." + region + ".amazonaws.com");
            return client;

    }
}