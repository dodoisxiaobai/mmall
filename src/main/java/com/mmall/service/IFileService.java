package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author dodo
 * @date 2018/4/26
 * @description
 */
public interface IFileService {

    String upload(MultipartFile file, String path);
}
