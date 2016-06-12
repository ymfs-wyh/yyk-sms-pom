package com.yyk333.upacp.pay.services;

import java.util.Map;

import com.yyk333.upacp.pay.vo.AuthRequestVO;
import com.yyk333.upacp.pay.vo.BackTradeQueryVO;
import com.yyk333.upacp.pay.vo.BackTradeRequestVO;
import com.yyk333.upacp.pay.vo.FileTransferVO;
import com.yyk333.upacp.pay.vo.FrontTradeRequestVO;
import com.yyk333.upacp.pay.vo.ResponseViewObject;

public interface TradeService {

	public ResponseViewObject tradeDeal(Map<String, String> reqMap) throws Exception;

	public Map<String, String> objectToMap(FrontTradeRequestVO frontTradeVO) throws Exception;

	public ResponseViewObject paramsValidate(FrontTradeRequestVO frontTradeVO) throws Exception;
	
	public Map<String, String> objectToMap(BackTradeRequestVO frontTradeVO) throws Exception;

	public ResponseViewObject paramsValidate(BackTradeRequestVO backTradeRequestVO) throws Exception;

	public ResponseViewObject tradeCancel(Map<String, String> reqMap) throws Exception;

	public ResponseViewObject paramsQueryValidate(BackTradeQueryVO backTradeQueryVO) throws Exception;

	public Map<String, String> queryVOToMap(BackTradeQueryVO backTradeQueryVO) throws Exception;

	public ResponseViewObject tradeQuery(Map<String, String> reqMap) throws Exception;

	public ResponseViewObject paramsAuthValidate(AuthRequestVO authRequestVO) throws Exception;

	public Map<String, String> authVOToMap(AuthRequestVO authRequestVO) throws Exception;

	public ResponseViewObject tradeAuth(Map<String, String> reqMap) throws Exception;

	public ResponseViewObject paramsFileValidate(FileTransferVO fileTransferVO) throws Exception;

	public Map<String, String> fileObjToMap(FileTransferVO fileTransferVO) throws Exception;

	public ResponseViewObject fileTrans(Map<String, String> reqMap) throws Exception;

	public ResponseViewObject tradeAppDeal(Map<String, String> reqMap);


}
