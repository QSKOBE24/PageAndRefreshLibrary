package com.guosen.pageandrefreshlistview;

import java.util.List;

/**
 * The data request interface.
 *
 * @author QSKOBE24
 */
public interface PageAndRefreshRequestService {
	
	/**
	 * Send request to get data from net or database.
	 * When received result data of the request, we should called {@link PageAndRefreshRequestCallBack#onRequestComplete(List, int)} to
	 * render data.
	 *  
	 * @param page What page data was requested .
	 * @param callback The result data callback.
	 */
	void sendRequest(int page, PageAndRefreshRequestCallBack callback);
}
