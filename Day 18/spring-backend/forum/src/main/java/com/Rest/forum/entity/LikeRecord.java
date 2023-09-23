package com.Rest.forum.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;

@Entity
@Data
public class LikeRecord {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@EmbeddedId
	private LikeId likeId;

}
