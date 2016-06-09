package com.yyk333.sms.utils;

import java.io.File;

import com.aliyun.oss.OSSClient;

public class OSSUtil {

	// endpoint是访问OSS的域名。如果您已经在OSS的控制台上 创建了Bucket，请在控制台上查看域名。
	// 如果您还没有创建Bucket，endpoint选择请参看文档中心的“开发人员指南 > 基本概念 > 访问域名”，
	// 链接地址是：https://help.aliyun.com/document_detail/oss/user_guide/oss_concept/endpoint.html?spm=5176.docoss/user_guide/endpoint_region
	// endpoint的格式形如“http://oss-cn-hangzhou.aliyuncs.com/”，注意http://后不带bucket名称，
	// 比如“http://bucket-name.oss-cn-hangzhou.aliyuncs.com”，是错误的endpoint，请去掉其中的“bucket-name”。
	private static final String endpoint = PropertiesUtil.getProperty("file_oss_endpoint",
			"http://yyk333-test-1.oss.yyk333.com");

	// accessKeyId和accessKeySecret是OSS的访问密钥，您可以在控制台上创建和查看，
	// 创建和查看访问密钥的链接地址是：https://ak-console.aliyun.com/#/。
	// 注意：accessKeyId和accessKeySecret前后都没有空格，从控制台复制时请检查并去除多余的空格。
	private static final String accessKeyId = PropertiesUtil.getProperty("file_oss_access_key_id", "RvRDORKcVgv8z2sB");
	private static final String accessKeySecret = PropertiesUtil.getProperty("file_oss_access_key_secret",
			"Hyhl4OIUqxecXxHpUro1kus4eRSMsz");

	// Bucket用来管理所存储Object的存储空间，详细描述请参看“开发人员指南 > 基本概念 > OSS基本概念介绍”。
	// Bucket命名规范如下：只能包括小写字母，数字和短横线（-），必须以小写字母或者数字开头，长度必须在3-63字节之间。
	public static final String bucketName = PropertiesUtil.getProperty("file_oss_bucket_name", "yyk333-test-1");

	private static final OSSUtil OSS_UTIL = new OSSUtil();

	private OSSClient ossClient = null;

	public OSSUtil() {
		ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
	}

	public static OSSUtil getInstance() {
		return OSS_UTIL;
	}

	public boolean uploadFile(String bucketName, String dirPathAndName, String localPathAndName) {
		if (doesBucketExist(bucketName)) {
			ossClient.putObject(bucketName, dirPathAndName, new File(localPathAndName));
			return true;
		}
		return false;
	}

	public boolean doesBucketExist(String bucketName) {
		if (ossClient.doesBucketExist(bucketName)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void main(String[] args) {
		OSSUtil.getInstance().uploadFile("yyk333-test-1","Public/Uploads/firing/5746573fc83bf.jpg","D:/development/doc/work/desk/待办事宜/启动页&版本更新/firing/5746573fc83bf.jpg");
	}

}
