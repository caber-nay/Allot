package madcourse.neu.edu.allot.blackbox.handlers;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import madcourse.neu.edu.allot.blackbox.api.AllotApi;
import madcourse.neu.edu.allot.blackbox.models.Group;
import madcourse.neu.edu.allot.blackbox.responders.FetchGroupsResponder;
import madcourse.neu.edu.allot.blackbox.response.FetchUserGroupsResponse;

public class FetchGroupsHandler {

    private static RequestParams params;
    private static AsyncHttpClient client;

    static {
        client = new AsyncHttpClient();
    }

    public FetchGroupsHandler() {


    }

    public static void doFetch(final FetchGroupsResponder responder, String id, String token) {

        params = new RequestParams();
        params.put("id", id);
        params.put("token", token);

        client.post(AllotApi.FETCH_USER_GROUPS, params, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                responder.onFailedGroupsFetch("Error");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                FetchUserGroupsResponse resp = FetchUserGroupsResponse.parseJson(responseString);

                int status = resp.getStatus();

                if (status == 200) {

                    List<Group> groups = resp.getGroups();
                    responder.onSuccessfullGroupsFetch(groups);

                } else {
                    responder.onFailedGroupsFetch("Server Error");
                }
            }
        });

    }
}
