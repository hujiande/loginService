package com.hjd.net.managerservice.entity.vo.sysmgr;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author hujiande
 * @since 2018-12-28
 */
public class AuthorityNode {
    private Long id;
    private Long parentId;
    private String fullId;
    private String label;
    private String code;
    private Integer showOrder;
    private List<AuthorityNode> children;

    public AuthorityNode(){}

    public AuthorityNode(Long id, Long parentId, String label) {
        this.id = id;
        this.parentId = parentId;
        this.label = label;
    }

    public List<AuthorityNode> getChildren() {
        if (children == null) {
            children = Lists.newArrayList();
        }
        return children;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getFullId() {
        return fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public void setChildren(List<AuthorityNode> children) {
        this.children = children;
    }
}
