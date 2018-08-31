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
public class WechatGetHeadImgAction extends AbstractHttpAction {

    private String headImgUrl;
	
    /**
     * <p>Constructor for AbstractHttpAction.</p>
     *
     * @param context  a {@link QQContext} object.
     * @param listener a {@link QQActionListener} object.
     */
    public WechatGetHeadImgAction(QQContext context, QQActionListener listener,String headImgUrl) {
        super(context, listener);
        this.headImgUrl=headImgUrl; 
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected QQHttpRequest onBuildRequest() throws QQException, JSONException { 
    	String url=getContext().getSession().getBase_host()+headImgUrl;
        QQHttpRequest req = createHttpRequest("GET", url);  
         
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
