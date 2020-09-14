package org.selftest.interview.test;

import java.io.Serializable;
import java.util.Date;

/**
 * @author guozhipeng
 * @date 2020/8/20 17:30
 */
public class A implements Serializable{

    private static final long serialVersionUID = -1080500962137810403L;
    private Date endTime;

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "A{" +
                "endTime=" + DateUtil.format(endTime,"yyyy-MM-dd HH:mm:ss") +
                '}';
    }
}
