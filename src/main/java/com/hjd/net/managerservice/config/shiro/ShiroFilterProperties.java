package com.hjd.net.managerservice.config.shiro;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "permission-config")
public class ShiroFilterProperties {
    List<Map<String, String>> perms;

    public List<Map<String, String>> getPerms() {
        return perms;
    }

    public void setPerms(List<Map<String, String>> perms) {
        this.perms = perms;
    }
}
