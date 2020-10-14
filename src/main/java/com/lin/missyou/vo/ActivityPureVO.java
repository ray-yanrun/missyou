package com.lin.missyou.vo;

import com.lin.missyou.model.Activity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@NoArgsConstructor
public class ActivityPureVO {

    private Long id;
    private String title;
    private String startTime;
    private String endTime;
    private String remark;
    private Boolean online;
    private String entranceImg;

    public ActivityPureVO(Activity activity){
        BeanUtils.copyProperties(activity, this);
    }

    public ActivityPureVO(Object obj){
        BeanUtils.copyProperties(obj, this);
    }
}
