package com.dao;

import com.pojo.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeDao  extends JpaRepository<Resume,Long> {

}
