package com.wechat.action;

 
import iqq.im.QQActionListener;
import iqq.im.QQException;
import iqq.im.action.AbstractHttpAction;
import iqq.im.core.QQConstants;
import iqq.im.core.QQContext;
import iqq.im.core.QQSession;
import iqq.im.event.QQActionEvent;
import iqq.im.http.QQHttpRequest;
import iqq.im.http.QQHttpResponse;

import org.json.JSONException;

import com.wechat.core.WechatConstants;

import javax.imageio.ImageIO;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by Tony on 10/6/15.
 */
public class GetQRCodeAction extends AbstractHttpAction {

    /**
     * <p>Constructor for AbstractHttpAction.</p>
     *
     * @param context  a {@link QQContext} object.
     * @param listener a {@link QQActionListener} object.
     */
    public GetQRCodeAction(QQContext context, QQActionListener listener) {
        super(context, listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected QQHttpRequest onBuildRequest() throws QQException, JSONException {
    	QQSession session=this.getContext().getSession();
        QQHttpRequest req = createHttpRequest("GET", WechatConstants.URL_GET_QRCODE+session.getUuid()); 
        req.addGetValue("t", "webwx");
        req.addGetValue("_", String.valueOf(System.currentTimeMillis())); 
         
        return req;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onHttpStatusOK(QQHttpResponse response) throws QQException, JSONException {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(response.getResponseData());
            notifyActionEvent(QQActionEvent.Type.EVT_OK, ImageIO.read(in));
        } catch (IOException e) {
            notifyActionEvent(QQActionEvent.Type.EVT_ERROR, new QQException(QQException.QQErrorCode.UNKNOWN_ERROR, e));
        }
    }
}
