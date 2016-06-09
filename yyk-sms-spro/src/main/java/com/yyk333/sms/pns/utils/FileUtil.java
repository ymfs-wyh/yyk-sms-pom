package com.yyk333.sms.pns.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.codec.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * 
 * @Title: FileUtil.java
 * Copyright: Copyright (c) 2014 
 * Company:杭州宁居科技有限公司
 * 
 * @author 柴观新
 * 2014-4-21 下午2:24:26
 * @version V1.0
 */
public class FileUtil {

    private final static Logger L = LoggerFactory.getLogger(FileUtil.class);
    
    /**
     * @param mFile
     *            文件
     * @param filePath
     *            路径
     * @param destFile
     *            目标文件
     * @return String 文件名称
     * @exception Exception
     *                异常
     */
    public static String saveFile(MultipartFile mFile, String filePath, File destFile) throws Exception {
        // 获取或创建存放的文件夹
        File dirPath = new File(filePath);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }

        // 保存文件
        // try {
        mFile.transferTo(destFile);
        return destFile.getName();
        // } catch (Exception e) {
        // L.error("保存上传文件异常", e);
        // return false;
        // }
        // return true;
    }

    /**
     * @param request 请求
     * @param picPath 图片路径
     * @param defaultPath 默认图片路径
     * @param response HttpServletResponse响应
     * @throws Exception 异常
     */
     public static void readImage(HttpServletRequest request, String picPath,
     String defaultPath, HttpServletResponse response) throws Exception {
         InputStream in = null;
         if (!StringUtils.isNotBlank(picPath)) {
             picPath = request.getServletContext().getRealPath(defaultPath);
         }
         try {
             in = new FileInputStream(picPath);
             response.setContentType("image/*");
             BufferedOutputStream bos = null;
             try {
                 bos = new BufferedOutputStream(response.getOutputStream());
                 int n;
                 byte[] b = new byte[256];
                 while ((n = in.read(b)) != -1) {
                     bos.write(b, 0, n);
                 }
             } catch (IOException e) {
                 L.error("读取文件异常", e);
             } finally {
                 if(bos!=null){
                     try {
                         bos.flush();
                         bos.close();
                     } catch (IOException e) {
                     }
                 }
                 if(in!=null){
                     try {
                         in.close();
                     } catch (IOException e) {
                     }
                 }
             }
         } catch (FileNotFoundException e) {
        	 if(StringUtils.isBlank(defaultPath)){
        		 L.error("读取文件不存在",e);
        		 return ;
        	 }
             picPath = request.getServletContext().getRealPath(defaultPath);
             BufferedOutputStream bos = null;
             try {
                 in = new FileInputStream(picPath);
                 response.setContentType("image/*");
                 
                 bos = new BufferedOutputStream(response.getOutputStream());
                 int n;
                 byte[] b = new byte[256];
                 while ((n = in.read(b)) != -1) {
                     bos.write(b, 0, n);
                 }
             } catch (IOException ee) {
                 L.error("读取文件异常", ee);
             } finally {
                 if(bos!=null){
                     try {
                         bos.flush();
                         bos.close();
                     } catch (IOException ee) {
                     }
                 }
                 if(in!=null){
                     try {
                         in.close();
                     } catch (IOException ee) {
                     }
                 }
             }
         }
     }
     
     /**
      * 
       * 截取文件路径
       * @param path 文件路径
       * @return String 路径
      */
     public static String buildPath(String path){
         if(path.indexOf("\\")>=0){
             path = path.substring(0, path.indexOf("\\"));
         }else{
             path = path.substring(0, path.indexOf("/",1));
         }
         return path;
     }

    /**
     * @param file
     *            MultipartFile
     * @param path
     *            路径
     * @exception Exception
     *                异常
     */
    private static String getUploadFile(MultipartFile file, String path) throws Exception {
        // 保存文件到本地
        File destFile = new File(path + System.currentTimeMillis() + file.getOriginalFilename());
        return FileUtil.saveFile(file, path, destFile);
    }

    private static String getUploadFileAndName(MultipartFile file, String path) throws Exception {
        // 保存文件到本地
        String destName = path + UUID.randomUUID().toString() + file.getOriginalFilename();
        File destFile = new File(destName);
        FileUtil.saveFile(file, path, destFile);
        return destName;
    }

    /**
     * 多文件上传
     * 
     * @param request
     *            请求
     * @param filePath
     *            路径
     * @return String 文件名称
     * @throws Exception 异常
     */
    public static String doMultiUpload(HttpServletRequest request, String filePath) throws Exception{
        String fileNames = "";
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Iterator<String> iter = multipartRequest.getFileNames();
        while (iter.hasNext()) {
            String fileName = (String) iter.next();
            MultipartFile mFile = multipartRequest.getFile(fileName);
            if (!mFile.isEmpty()) { // 文件非空
                String tempFileName = getUploadFile(mFile, filePath);
                if(StringUtils.isNotEmpty(tempFileName)){
                    fileNames = tempFileName;
                }
            }
        }
        return fileNames;
    }

    /**
      * doMultiUpload(上传多文件)
      *
      * @Title: doMultiUpload
      * @param request HttpServletRequest
      * @param filePath 文件上传路径
      * @param saveFileNameList 用以存储上传文件的文件名
      * @return
      * @return Map<String,Object[]>    key是服务器上存储真实文件名，value是文件大小
      */
    public static Map<String, Object[]> doMultiUpload(HttpServletRequest request, String filePath,
            List<String> saveFileNameList) {
        Map<String, Object[]> destFileNameList = new HashMap<String, Object[]>();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Iterator<String> iter = multipartRequest.getFileNames();
        try {
            while (iter.hasNext()) {
                String fileName = (String) iter.next();
                MultipartFile mFile = multipartRequest.getFile(fileName);
                if (!mFile.isEmpty()) { // 文件非空
                    String destName = getUploadFileAndName(mFile, filePath);
                    Object[] temps = new Object[2];
                    temps[0] = destName;
                    temps[1] = mFile.getSize();
                    destFileNameList.put(mFile.getOriginalFilename(), temps);
                    saveFileNameList.add(mFile.getOriginalFilename());
                }
            }
        } catch (Exception e) {
            L.error("保存上传文件异常", e);
            return null;
        }
        return destFileNameList;
    }

    /**
     * 
     * readfile(读取某个文件夹下的所有文件)
     * 
     * @param filepath
     *            filepath
     * @return List<File> List<File>
     */
    public static List<InputStream> readfile(String filepath) {
        List<InputStream> files = new ArrayList<InputStream>();
        InputStream in = null;
        try {
            File file = new File(filepath);
            if (file.isDirectory()) {
                String[] filelist = file.list();
                if(filelist!=null){
                	 for (int i = 0; i < filelist.length; i++) {
                         File readfile = new File(filepath + "\\" + filelist[i]);
                         if (!readfile.isDirectory()) {
                             in = new FileInputStream(readfile);
                             files.add(in);
                         }
                     }
                }
            }

        } catch (Exception e) {
            L.error("读取文件异常", e);
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                L.error("InputStream异常", e);
            }
        }
        return files;
    }

    /**
     * 
     * readfile(读取某个文件夹下的所有文件)
     * 
     * @param filepath
     *            filepath
     * @return List<File> List<File>
     */
    public static List<File> readfileReturnFile(String filepath) {
        List<File> files = new ArrayList<File>();
        try {
            File file = new File(filepath);
            if (file.isDirectory()) {
                String[] filelist = file.list();
                if(filelist!=null){
                	 for (int i = 0; i < filelist.length; i++) {
                         File readfile = new File(filepath + "\\" + filelist[i]);
                         if (!readfile.isDirectory()) {
                             files.add(readfile);
                         }
                     }
                }
            }
        } catch (Exception e) {
            L.error("读取文件异常", e);
        }
        return files;
    }

    /**
     * 
     * 删除指定文件夹下所有文件
     * 
     * @param path
     *            文件夹完整绝对路径
     * @return boolean boolean
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        if(tempList!=null){
        	for (int i = 0; i < tempList.length; i++) {
                if (path.endsWith(File.separator)) {
                    temp = new File(path + tempList[i]);
                } else {
                    temp = new File(path + File.separator + tempList[i]);
                }
                if (temp.isFile()) {
                    temp.delete();
                }
                if (temp.isDirectory()) {
                    delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                    flag = true;
                }
            }
        }
        return flag;
    }

    /**
     * 
     * 删除指定文件
     * 
     * @param file
     *            文件
     * @return boolean boolean
     */
    public static boolean delFile(File file) {
        boolean flag = false;
        if (!file.exists()) {
            return flag;
        }
        if (file.isFile()) {
            file.delete();
            L.error("删除文件成功");
            flag = true;
        }
        return flag;
    }
    
    /**
     * 
      * delFile(根据文件路径删除文件)
      *
      * @Title: delFile
      * @param filePath 文件路径
      * @return boolean  是否删除成功
      * @throws
     */
    public static boolean delFile(String filePath) {
    	File file = new File(filePath);
    	if(file.exists()){
    		return delFile(file);
    	}
    	return false;
    }
    
    /**
     * 
      * readFirstLine(读取文件第一行数据)
      *
      * @Title: readFirstLine
      * @param filePath 文件路径
      * @return String   返回文件第一行数据
      * @throws
     */
    public static String readFirstLine(String filePath){
    	BufferedReader br = null;
    	try{
    		File file = new File(filePath);
        	br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            return br.readLine();
    	}catch(IOException e){
    		L.error("读取文件第一行数据异常",e);
    	}finally{
    		if(br!=null){
    			 try {
    				 br.close();
                 } catch (IOException ee) {
                 }
    		}
    	}
    	return null;
    }
    
	/**
	 * 读取txt文件的内容
	 * 
	 * @param file
	 *            想要读取的文件对象
	 * @return 返回文件内容
	 */
	public static String txt2String(String filePath) {
		String result = "";
		try {
			File file = new File(filePath);
			BufferedReader br= new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8")); 
			String s = "";
			while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
				result = result + s;
			}
			br.close();
		} catch (Exception e) {
		    L.error("读取txt文件的内容txt2String异常",e);
		}
		return result;
	}

	/**
	 * 创建文件
	 * 
	 * @param fileName
	 * @return
	 */
	public static String createFile(String filePath,String fileName,String fileContent) throws Exception {
		try {
			// 获取或创建存放的文件夹
	        File dirPath = new File(filePath);
	        if (!dirPath.exists()) {
	            dirPath.mkdirs();
	        }
	       
	        File file = new File(filePath+File.separator+fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileUtils.writeStringToFile(file, fileContent);//把md5写到相应的readme中
			return file.getAbsolutePath();
		} catch (Exception e) {
			L.error("创建文件createFile异常",e);
		}
		return "";
	}
	
	/**
     * 创建文件
     * 
     * @param fileName
     * @return
     */
    public static String writeFile(String filePath,String fileContent) throws Exception {
        try {
            // 获取或创建存放的文件夹
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileUtils.writeStringToFile(file, fileContent);//把内容写到相应的文件中
            return file.getAbsolutePath();
        } catch (Exception e) {
            L.error("writeFile写文件失败",e);
        }
        return "";
    }
	
	/**
     * download()
     *
     * @Title: download
     * @param request 请求
     * @param response 相应
     * @param file 文件
     * @return 成功或失败
     * @throws Exception 异常
     */
   public static boolean download(HttpServletRequest request, HttpServletResponse response,
           File file) throws Exception {

       BufferedInputStream bis = null;
       BufferedOutputStream bos = null;
       try {
           request.setCharacterEncoding("UTF-8");
           response.setContentType("application/x-msdownload;");

           String fileName = file.getName();

           String agent = request.getHeader("User-Agent");
           if (agent != null) { // 移动端可能没有设置头
               agent = agent.toLowerCase();
               if (agent.indexOf("firefox") != -1) {
                   // 解决空格会截断问题
                   fileName = "=?UTF-8?B?" + Base64.encodeToString(fileName.getBytes("UTF-8"))
                           + "?=";
               } else if (agent.indexOf("msie") != -1) {
                   fileName = URLEncoder.encode(fileName, "UTF-8");
                   fileName = fileName.replaceAll("\\+", "%20"); // 解决编码后空格变+号的情况
               } else if (agent.indexOf("chrome") != -1) {
                   fileName = "=?UTF-8?B?" + Base64.encodeToString(fileName.getBytes("UTF-8"))
                           + "?=";
               } else {
                   fileName = "=?UTF-8?B?" + Base64.encodeToString(fileName.getBytes("UTF-8"))
                           + "?=";
               }
           }

           response.setHeader("Content-disposition", "attachment; filename=" + fileName);
           response.setHeader("Content-Length", String.valueOf(file.length()));
           bis = new BufferedInputStream(new FileInputStream(file.getAbsolutePath()));
           bos = new BufferedOutputStream(response.getOutputStream());
           byte[] buff = new byte[bis.available()];
           int bytesRead;
           while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
               bos.write(buff, 0, bytesRead);
           }
           bos.flush();
       } catch (Exception e) {
           L.error("读取需下载的文件失败", e);
           throw new IllegalAccessError("读取需下载的文件失败");
       } finally {
           if (bis != null) {
               try {
                   bis.close();
               } catch (IOException e) {
                   throw new IOException("关闭文件异常");
               }
           }
           if (bos != null) {
               try {
                   bos.close();
               } catch (IOException e) {
                   throw new IOException("关闭文件异常");
               }
           }
       }
       return true;
   }
   
    /**
     * 移动文件
     * @param srcFile 移动目标文件
     * @param destPath 移动到的路径
     * @return
     */
	public static boolean moveFile(File srcFile, String destPath) {
		// Destination directory
		File dir = new File(destPath);
		if(!dir.exists()){
			dir.mkdirs();
		}
		// Move file to new directory
		boolean success = srcFile.renameTo(new File(dir, srcFile.getName()));
		return success;
	}
	
	public static void setHeader(HttpServletRequest request,
            HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
        response.setContentType("application/x-msdownload;");
        String agent=request.getHeader("User-Agent");
        if (agent != null) {    //移动端可能没有设置头
            agent = agent.toLowerCase();
            if (agent.indexOf("firefox") != -1) {
                //解决空格会截断问题
                fileName = "=?UTF-8?B?" + (new String(org.apache.commons.codec.binary.Base64.encodeBase64(fileName.getBytes("UTF-8")))) + "?=";
            } else if (agent.indexOf("msie") != -1) {
                fileName = URLEncoder.encode(fileName, "UTF-8");
                fileName = fileName.replaceAll("\\+", "%20"); //解决编码后空格变+号的情况
            } else if (agent.indexOf("chrome") != -1) {
                fileName = "=?UTF-8?B?" + (new String(org.apache.commons.codec.binary.Base64.encodeBase64(fileName.getBytes("UTF-8")))) + "?=";
            }else{
                fileName = "=?UTF-8?B?" + (new String(org.apache.commons.codec.binary.Base64.encodeBase64(fileName.getBytes("UTF-8")))) + "?=";
            }
        }
        response.setHeader("Content-disposition", "attachment; filename="
                + fileName);
    }
	
}
