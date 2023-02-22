package com.nobes.timetable.product.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@TableName(value = "programid")
@AllArgsConstructor
@NoArgsConstructor
public class ProgramId {

    @TableField("programName")
    private String programName;

    @TableField("catoid")
    private Integer catoid;

    @TableField("poid")
    private Integer poid;

}
