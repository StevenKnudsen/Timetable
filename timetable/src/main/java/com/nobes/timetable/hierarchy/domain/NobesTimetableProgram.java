package com.nobes.timetable.hierarchy.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author xiangpen
 * @since 2023-02-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class NobesTimetableProgram implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("programName")
    private String programName;

    private Integer catoid;

    private Integer poid;


}
