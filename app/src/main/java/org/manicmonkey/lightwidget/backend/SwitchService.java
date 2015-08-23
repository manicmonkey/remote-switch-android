package org.manicmonkey.lightwidget.backend;

import java.util.List;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * @author James Baxter 2015-08-10.
 */
public interface SwitchService {
    @POST("/")
    Response create(@Body Switch newSwitch);

    @GET("/")
    List<Switch> get();

    @GET("/{name}")
    Switch get(@Path("name") String name);

    @PUT("/{name}")
    Response update(@Path("name") String name, @Body Switch existingSwitch);

    @DELETE("/{name}")
    Response delete(@Path("name") String name);

    @PATCH("/{name}")
    Response execute(@Path("name") String name, @Body SwitchAction switchAction);
}
