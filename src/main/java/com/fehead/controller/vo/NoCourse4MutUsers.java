package com.fehead.controller.vo;

import com.fehead.dao.entity.NoCourse;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lmwis
 * @description:
 * @date 2019-09-16 17:02
 * @Version 1.0
 */
public class NoCourse4MutUsers extends NoCourse {

    private List<String> username = new ArrayList<>();

    public List<String> getUsername() {
        return username;
    }

    public void setUsername(List<String> username) {
        this.username = username;
    }
}
