package com.service.impl;

import com.dao.ResumeDao;
import com.pojo.Resume;
import com.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResumeServiceImpl implements ResumeService {

    @Override
    public Resume findDefaultResumeByUserId(Long userId) {
        Resume resume = new Resume();
        resume.setName("假装这是我从数据库里查到的");
        return resume;
    }
}
