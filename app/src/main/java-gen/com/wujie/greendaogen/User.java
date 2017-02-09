package com.wujie.greendaogen;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by wujie on 2017/2/7.
 */
@Entity
public class User {
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private int age;
}
