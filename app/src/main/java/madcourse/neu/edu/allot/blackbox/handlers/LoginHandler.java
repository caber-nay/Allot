package madcourse.neu.edu.allot.blackbox.handlers;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import madcourse.neu.edu.allot.blackbox.api.AllotApi;
import madcourse.neu.edu.allot.blackbox.models.User;
import madcourse.neu.edu.allot.blackbox.responders.LoginResponder;
import madcourse.neu.edu.allot.blackbox.response.LoginResponse;

/**
 * Created by zeko on 11/28/17.
 */
public class LoginHandler {

    private static RequestParams params;
    private static AsyncHttpClient client;

    static {
        client = new AsyncHttpClient();
    }

    public LoginHandler() {


    }

    public static void doLogin(final LoginResponder responder, String email, String password) {

        params = new RequestParams();
        params.put("email", email);
        params.put("password", password);

        client.post(AllotApi.LOGIN_URL, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                responder.failedLogin("Error");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                LoginResponse resp = LoginResponse.parseJson(responseString);

                int status = resp.getStatus();

                if (status == 200) {

                    User user = new User();

                    // add user information
                    user.setFirstName(resp.getFirstName());
                    user.setLastName(resp.getLastName());
                    user.setEmail(resp.getEmail());
                    user.setId(resp.getId());
                    user.setEmail(resp.getEmail());
                    user.setToken(resp.getToken());

                    responder.successfulLogin(user);
                } else {
                    responder.failedLogin("Invalid username/password");
                }
            }
        });

    }
}