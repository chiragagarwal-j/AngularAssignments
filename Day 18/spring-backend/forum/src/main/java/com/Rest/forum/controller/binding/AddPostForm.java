package com.Rest.forum.controller.binding;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AddPostForm {
	private String content;
	private int userId;
	private LocalDateTime scheduleDate;
}
