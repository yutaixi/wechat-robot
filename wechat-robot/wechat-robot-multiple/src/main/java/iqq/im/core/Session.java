package iqq.im.core;
 

public interface Session {

  
	public State getState() ;
	public enum State {
        OFFLINE,
        ONLINE,
        KICKED,
        LOGINING,
        ERROR
    }
	
	
}
