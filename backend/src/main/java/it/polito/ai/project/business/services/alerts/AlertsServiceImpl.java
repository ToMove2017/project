package it.polito.ai.project.business.services.alerts;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import it.polito.ai.project.repo.entities.alerts.Alert;
import it.polito.ai.project.repo.entities.alerts.AlertType;

@Service
@Transactional
public class AlertsServiceImpl implements AlertsService {

	@Override
	public List<AlertType> getAlertsType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Alert> getAlerts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Alert getAlertById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Alert> getAlertsByHashtag(String hashtag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Alert addNewAlert(Alert newAlert) {
		// TODO Auto-generated method stub
		return null;
	}

}
