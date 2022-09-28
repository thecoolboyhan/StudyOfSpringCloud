package com.service;

import com.pojo.Resume;

public interface ResumeService {
    Resume findDefaultResumeByUserId(Long userId);
}
