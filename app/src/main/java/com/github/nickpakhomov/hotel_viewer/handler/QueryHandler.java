package com.github.nickpakhomov.hotel_viewer.handler;

import com.github.nickpakhomov.hotel_viewer.models.ResponseBody;

import java.util.List;

/**
 * Created by Nikolay Pakhomov on 08/09/17.
 */

interface QueryHandler {
    void insert(List<ResponseBody> list);
    void update(ResponseBody responseBody);
}
