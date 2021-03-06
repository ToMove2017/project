package it.polito.ai.project.business.services.emailVerification.sendgrid;

import java.util.ArrayList;
import java.util.List;

public class EmailPersonalization {

	private List<EmailAddress> to;
	private String subject;
	private EmailValues substitutions;

	EmailPersonalization(String destinationEmail, String nickname, String activation_link) {
		to = new ArrayList<>();
		to.add(new EmailAddress(destinationEmail));
		subject = "ToMove - email verification";
		substitutions = new EmailValues(nickname, activation_link, destinationEmail);
	}

	public List<EmailAddress> getTo() {
		return to;
	}

	public String getSubject() {
		return subject;
	}

	public EmailValues getSubstitutions() {
		return substitutions;
	}

}
