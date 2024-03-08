package app.controller;

import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import app.model.Person;
import app.service.PersonService;
import app.service.ServiceException;

@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonController {
	private PersonService personService = new PersonService();

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response addPerson(String json) {
		Person person = new Gson().fromJson(json, Person.class);
		try {
			person.setPersonId("p" + System.currentTimeMillis());
			personService.add(person);
			return Response.status(Response.Status.CREATED).entity(new Gson().toJson(person)).build();
		} catch (IllegalArgumentException | ServiceException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Response getPersonById(@PathParam("id") String id) {
		Optional<Person> personOptional = personService.findById(id);
		if (personOptional.isPresent()) {
			return Response.status(Response.Status.OK).entity(new Gson().toJson(personOptional.get())).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Person not found").build();
		}
	}

	@GET
	@Produces("application/json")
	public String getAllPersons() {
		return new Gson().toJson(personService.list());
	}

}
