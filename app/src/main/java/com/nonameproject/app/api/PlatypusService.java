package com.nonameproject.app.api;

import com.google.gson.JsonObject;
import com.nonameproject.app.content.ContentList;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface PlatypusService {

    @GET("/dataset/list?f=100&iDisplayStart=0&iDisplayLength=100&s_fields=JSON&f_id=16&ajax=true&_=1452849816186")
    Call<JsonObject> getMainJson();

    @GET("/dataset/list?f=14&iDisplayStart=0&iDisplayLength=200&iSortingCols=1&iSortCol_0=1&s_fields=id,name&_=1453455717700")
    Call<ContentList> getMunicipalityList();

    @GET("/dataset/list?f=20&iDisplayStart=0&iDisplayLength=200&iSortingCols=1&iSortCol_0=1&s_fields=id,name&_=1453455717700")
    Call<ContentList> getOrgformList();

    @GET("/dataset/list?f=15&iDisplayStart=0&iDisplayLength=200&iSortingCols=1&iSortCol_0=1&s_fields=id,name")
    Call<ContentList> getOrganizationList(@Query("f_municipality_id") int m, @Query("f_orgform") int o);

    //dataset/list?f=15&iDisplayStart=0&iDisplayLength=200&iSortingCols=1&iSortCol_0=1&s_fields=id,name&f_municipality_id=1&f_orgform=1&_=1453566156184
    //dataset/list?f=15&iDisplayStart=0&iDisplayLength=200&iSortingCols=1&iSortCol_0=1&s_fields=id,name&f_municipality_id=2&f_orgform=1&_=1453566247373
    //dataset/list?f=15&iDisplayStart=0&iDisplayLength=200&iSortingCols=1&iSortCol_0=1&s_fields=id,name&f_municipality_id=3&f_orgform=1&_=1453567034957
    //dataset/list?f=15&iDisplayStart=0&iDisplayLength=200&iSortingCols=1&iSortCol_0=1&s_fields=id,name&f_municipality_id=19&f_orgform=1&_=1453568910169

}
