/*
 * Copyright (c) 2021. All content and codes belong to Ubuy Nigeria And should not be reproduced or used without written consent from the author
 *
 */

package com.ubuyng.app.Services;

import com.ubuyng.app.Responses.TaskResponse;
import com.ubuyng.app.ubuyapi.Models.TaskRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TaskService {

    @POST("customers/post/debug/task")
    Call<TaskResponse> saveTask(@Body TaskRequest taskRequest);

}
