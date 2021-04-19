package com.assignment.comment.app.dao;

import java.time.Instant;
import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.document.KeyAttribute;
import com.assignment.comment.app.infra.aws.dynamo.DynamoClient;
import com.assignment.comment.app.model.CommentModel;
import com.assignment.comment.app.process.TextIdentificationProcess;

@Component
public class CommentDoaImpl implements CommentDoa {

	private DynamoClient dynamoClient;

	private TextIdentificationProcess textIdentificationProcess;

	public CommentDoaImpl(DynamoClient dynamoClient, TextIdentificationProcess textIdentificationProcess) {
		this.dynamoClient = dynamoClient;
		this.textIdentificationProcess = textIdentificationProcess;
	}

	@Override
	public boolean createComment(CommentModel commentModel) {
		return this.textIdentificationProcess.startProcess(commentModel);
	}

	@Override
	public void softDeleteItem(String commentId) {
		this.dynamoClient.updateItem("comment-table", "set isActive = :isActive, updatedAt = :updatedAt",
				new HashMap<String, Object>() {
					{
						put(":isActive", 0);
						put(":updatedAt", Instant.now().getEpochSecond());
					}
				}, new KeyAttribute("id", commentId));
	}
}
