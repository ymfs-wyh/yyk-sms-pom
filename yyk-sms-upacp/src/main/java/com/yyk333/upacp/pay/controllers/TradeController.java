package com.yyk333.upacp.pay.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yyk333.upacp.pay.services.TradeService;
import com.yyk333.upacp.pay.vo.AuthRequestVO;
import com.yyk333.upacp.pay.vo.BackTradeQueryVO;
import com.yyk333.upacp.pay.vo.BackTradeRequestVO;
import com.yyk333.upacp.pay.vo.FileTransferVO;
import com.yyk333.upacp.pay.vo.FrontTradeRequestVO;
import com.yyk333.upacp.pay.vo.ResponseViewObject;

@Controller
@RequestMapping(value = "/trade")
public class TradeController {

	private static final Logger logger = LoggerFactory.getLogger(TradeController.class);

	@Autowired
	private TradeService tradeService;

	/**
	 * 交易：消费：前台跳转，有前台通知应答和后台通知应答,网页、手机网页<br>
	 * 日期： 2015-05-03<br>
	 * 版本： 1.0.0
	 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码性能规范性等方面的保障
	 * <br>
	 * 提示：该接口参考文档位置：open.unionpay.com帮助中心 下载 产品接口规范 《网关支付产品接口规范》，<br>
	 * 《平台接入接口规范-第5部分-附录》（内包含应答码接口规范，全渠道平台银行名称-简码对照表)<br>
	 * 《全渠道平台接入接口规范 第3部分 文件接口》（对账文件格式说明）<br>
	 * 测试过程中的如果遇到疑问或问题您可以：1）优先在open平台中查找答案： 调试过程中的问题或其他问题请在
	 * https://open.unionpay.com/ajweb/help/faq/list 帮助中心 FAQ 搜索解决方案
	 * 测试过程中产生的6位应答码问题疑问请在https://open.unionpay.com/ajweb/help/respCode/
	 * respCodeList 输入应答码搜索解决方案 2） 咨询在线人工支持：
	 * open.unionpay.com注册一个用户并登陆在右上角点击“在线客服”，咨询人工QQ测试支持。
	 * 交易说明:1）以后台通知或交易状态查询交易确定交易成功,前台通知不能作为判断成功的标准.
	 * 2）交易状态查询交易（query_trade）建议调用机制：前台类交易建议间隔（5分、10分、30分、60分、120分）发起交易查询，
	 * 如果查询到结果成功，则不用再查询。（失败，处理中，查询不到订单均可能为中间状态）。也可以建议商户使用payTimeout（支付超时时间），
	 * 过了这个时间点查询，得到的结果为最终结果。
	 */
	@RequestMapping(value = "/trade", method = RequestMethod.POST)
	public void front_trade(FrontTradeRequestVO frontTradeVO, HttpServletResponse resp) {

		try {
			ResponseViewObject object = tradeService.paramsValidate(frontTradeVO);

			if (object.getErrCode() == null) {
				Map<String, String> reqMap = tradeService.objectToMap(frontTradeVO);
				object = tradeService.tradeDeal(reqMap);
				resp.getWriter().write(String.valueOf(object.getData().get("html")));
			}
		} catch (Exception e) {
			ResponseViewObject object = new ResponseViewObject();
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("交易异常！");
			logger.debug("交易异常！", e);
		}
	}
	/**
	 * 交易：消费：前台跳转，有前台通知应答和后台通知应答,手机控件<br>
	 */
	@RequestMapping(value = "/getTradeNo")
	@ResponseBody
	public ResponseViewObject get_trade_no(FrontTradeRequestVO frontTradeVO, HttpServletResponse resp) {

		try {
			ResponseViewObject object = tradeService.paramsValidate(frontTradeVO);

			if (object == null) {
				Map<String, String> reqMap = tradeService.objectToMap(frontTradeVO);
				object = tradeService.tradeAppDeal(reqMap);
			}
			return object;
		} catch (Exception e) {
			ResponseViewObject object = new ResponseViewObject();
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("交易异常！");
			logger.debug("交易异常！", e);
			return object;
		}
	}

	/**
	 * 交易：消费撤销：后台资金类交易，有同步应答和后台通知应答,网关,手机页面,手机控件<br>
	 * 日期： 2016-05-04<br>
	 * 版本： 1.0.0
	 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码性能规范性等方面的保障
	 * <br>
	 * 该接口参考文档位置：open.unionpay.com帮助中心 下载 产品接口规范 《网关支付产品接口规范》<br>
	 * 《平台接入接口规范-第5部分-附录》（内包含应答码接口规范，全渠道平台银行名称-简码对照表）<br>
	 * 测试过程中的如果遇到疑问或问题您可以：1）优先在open平台中查找答案： 调试过程中的问题或其他问题请在
	 * https://open.unionpay.com/ajweb/help/faq/list 帮助中心 FAQ 搜索解决方案
	 * 测试过程中产生的6位应答码问题疑问请在https://open.unionpay.com/ajweb/help/respCode/
	 * respCodeList 输入应答码搜索解决方案 2） 咨询在线人工支持：
	 * open.unionpay.com注册一个用户并登陆在右上角点击“在线客服”，咨询人工QQ测试支持。
	 * 交易说明:1）以后台通知或交易状态查询交易（query_trade）确定交易成功，建议发起查询交易的机制：可查询N次（不超过6次），
	 * 每次时间间隔2N秒发起,即间隔1，2，4，8，16，32S查询（查询到03，04，05继续查询，否则终止查询）
	 * 2）消费撤销仅能对当清算日的消费做，必须为全额，一般当日或第二日到账。
	 * 
	 * 交易：退货交易：后台资金类交易，有同步应答和后台通知应答,网关,手机页面,手机控件<br>
	 * 日期： 2016-05-04<br>
	 * 版本： 1.0.0
	 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码性能规范性等方面的保障
	 * <br>
	 * 该接口参考文档位置：open.unionpay.com帮助中心 下载 产品接口规范 《网关支付产品接口规范》<br>
	 * 《平台接入接口规范-第5部分-附录》（内包含应答码接口规范，全渠道平台银行名称-简码对照表）<br>
	 * 测试过程中的如果遇到疑问或问题您可以：1）优先在open平台中查找答案： 调试过程中的问题或其他问题请在
	 * https://open.unionpay.com/ajweb/help/faq/list 帮助中心 FAQ 搜索解决方案
	 * 测试过程中产生的6位应答码问题疑问请在https://open.unionpay.com/ajweb/help/respCode/
	 * respCodeList 输入应答码搜索解决方案 2） 咨询在线人工支持：
	 * open.unionpay.com注册一个用户并登陆在右上角点击“在线客服”，咨询人工QQ测试支持。 交易说明：
	 * 1）以后台通知或交易状态查询交易（query_trade）确定交易成功，建议发起查询交易的机制：可查询N次（不超过6次），每次时间间隔2N秒发起,
	 * 即间隔1，2，4，8，16，32S查询（查询到03，04，05继续查询，否则终止查询） 2）退货金额不超过总金额，可以进行多次退货
	 * 3）退货能对11个月内的消费做（包括当清算日），支持部分退货或全额退货，到账时间较长，一般1-10个清算日（多数发卡行5天内，但工行可能会10天）
	 * ，所有银行都支持
	 *
	 * 交易：预授权撤销：后台交易，有同步应答和后台通知应答<br>
	 * 日期： 2016-05-05<br>
	 * 版本： 1.0.0 
	 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码性能规范性等方面的保障<br>
	 * 该接口参考文档位置：open.unionpay.com帮助中心 下载  产品接口规范  《网关支付产品接口规范》<br>
	 *              《平台接入接口规范-第5部分-附录》（内包含应答码接口规范，全渠道平台银行名称-简码对照表）<br>
	 * 测试过程中的如果遇到疑问或问题您可以：1）优先在open平台中查找答案：
	 * 							        调试过程中的问题或其他问题请在 https://open.unionpay.com/ajweb/help/faq/list 帮助中心 FAQ 搜索解决方案
	 *                             测试过程中产生的6位应答码问题疑问请在https://open.unionpay.com/ajweb/help/respCode/respCodeList 输入应答码搜索解决方案
	 *                          2） 咨询在线人工支持： open.unionpay.com注册一个用户并登陆在右上角点击“在线客服”，咨询人工QQ测试支持。
	 * 交易说明:1）以后台通知或交易状态查询交易（Form_6_5_Query）确定交易成功。建议发起查询交易的机制：可查询N次（不超过6次），每次时间间隔2N秒发起,即间隔1，2，4，8，16，32S查询（查询到03，04，05继续查询，否则终止查询）
	 *       2）预授权撤销对清算日30天之内（包括第30天）的预授权做，必须为预授权金额全额撤销。
	 *
	 * 交易：预授权完成：后台交易，有同步应答和后台通知应答<br>
	 * 日期： 2016-05-05<br>
	 * 版本： 1.0.0 
	 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码性能规范性等方面的保障<br>
	 * 该接口参考文档位置：open.unionpay.com帮助中心 下载  产品接口规范  《网关支付产品接口规范》<br>
	 *              《平台接入接口规范-第5部分-附录》（内包含应答码接口规范，全渠道平台银行名称-简码对照表）<br>
	 * 测试过程中的如果遇到疑问或问题您可以：1）优先在open平台中查找答案：
	 * 							        调试过程中的问题或其他问题请在 https://open.unionpay.com/ajweb/help/faq/list 帮助中心 FAQ 搜索解决方案
	 *                             测试过程中产生的6位应答码问题疑问请在https://open.unionpay.com/ajweb/help/respCode/respCodeList 输入应答码搜索解决方案
	 *                          2） 咨询在线人工支持： open.unionpay.com注册一个用户并登陆在右上角点击“在线客服”，咨询人工QQ测试支持。
	 * 交易说明:1）以后台通知或交易状态查询交易（Form_6_5_Query）确定交易成功。建议发起查询交易的机制：可查询N次（不超过6次），每次时间间隔2N秒发起,即间隔1，2，4，8，16，32S查询（查询到03，04，05继续查询，否则终止查询）
	 *       2）预授权完成交易必须在预授权交易30日内发起，否则预授权交易自动解冻。预授权完成金额可以是预授权金额的(0-115%] （大于0小于等于115）
	 *
	 */
	@RequestMapping(value = "/traCancel", method = RequestMethod.POST)
	@ResponseBody
	public ResponseViewObject back_trade_cancel(BackTradeRequestVO backTradeRequestVO) {
		try {
			ResponseViewObject object = tradeService.paramsValidate(backTradeRequestVO);

			if (object == null) {
				Map<String, String> reqMap = tradeService.objectToMap(backTradeRequestVO);
				object = tradeService.tradeCancel(reqMap);
			}
			return object;
		} catch (Exception e) {
			ResponseViewObject object = new ResponseViewObject();
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("交易异常！");
			logger.debug("交易异常！", e);
			return object;
		}
	}
	
	/**
	 * 交易：交易状态查询交易：只有同步应答 ,网关,手机页面,手机控件<br>
	 * 日期： 2015-05-05<br>
	 * 版本： 1.0.0 
	 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码性能及规范性等方面的保障<br>
	 * 该接口参考文档位置：open.unionpay.com帮助中心 下载  产品接口规范  《网关支付产品接口规范》，<br>
	 *              《平台接入接口规范-第5部分-附录》（内包含应答码接口规范，全渠道平台银行名称-简码对照表）<br>
	 * 测试过程中的如果遇到疑问或问题您可以：1）优先在open平台中查找答案：
	 * 							        调试过程中的问题或其他问题请在 https://open.unionpay.com/ajweb/help/faq/list 帮助中心 FAQ 搜索解决方案
	 *                             测试过程中产生的6位应答码问题疑问请在https://open.unionpay.com/ajweb/help/respCode/respCodeList 输入应答码搜索解决方案
	 *                           2） 咨询在线人工支持： open.unionpay.com注册一个用户并登陆在右上角点击“在线客服”，咨询人工QQ测试支持。
	 * 交易说明： 1）对前台交易发起交易状态查询：前台类交易建议间隔（5分、10分、30分、60分、120分）发起交易查询，如果查询到结果成功，则不用再查询。（失败，处理中，查询不到订单均可能为中间状态）。也可以建议商户使用payTimeout（支付超时时间），过了这个时间点查询，得到的结果为最终结果。
	 *        2）对后台交易发起交易状态查询：后台类资金类交易同步返回00，成功银联有后台通知，商户也可以发起 查询交易，可查询N次（不超过6次），每次时间间隔2N秒发起,即间隔1，2，4，8，16，32S查询（查询到03，04，05继续查询，否则终止查询）。
	 *        					         后台类资金类同步返03 04 05响应码及未得到银联响应（读超时）需发起查询交易，可查询N次（不超过6次），每次时间间隔2N秒发起,即间隔1，2，4，8，16，32S查询（查询到03，04，05继续查询，否则终止查询）。
	 */
	@RequestMapping(value = "/traQuery", method = RequestMethod.POST)
	@ResponseBody
	public ResponseViewObject back_trade_query(BackTradeQueryVO backTradeQueryVO) {
		try {
			ResponseViewObject object = tradeService.paramsQueryValidate(backTradeQueryVO);

			if (object == null) {
				Map<String, String> reqMap = tradeService.queryVOToMap(backTradeQueryVO);
				object = tradeService.tradeQuery(reqMap);
			}
			return object;
		} catch (Exception e) {
			ResponseViewObject object = new ResponseViewObject();
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("交易异常！");
			logger.debug("交易异常！", e);
			return object;
		}
	}
	
	/**
	 * 交易：预授权：前台跳转，有前台通知应答和后台通知应答,网关,手机页面,手机控件<br>
	 * 日期： 2016-05-05<br>
	 * 版本： 1.0.0
	 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码性能规范性等方面的保障<br>
	 * 提示：该接口参考文档位置：open.unionpay.com帮助中心 下载  产品接口规范  《网关支付产品接口规范》，<br>
	 *              《平台接入接口规范-第5部分-附录》（内包含应答码接口规范，全渠道平台银行名称-简码对照表)<br>
	 *              《全渠道平台接入接口规范 第3部分 文件接口》（对账文件格式说明）<br>
	 * 测试过程中的如果遇到疑问或问题您可以：1）优先在open平台中查找答案：
	 * 							        调试过程中的问题或其他问题请在 https://open.unionpay.com/ajweb/help/faq/list 帮助中心 FAQ 搜索解决方案
	 *                             测试过程中产生的6位应答码问题疑问请在https://open.unionpay.com/ajweb/help/respCode/respCodeList 输入应答码搜索解决方案
	 *                          2） 咨询在线人工支持： open.unionpay.com注册一个用户并登陆在右上角点击“在线客服”，咨询人工QQ测试支持。
	 * 交易说明:1）以后台通知或交易状态查询交易确定交易成功,前台通知不能作为判断成功的标准.
	 *       2）交易状态查询交易（query_trade）建议调用机制：前台类交易间隔（5分、10分、30分、60分、120分）发起交易查询，如果查询到结果成功，则不用再查询。（失败，处理中，查询不到订单均可能为中间状态）。也可以建议商户使用payTimeout（支付超时时间），过了这个时间点查询，得到的结果为最终结果。
	 */
	@RequestMapping(value = "/traAuth", method = RequestMethod.POST)
	public void back_trade_auth(AuthRequestVO authRequestVO, HttpServletResponse resp) {
		try {
			ResponseViewObject object = tradeService.paramsAuthValidate(authRequestVO);

			if (object == null) {
				Map<String, String> reqMap = tradeService.authVOToMap(authRequestVO);
				object = tradeService.tradeAuth(reqMap);
				resp.getWriter().write(String.valueOf(object.getData().get("html")));
			}
		} catch (Exception e) {
			ResponseViewObject object = new ResponseViewObject();
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("交易异常！");
			logger.debug("交易异常！", e);
		}
	}
	

	@RequestMapping(value = "/fileTrans", method = RequestMethod.POST)
	@ResponseBody
	public ResponseViewObject file_transfer(FileTransferVO fileTransferVO) {
		try {
			ResponseViewObject object = tradeService.paramsFileValidate(fileTransferVO);

			if (object == null) {
				Map<String, String> reqMap = tradeService.fileObjToMap(fileTransferVO);
				object = tradeService.fileTrans(reqMap);
			}
			return object;
		} catch (Exception e) {
			ResponseViewObject object = new ResponseViewObject();
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("交易异常！");
			logger.debug("交易异常！", e);
			return object;
		}
	}
	
	@RequestMapping(value="/index")
	public String toIndex(){
		return "web/index";
	}

}
