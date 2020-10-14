package com.lin.missyou.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
public class ThemeSpu {

    @Id
    private Long id;
    private Long themeId;
    private Long spuId;

}
