package imooc.com.bearlive.watcher;

import java.io.IOException;

import imooc.com.bearlive.ResponseObject;
import imooc.com.bearlive.livelist.LiveListResponseObj;
import imooc.com.bearlive.utils.request.BaseRequest;

/**
 * Created by Administrator.
 */

public class GetWatcherRequest extends BaseRequest {
    private static final String HOST = "http://imoocbearlive.butterfly.mopaasapp.com/roomServlet?action=getWatcher";



    public String getUrl(String roomId) {
        return HOST + "&=roomId" + roomId;
    }

    @Override
    protected void onFail(IOException e) {
        sendFailMsg(-100,e.toString());
    }

    @Override
    protected void onResponseSuccess(String body) {
        WatcherResponseObj watcherResponseObj = gson.fromJson(body, WatcherResponseObj.class);
        if (watcherResponseObj == null) {
            sendFailMsg(-101, "数据格式错误");
            return;
        }

        if (watcherResponseObj.code.equals(ResponseObject.CODE_SUCCESS)) {
            sendSuccMsg(watcherResponseObj.data);
        } else if (watcherResponseObj.code.equals(ResponseObject.CODE_FAIL)) {
            sendFailMsg(Integer.valueOf(watcherResponseObj.errCode), watcherResponseObj.errMsg);
        }
    }

    @Override
    protected void onResponseFail(int code) {
        sendFailMsg(code,"服务器异常");
    }

}
