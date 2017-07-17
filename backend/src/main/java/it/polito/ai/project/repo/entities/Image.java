package it.polito.ai.project.repo.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "images")
public class Image {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private byte[] value;

	public Image() {
	}

	public Image(byte[] value) {
		this.value = value;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}
	
	public String getUrl() {
		if (id != null) {
			// TODO find dynamic way of building this link
			return "https://localhost:8888/images/" + id + "/raw";
			/* this works only inside web context, not when this function is called by the websocket controller
			Link selfLink = ControllerLinkBuilder
					.linkTo(ControllerLinkBuilder.methodOn(ImagesController.class).getImageResource(id))
					.withSelfRel();
			Link rawLink = new Link(selfLink.getHref() + "/raw", "raw");
			return rawLink.getHref();*/
		} else {
			return null;
		}
		
	}

}
