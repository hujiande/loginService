package com.hjd.net.managerservice.entity.sysmgr;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hjd.net.managerservice.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 附件表
 * </p>
 *
 * @author hujiande
 * @since 2019-07-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("st_att")
public class Att extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private LocalDateTime createDate;

    private Boolean deleteFlag;

    private String description;

    private String lotId;

    private String path;

    private String type;

    private Long fileSize;

    private String originName;

    /**
     * 有效标志
     */
    private String ynFlag;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 修改人
     */
    private String editor;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 修改时间
     */
    private LocalDateTime modifiedTime;

}
