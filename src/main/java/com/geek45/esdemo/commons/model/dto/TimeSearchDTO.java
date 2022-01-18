/**
 * From geek45.com
 * Email to : rubixgeek795@gmail.com
 */
package com.geek45.esdemo.commons.model.dto;

import java.io.Serializable;

/**
 * @ClassName: TimeSearchDTO
 * @Decription: 时间查询条件
 * @Author: rubik
 *  rubik create TimeSearchDTO.java of 2022/1/18 12:13 下午
 */
public class TimeSearchDTO implements Serializable {
    private static final long serialVersionUID = -5689729350622952560L;
    /**
     * the time filed of search
     */
    private String timeField;
    /**
     * the start time
     * date format is yyyy-MM-dd HH:mm:ss
     */
    private String startTime;
    /**
     * the end time
     * date format is yyyy-MM-dd HH:mm:ss
     */
    private String endTime;

    public String getTimeField() {
        return timeField;
    }

    public void setTimeField(String timeField) {
        this.timeField = timeField;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
