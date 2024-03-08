package app.controller;

import java.util.LinkedHashMap;
import java.util.Map;
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

import app.model.MoneyTransfer;
import app.model.Person;
import app.model.PersonAccount;
import app.service.MoneyTransferService;
import app.service.PersonService;
import app.service.ServiceException;

@Path("/transaction")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class MoneyTransferController {
	private MoneyTransferService moneyTransferService = new MoneyTransferService();

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response addTransaction(String json) {
		MoneyTransfer moneyTransfer = new Gson().fromJson(json, MoneyTransfer.class);
	    System.out.println("Amount received: " + moneyTransfer.getAmount());

		try {
			moneyTransfer.setFromAccountId("p" + System.currentTimeMillis());
			moneyTransferService.add(moneyTransfer);
	        System.out.println("Transaction added successfully: " + moneyTransfer);

			return Response.status(Response.Status.CREATED).entity(new Gson().toJson(moneyTransfer)).build();
		} catch (IllegalArgumentException | ServiceException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Response getAccountByNumber(@PathParam("id") String id) {
		Optional<PersonAccount> personAccountOptional = personAccountService.findById(id);
		if (personAccountOptional.isPresent()) {
			PersonService personService = new PersonService();
			Optional<Person> personOptional = personService.findById(personAccountOptional.get().getPersonId());
			Map<String,Object> result = new LinkedHashMap<>();
			result.put("personAccount", personAccountOptional.get());
			result.put("person", personOptional.get());
			return Response.status(Response.Status.OK).entity(new Gson().toJson(result)).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("PersonAccount not found").build();
		}
	}

	@GET
	@Produces("application/json")
	public String getAllPersons() {
		return new Gson().toJson(moneyTransferService.list());
	}}
