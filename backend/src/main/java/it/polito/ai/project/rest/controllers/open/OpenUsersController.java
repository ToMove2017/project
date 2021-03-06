package it.polito.ai.project.rest.controllers.open;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import it.polito.ai.project.business.services.users.UsersService;
import it.polito.ai.project.rest.resources.AnonymizedUser;

/**
 * 
 * This class provides an open endpoint for users as required in assignment 4
 *
 */
@RestController
public class OpenUsersController {

	@Autowired
	private UsersService usersService;

	@RequestMapping(value = "/api/open/users", method = RequestMethod.GET, headers = "Accept=application/json")
	public PagedResources<Resource<AnonymizedUser>> getOpenUsers(Pageable pageable,
			PagedResourcesAssembler<AnonymizedUser> assembler) {
		Page<AnonymizedUser> anonimizedUsers = usersService
				// get the users
				.findAllUsers(pageable)
				// anonymize them
				.map(u -> new AnonymizedUser(u));
		// return result HATEOAS
		return assembler.toResource(anonimizedUsers);
	}
}