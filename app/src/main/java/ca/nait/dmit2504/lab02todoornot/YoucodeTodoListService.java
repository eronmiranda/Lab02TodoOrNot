package ca.nait.dmit2504.lab02todoornot;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface YoucodeTodoListService {
    @GET("Lab02Get.jsp")
    Call<String> listLab02ByUsernamePassword(@Query("ALIAS") String username,
                                             @Query("PASSWORD") String password);

    @FormUrlEncoded
    @POST("Lab02Post.jsp")
    Call<String> archiveListItem(@Field("LIST_TITLE") String listTitle,
                                @Field("CONTENT") String listItem,
                                @Field("COMPLETED_FLAG") String completed,
                                @Field("ALIAS") String username,
                                @Field("PASSWORD") String password,
                                 @Field("CREATED_DATE") String date);
}
