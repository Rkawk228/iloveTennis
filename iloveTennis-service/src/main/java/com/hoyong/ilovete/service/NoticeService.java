package com.hoyong.ilovete.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NoticeService {
	
	@Autowired
	private NoticeMapper noticeMapper; 
}
