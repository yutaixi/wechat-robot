/*
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements.  See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* The ASF licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License.  You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

/**
 * Project  : WebQQCoreAsync
 * Package  : iqq.im.core
 * File     : QQSession.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2012-9-5
 * License  : Apache License 2.0
 */
package iqq.im.core;
  
import java.util.Random;

import com.blade.kit.json.JSONObject;
import com.im.base.wechat.WechatContact;
import com.im.utils.MathUtil;
import com.wechat.bean.WechatUser;
 
 

/**
 * QQSession保存了每次登陆时候的状态信息
 *
 * @author solosky
 */
public class QQSession implements Session{
    private long clientId=53999199;
    private String sessionId;
    private String vfwebqq;
    private String ptwebqq;
    private String loginSig;
    private String cfaceKey;    // 上传群图片时需要
    private String cfaceSig;    // 上传群图片时需要
    private String emailAuthKey;// 邮箱登录认证
    private int index;            // 禁用群时需要
    private int port;            // 禁用群时需要
    private int pollErrorCnt;
    private volatile State state;
    private String psessionid;

    
    
    //wechat session
    
    private String uuid;
    private String redirect_uri;
    private String base_uri; 
    private String base_host;
    private String webpush_url;
    private String file_upload_host; 
	private String skey;
	private String synckey;
	private String wxsid;
	private Long wxuin;
	private String pass_ticket;
	private String webwx_data_ticket; 
	private String  deviceId = "e" +MathUtil.genFixedLengthRandomNum(15);
	private String webwxuvid;
	private String webwx_auth_ticket;
	private String cookie;
	
	private JSONObject baseRequest;
	private JSONObject SyncKey;
	private WechatContact User;
	
	
	
	
    
    public String getWebpush_url() {
		return webpush_url;
	}

	public void setWebpush_url(String webpush_url) {
		this.webpush_url = webpush_url;
	}

	public String getWebwxuvid() {
		return webwxuvid;
	}

	public void setWebwxuvid(String webwxuvid) {
		this.webwxuvid = webwxuvid;
	}

	public String getWebwx_auth_ticket() {
		return webwx_auth_ticket;
	}

	public void setWebwx_auth_ticket(String webwx_auth_ticket) {
		this.webwx_auth_ticket = webwx_auth_ticket;
	}

	public String getSkey() {
		return skey;
	}

	public void setSkey(String skey) {
		this.skey = skey;
	}

	public String getSynckey() {
		return synckey;
	}

	public void setSynckey(String synckey) {
		this.synckey = synckey;
	}

	public String getWxsid() {
		return wxsid;
	}

	public void setWxsid(String wxsid) {
		this.wxsid = wxsid;
	}

	public Long getWxuin() {
		return wxuin;
	}

	public void setWxuin(Long wxuin) {
		this.wxuin = wxuin;
	}

	public String getPass_ticket() {
		return pass_ticket;
	}

	public void setPass_ticket(String pass_ticket) {
		this.pass_ticket = pass_ticket;
	}

	public String getWebwx_data_ticket() {
		return webwx_data_ticket;
	}

	public void setWebwx_data_ticket(String webwx_data_ticket) {
		this.webwx_data_ticket = webwx_data_ticket;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public JSONObject getBaseRequest() {
		return baseRequest;
	}

	public void setBaseRequest(JSONObject baseRequest) {
		this.baseRequest = baseRequest;
	}

	public JSONObject getSyncKey() {
		return SyncKey;
	}

	public void setSyncKey(JSONObject syncKey) {
		SyncKey = syncKey;
	}

	 

	public WechatContact getUser() {
		return User;
	}

	public void setUser(WechatContact user) {
		User = user;
	}

	public String getRedirect_uri() {
		return redirect_uri;
	}

	public void setRedirect_uri(String redirect_uri) {
		this.redirect_uri = redirect_uri;
	}

	public String getBase_uri() {
		return base_uri;
	}

	public void setBase_uri(String base_uri) {
		this.base_uri = base_uri;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getPsessionid() {
        return psessionid;
    }

    public void setPsessionid(String psessionid) {
        this.psessionid = psessionid;
    }
 

    /**
     * <p>Getter for the field <code>clientId</code>.</p>
     *
     * @return a long.
     */
    public long getClientId() {
        if (clientId == 0) {
            clientId = Math.abs(new Random().nextInt()); //random??
        }
        return clientId;
    }

    /**
     * <p>Setter for the field <code>clientId</code>.</p>
     *
     * @param clientId a long.
     */
    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    /**
     * <p>Getter for the field <code>sessionId</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * <p>Setter for the field <code>sessionId</code>.</p>
     *
     * @param sessionId a {@link java.lang.String} object.
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * <p>Getter for the field <code>vfwebqq</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getVfwebqq() {
        return vfwebqq;
    }

    /**
     * <p>Setter for the field <code>vfwebqq</code>.</p>
     *
     * @param vfwebqq a {@link java.lang.String} object.
     */
    public void setVfwebqq(String vfwebqq) {
        this.vfwebqq = vfwebqq;
    }

    /**
     * <p>Getter for the field <code>pollErrorCnt</code>.</p>
     *
     * @return a int.
     */
    public int getPollErrorCnt() {
        return pollErrorCnt;
    }

    /**
     * <p>Setter for the field <code>pollErrorCnt</code>.</p>
     *
     * @param pollErrorCnt a int.
     */
    public void setPollErrorCnt(int pollErrorCnt) {
        this.pollErrorCnt = pollErrorCnt;
    }

    /**
     * <p>Getter for the field <code>state</code>.</p>
     *
     * @return a {@link iqq.im.core.QQSession.State} object.
     */
    public State getState() {
        return state;
    }

    /**
     * <p>Setter for the field <code>state</code>.</p>
     *
     * @param state a {@link iqq.im.core.QQSession.State} object.
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * <p>Getter for the field <code>cfaceKey</code>.</p>
     *
     * @return the cfaceKey
     */
    public String getCfaceKey() {
        return cfaceKey;
    }

    /**
     * <p>Setter for the field <code>cfaceKey</code>.</p>
     *
     * @param cfaceKey the cfaceKey to set
     */
    public void setCfaceKey(String cfaceKey) {
        this.cfaceKey = cfaceKey;
    }

    /**
     * <p>Getter for the field <code>cfaceSig</code>.</p>
     *
     * @return the cfaceSig
     */
    public String getCfaceSig() {
        return cfaceSig;
    }

    /**
     * <p>Setter for the field <code>cfaceSig</code>.</p>
     *
     * @param cfaceSig the cfaceSig to set
     */
    public void setCfaceSig(String cfaceSig) {
        this.cfaceSig = cfaceSig;
    }

    /**
     * <p>Getter for the field <code>index</code>.</p>
     *
     * @return a int.
     */
    public int getIndex() {
        return index;
    }

    /**
     * <p>Setter for the field <code>index</code>.</p>
     *
     * @param index a int.
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * <p>Getter for the field <code>port</code>.</p>
     *
     * @return a int.
     */
    public int getPort() {
        return port;
    }

    /**
     * <p>Setter for the field <code>port</code>.</p>
     *
     * @param port a int.
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * <p>Getter for the field <code>ptwebqq</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getPtwebqq() {
        return ptwebqq;
    }

    /**
     * <p>Setter for the field <code>ptwebqq</code>.</p>
     *
     * @param ptwebqq a {@link java.lang.String} object.
     */
    public void setPtwebqq(String ptwebqq) {
        this.ptwebqq = ptwebqq;
    }

    /**
     * <p>Getter for the field <code>loginSig</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getLoginSig() {
        return loginSig;
    }

    /**
     * <p>Setter for the field <code>loginSig</code>.</p>
     *
     * @param loginSig a {@link java.lang.String} object.
     */
    public void setLoginSig(String loginSig) {
        this.loginSig = loginSig;
    }

    /**
     * <p>Getter for the field <code>emailAuthKey</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getEmailAuthKey() {
        return emailAuthKey;
    }

    /**
     * <p>Setter for the field <code>emailAuthKey</code>.</p>
     *
     * @param emailAuthKey a {@link java.lang.String} object.
     */
    public void setEmailAuthKey(String emailAuthKey) {
        this.emailAuthKey = emailAuthKey;
    }

	public String getBase_host() {
		return base_host;
	}

	public void setBase_host(String base_host) {
		this.base_host = base_host;
	}

	public String getFile_upload_host() {
		return file_upload_host;
	}

	public void setFile_upload_host(String file_upload_host) {
		this.file_upload_host = file_upload_host;
	}
    
    
}
