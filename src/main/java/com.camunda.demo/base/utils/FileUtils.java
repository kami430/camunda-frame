package com.camunda.demo.base.utils;

import com.camunda.demo.base.configuration.SystemConfig;
import com.camunda.demo.base.http.BusinessException;
import com.camunda.demo.base.http.ResponseCode;
import org.apache.commons.io.IOUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileUtils {

    private static String FILEDESK = "D:/uploadPath4J";

    private static SystemConfig config = SystemConfig.Static.getConfig();

    /**
     * 初始化配置的文件路径
     */
    static {
        if (config.getFileDesk() != null) {
            FILEDESK = config.getFileDesk();
        }
    }

    /**
     * 文件上传
     *
     * @param file 上传的文件
     * @return
     * @throws IOException
     */
    public static String upload(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new FileNotFoundException("上传文件为空");
        }
        String path = getUploadFilePath(file.getOriginalFilename());
        File destFile = new File(path);
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
        file.transferTo(destFile);
        return path;
    }

    /**
     * 多文件上传
     *
     * @param files 上传的文件数组
     * @return
     * @throws IOException
     */
    public static List<String> upload(MultipartFile[] files) throws IOException {
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new FileNotFoundException("上传文件为空");
            }
        }
        List<String> paths = new ArrayList<>();
        for (MultipartFile file : files) {
            paths.add(upload(file));
        }
        return paths;
    }

    /**
     * 文件下载
     *
     * @param path     文件路径
     * @param response 输出流
     */
    public static void download(String path, HttpServletResponse response) {
        File file = new File(path);
        if (!file.exists()) {
            throw new BusinessException(ResponseCode.NOT_FOUND, "文件不存在");
        }
        try (InputStream inputStream = new FileInputStream(file);
             OutputStream outputStream = response.getOutputStream()) {
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;filename=" + getDownloadFileName(file.getName()));
            response.setCharacterEncoding("UTF-8");
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.SERVER_ERROR, e.getMessage(), e);
        }
    }

    /**
     * 文件预览
     *
     * @param path     文件路径
     * @param response 输出流
     */
    public static void preview(String path, HttpServletResponse response) {
        File file = new File(path);
        if (!file.exists()) {
            throw new BusinessException(ResponseCode.NOT_FOUND, "文件不存在");
        }
        try (InputStream inputStream = new FileInputStream(file);
             OutputStream outputStream = response.getOutputStream()) {
            response.setContentType(getContentType(path));
            response.addHeader("Content-Disposition", "inline;filename=" + getDownloadFileName(file.getName()));
            response.setCharacterEncoding("UTF-8");
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.SERVER_ERROR, e.getMessage(), e);
        }
    }

    /**
     * 获取上传的文件名
     *
     * @param file
     * @return
     */
    public static String getUploadFileName(MultipartFile file) {
        return file.getOriginalFilename();
    }

    /**
     * 获取上传的文件类型
     *
     * @param file
     * @return
     */
    public static String getUploadFileExt(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        int lastIndex = fileName.lastIndexOf(".");
        return lastIndex != -1 ? fileName.substring(fileName.lastIndexOf(".") + 1) : "";
    }

    /**
     * 构建上传路径
     *
     * @param name 文件名
     * @return
     */
    private static String getUploadFilePath(String name) {
        // 获取文件路径(文件名后加时间戳防止文件名冲突)
        LocalDateTime now = LocalDateTime.now();
        String datePath = now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        int id = Math.abs(UUID.randomUUID().hashCode());
        boolean hasSuffixName = name.indexOf(".") != -1;
        String suffixName = hasSuffixName ? name.substring(name.lastIndexOf(".")) : "";
        name = hasSuffixName ? name.substring(0, name.lastIndexOf(".")) : name;
        name = name + "_" + id + suffixName;
        return FILEDESK + "/" + datePath + "/" + name;
    }

    /**
     * 获取文件原名
     *
     * @param name
     * @return
     */
    private static String getDownloadFileName(String name) throws UnsupportedEncodingException {
        int index1 = name.lastIndexOf("_");
        int index2 = name.lastIndexOf(".");
        if (index1 != -1 && index2 != -1) {
            name = name.substring(0, index1) + name.substring(index2);
        } else if (index2 == -1) {
            name = name.substring(0, index1);
        }
        name = new String(name.getBytes("UTF-8"), "iso-8859-1");
        return name;
    }

    /**
     * 根据文件名/文件路径获取contentType
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    private static String getContentType(String path) throws IOException {
        String contentType = Files.probeContentType(Paths.get(path));
        return StringUtils.isEmpty(contentType) ? "application/octet-stream" : contentType;
    }

}
