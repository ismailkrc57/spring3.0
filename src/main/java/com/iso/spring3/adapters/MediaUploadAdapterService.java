package com.iso.spring3.adapters;

import com.iso.spring3.core.utils.result.DataResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


public interface MediaUploadAdapterService {
    DataResult<Map<?, ?>> uploadImage(MultipartFile image);

}
