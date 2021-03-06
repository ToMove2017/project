package it.polito.ai.project.business.services.alerts;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.polito.ai.project.repo.AlertTypesRepository;
import it.polito.ai.project.repo.AlertsRepository;
import it.polito.ai.project.repo.RatingsRepository;
import it.polito.ai.project.repo.entities.User;
import it.polito.ai.project.repo.entities.alerts.Alert;
import it.polito.ai.project.repo.entities.alerts.AlertType;
import it.polito.ai.project.repo.entities.alerts.Rating;
import it.polito.ai.project.repo.entities.alerts.RatingKey;
import it.polito.ai.project.web.exceptions.NotFoundException;

@Service
@Transactional
public class AlertsServiceImpl implements AlertsService {
	private final static int maxAlertDuration = 5;

	@Autowired
	private AlertTypesRepository alertTypesRepository;
	@Autowired
	private AlertsRepository alertsRepository;
	@Autowired
	private RatingsRepository ratingsRepository;

	@Override
	public List<AlertType> getAlertsType() {
		return alertTypesRepository.findAll();
	}

	@Override
	public AlertType getAlertTypeById(Long id) {
		if (id == null) {
			return null;
		}
		return alertTypesRepository.findOne(id);
	}

	@Override
	public List<Alert> getAlerts() {
		// get the currentTime - maxAlertDuration
		Date time = getCurrentTimeMinusMaxAlertDuration();

		// get and return the list of active alerts
		return alertsRepository.findByLastViewTimeAfter(time);
	}

	@Override
	public Alert getAlertById(Long id) {
		if (id == null) {
			return null;
		}
		// get the currentTime - maxAlertDuration
		Date time = getCurrentTimeMinusMaxAlertDuration();
		return alertsRepository.findOneByIdAndLastViewTimeAfter(id, time);
	}

	@Override
	public List<Alert> getAlertsByHashtag(String hashtag) {
		// get the currentTime - maxAlertDuration
		Date time = getCurrentTimeMinusMaxAlertDuration();

		// get and return the list of active alerts that match the requested
		// hashtag
		return alertsRepository.findByHashtagContainingAndLastViewTimeAfter(hashtag, time);
	}

	@Override
	public Alert addNewAlert(Alert newAlert) {
		return alertsRepository.save(newAlert);
	}

	@Override
	public Alert updateAlertLastViewTime(Long id) {
		if (id == null) {
			return null;
		}
		// get the alert
		// get the currentTime - maxAlertDuration
		Date time = getCurrentTimeMinusMaxAlertDuration();
		Alert alert = alertsRepository.findOneByIdAndLastViewTimeAfter(id, time);

		if (alert == null) {
			// alert not found or expired
			return null;
		}

		// update the last view time with the current time
		alert.setLastViewTime(new Date());

		// update the alert into the DB
		alertsRepository.save(alert);

		return alert;
	}

	@Override
	public Alert voteAlert(User user, Long alertId, int vote) {
		// FIXME the returned alert rating is not updated. It seems that the
		// alert entity is returned before
		// the execution of the trigger that updates the average rate of the
		// voted alert

		// get the currentTime - maxAlertDuration
		Date time = getCurrentTimeMinusMaxAlertDuration();
		Alert alert = alertsRepository.findOneByIdAndLastViewTimeAfter(alertId, time);
		if (alert == null) {
			// alert not found or expired
			throw new NotFoundException();
		}

		// Insert the new rating, or replace the old one if it already exists
		Rating newRating = new Rating(user, alert, vote);

		ratingsRepository.save(newRating);

		return alert;
	}

	public Rating getAlertRatingForUser(Long alertId, User user) {
		Alert alert = alertsRepository.findOne(alertId);
		if (alert == null) {
			return null;
		}
		RatingKey ratingKey = new RatingKey(user, alert);
		return ratingsRepository.findOne(ratingKey);
	}

	private Date getCurrentTimeMinusMaxAlertDuration() {
		// get the current time
		Date currentTime = new Date();

		// subtract the maxAlertDuration from the current time
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentTime);
		calendar.add(Calendar.MINUTE, -maxAlertDuration);
		Date result = calendar.getTime();

		return result;
	}

}
